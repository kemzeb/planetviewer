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
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

@Configuration
@Transactional
@ConditionalOnProperty(name = "planetviewer.should-cli-runner-run", havingValue = "true")
@RequiredArgsConstructor
public class DatabaseLoader {

  private static final String planetarySystemArchiveFilename = "ps.json";

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

    buildIndex(
        exoplanetRepository, exoplanetDocumentRepository, exoplanetMapper::toExoplanetDocument);
    buildIndex(starRepository, starDocumentRepository, starMapper::toStarDocument);

    logger.info("Finished creating new documents for ElasticSearch.");
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
