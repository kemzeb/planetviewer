package com.kemzeb.starviewer.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemzeb.starviewer.document.ExoplanetDocument;
import com.kemzeb.starviewer.entity.Exoplanet;
import com.kemzeb.starviewer.entity.PlanetarySystem;
import com.kemzeb.starviewer.entity.Star;
import com.kemzeb.starviewer.mapper.ExoplanetMapper;
import com.kemzeb.starviewer.repository.ExoplanetDocumentRepository;
import com.kemzeb.starviewer.repository.ExoplanetRepository;
import com.kemzeb.starviewer.repository.PlanetarySystemRepository;
import com.kemzeb.starviewer.repository.StarRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Configuration
@Transactional
@ConditionalOnProperty(name = "starviewer.should-cli-runner-run", havingValue = "true")
@RequiredArgsConstructor
public class DatabaseLoader {

  private static final String planetarySystemArchiveFilename = "ps.json";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final PsArchiveMapper psArchiveMapper;
  private final ExoplanetMapper exoplanetMapper;
  private final ObjectMapper objectMapper;

  private final PlanetarySystemRepository planetarySystemRepository;
  private final ExoplanetRepository exoplanetRepository;
  private final StarRepository starRepository;
  private final ExoplanetDocumentRepository exoplanetDocumentRepository;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      buildTables();
      buildElasticSearchIndicies();
    };
  }

  private void buildTables() {
    if (starRepository.count() > 0) {
      return;
    }

    logger.info("Initializing PostgreSQL database...");

    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource("classpath:" + planetarySystemArchiveFilename);
    String content = tryToStringifyResource(resource);
    List<PsArchive> systemArchives =
        tryToParseJson(content, new TypeReference<List<PsArchive>>() {});

    for (PsArchive archive : systemArchives) {
      Optional<Exoplanet> maybeExoplanet = exoplanetRepository.findById(archive.plName);

      if (maybeExoplanet.isEmpty()) {
        Optional<Star> maybeStar = starRepository.findById(archive.hostname);

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

  private String tryToStringifyResource(Resource resource) {
    try {
      return resource.getContentAsString(Charset.defaultCharset());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    } catch (NullPointerException ex) {
      throw new RuntimeException(
          "Database cannot be initalized because there's no ps.json in the resource/ directory."
              + " Please run scripts/archive-fetch.sh.");
    }
  }

  private void buildElasticSearchIndicies() {
    logger.info("Deleting old documents from ElasticSearch...");

    exoplanetDocumentRepository.deleteAll();

    logger.info("Finished deleting old documents from ElasticSearch.");
    logger.info("Creating new documents for ElasticSearch...");

    Page<Exoplanet> page = exoplanetRepository.findAll(PageRequest.of(0, 64));

    do {
      List<ExoplanetDocument> exoplanetDocuments =
          page.getContent().stream().map(exoplanetMapper::toExoplanetDocument).toList();

      exoplanetDocumentRepository.saveAll(exoplanetDocuments);
      page = exoplanetRepository.findAll(page.nextPageable());
    } while (page.hasNext());

    logger.info("Finished creating new documents for ElasticSearch.");
  }
}
