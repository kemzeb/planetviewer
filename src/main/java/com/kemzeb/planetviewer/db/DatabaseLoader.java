package com.kemzeb.planetviewer.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemzeb.planetviewer.db.entity.PsArchive;
import com.kemzeb.planetviewer.db.mapper.PsArchiveMapper;
import com.kemzeb.planetviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.planetviewer.exoplanet.mapper.ExoplanetMapper;
import com.kemzeb.planetviewer.exoplanet.repo.ExoplanetDocumentRepository;
import com.kemzeb.planetviewer.exoplanet.repo.ExoplanetRepository;
import com.kemzeb.planetviewer.star.entity.Star;
import com.kemzeb.planetviewer.star.mapper.StarMapper;
import com.kemzeb.planetviewer.star.repo.StarDocumentRepository;
import com.kemzeb.planetviewer.star.repo.StarRepository;
import com.kemzeb.planetviewer.system.entity.PlanetarySystem;
import com.kemzeb.planetviewer.system.repo.PlanetarySystemRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@Transactional
@ConditionalOnProperty(name = "planetviewer.should-cli-runner-run", havingValue = "true")
@RequiredArgsConstructor
public class DatabaseLoader {

  private static final String PS_ARCHIVE_FILENAME = "ps.json";
  private static final String PS_ARCHIVE_URI = "https://exoplanetarchive.ipac.caltech.edu";
  private static final String PS_ARCHIVE_URI_PATH = "/TAP/sync";

  @Value("${planetviewer.app-data-dir}")
  private String appDataDirPath;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final PsArchiveMapper psArchiveMapper;
  private final ExoplanetMapper exoplanetMapper;
  private final StarMapper starMapper;
  private final ObjectMapper objectMapper;

  private final PlanetarySystemRepository planetarySystemRepository;
  private final ExoplanetRepository exoplanetRepository;
  private final StarRepository starRepository;
  private final ExoplanetDocumentRepository exoplanetDocumentRepository;
  private final StarDocumentRepository starDocumentRepository;

  private final TaskExecutor taskExecutor;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      buildTables();
      buildElasticSearchIndicies();
    };
  }

  private void buildTables() {
    // TODO: This is not the best way to determine if we need to rebuild the tables.
    // What if we stop the server before we finish adding all the necessary rows?
    if (starRepository.count() > 0) {
      return;
    }

    logger.info("Initializing PostgreSQL database...");

    String content = tryToGetArchiveContent();

    List<PsArchive> systemArchives =
        tryToParseJson(content, new TypeReference<List<PsArchive>>() {});

    for (PsArchive archive : systemArchives) {
      Optional<Exoplanet> maybeExoplanet = exoplanetRepository.findById(archive.plName);

      if (maybeExoplanet.isEmpty()) {
        Optional<Star> maybeStar = starRepository.findById(archive.stellarHost);

        if (maybeStar.isEmpty()) {
          PlanetarySystem planetarySystem = psArchiveMapper.toPlanetarySystem(archive);
          planetarySystem = planetarySystemRepository.save(planetarySystem);

          Star star = psArchiveMapper.toStar(archive);
          star.setPlanetarySystem(planetarySystem);

          maybeStar = Optional.of(starRepository.save(star));
        }

        Exoplanet exoplanet = psArchiveMapper.toExoplanet(archive);
        exoplanet.setStellarHost(maybeStar.get());
        exoplanetRepository.save(exoplanet);
        continue;
      }

      Exoplanet exoplanet = maybeExoplanet.get();
      Star star = exoplanet.getStellarHost();

      if (archive.defaultFlag) {
        psArchiveMapper.updateStar(archive, star);
        star = starRepository.save(star);

        PlanetarySystem planetarySystem = star.getPlanetarySystem();
        psArchiveMapper.updatePlanetarySystem(archive, planetarySystem);
        planetarySystemRepository.save(planetarySystem);

        psArchiveMapper.updateExoplanet(archive, exoplanet);
        exoplanetRepository.save(exoplanet);
        continue;
      }

      psArchiveMapper.updateNullFieldsInStar(archive, star);
      star = starRepository.save(star);

      PlanetarySystem planetarySystem = star.getPlanetarySystem();
      psArchiveMapper.updateNullFieldsInPlanetarySystem(archive, planetarySystem);
      planetarySystemRepository.save(planetarySystem);

      psArchiveMapper.updateNullFieldsInExoplanet(archive, exoplanet);
      exoplanetRepository.save(exoplanet);
    }

    logger.info("Finished initializing PostgreSQL database.");
  }

  private <T> T tryToParseJson(String json, TypeReference<T> ref) {
    try {
      return objectMapper.readValue(json, ref);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private String tryToGetArchiveContent() {
    try {
      return Files.readString(
          Paths.get(String.format("%s/%s", appDataDirPath, PS_ARCHIVE_FILENAME)));
    } catch (NoSuchFileException e) {
      logger.info("PS table not stored locally. Fetching from " + PS_ARCHIVE_URI + "...");

      Path path = tryToFetchPsArchive();

      logger.info(
          String.format("Finished fetching PS table. Stored at %s.", path.toAbsolutePath()));

      try {
        return Files.readString(path);
      } catch (IOException ioException) {
        throw new RuntimeException(ioException);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path tryToFetchPsArchive() {
    HttpClient httpClient = HttpClient.newHttpClient();
    try {
      URI uri =
          UriComponentsBuilder.fromUriString(PS_ARCHIVE_URI)
              .path(PS_ARCHIVE_URI_PATH)
              .queryParam(
                  "query",
                  "select+default_flag,pl_name,hostname,sy_snum,"
                      + "sy_pnum,sy_mnum,discoverymethod,disc_year,"
                      + "disc_facility,pl_orbper,pl_rade,pl_bmasse,pl_eqt,st_spectype"
                      + ",st_teff,st_rad,st_mass,st_age,sy_dist,glat,glon+from+ps")
              .queryParam("format", "json")
              .build()
              .toUri();

      HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

      HttpResponse<Path> response =
          httpClient.send(
              request,
              HttpResponse.BodyHandlers.ofFile(
                  Path.of(String.format("%s/%s", appDataDirPath, PS_ARCHIVE_FILENAME))));

      return response.body();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void buildElasticSearchIndicies() {
    logger.info("Deleting old documents from ElasticSearch...");

    exoplanetDocumentRepository.deleteAll();
    starDocumentRepository.deleteAll();

    logger.info("Finished deleting old documents from ElasticSearch.");
    logger.info("Creating new documents for ElasticSearch...");

    taskExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            buildIndex(
                exoplanetRepository,
                exoplanetDocumentRepository,
                exoplanetMapper::toExoplanetDocument);
            logger.info("Finished building exoplanet index.");
          }
        });

    taskExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            buildIndex(starRepository, starDocumentRepository, starMapper::toStarDocument);
            logger.info("Finished building star index.");
          }
        });
  }

  private <V, T> void buildIndex(
      JpaRepository<V, ?> jpaRepository,
      ElasticsearchRepository<T, ?> esRepository,
      Function<V, T> mapper) {

    Page<V> page = jpaRepository.findAll(PageRequest.of(0, 64));

    do {
      List<T> documents = page.getContent().stream().map(mapper).toList();

      esRepository.saveAll(documents);
      page = jpaRepository.findAll(page.nextPageable());
    } while (page.hasNext());
  }
}
