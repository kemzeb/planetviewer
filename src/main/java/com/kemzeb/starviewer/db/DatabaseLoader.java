package com.kemzeb.starviewer.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.starviewer.exoplanet.entity.ExoplanetRepository;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.star.entity.StarRepository;
import com.kemzeb.starviewer.system.entity.PlanetarySystem;
import com.kemzeb.starviewer.system.entity.PlanetarySystemRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@Transactional
@ConditionalOnProperty(name = "starviewer.should-cli-runner-run", havingValue = "true")
@RequiredArgsConstructor
public class DatabaseLoader {

  private static final String planetarySystemArchiveFilename = "ps.json";

  private final PsArchiveMapper psArchiveMapper;
  private final ObjectMapper objectMapper;

  private final PlanetarySystemRepository planetarySystemRepository;
  private final ExoplanetRepository exoplanetRepository;
  private final StarRepository starRepository;

  @Bean
  CommandLineRunner initDatabase() {
    return args -> {
      buildTables();
    };
  }

  private void buildTables() {
    if (starRepository.count() > 0) {
      return;
    }

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
          PlanetarySystem starSystem = psArchiveMapper.toStarSystem(archive);
          starSystem = planetarySystemRepository.save(starSystem);

          Star star = psArchiveMapper.toStar(archive);
          star.setStarSystem(starSystem);

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

        PlanetarySystem starSystem = star.getStarSystem();
        psArchiveMapper.updateStarSystem(archive, starSystem);
        planetarySystemRepository.save(starSystem);

        psArchiveMapper.updateExoplanet(archive, exoplanet);
        exoplanetRepository.save(exoplanet);
        continue;
      }

      psArchiveMapper.updateNullFieldsInStar(archive, star);
      star = starRepository.save(star);

      PlanetarySystem starSystem = star.getStarSystem();
      psArchiveMapper.updateNullFieldsInStarSystem(archive, starSystem);
      planetarySystemRepository.save(starSystem);

      psArchiveMapper.updateNullFieldsInExoplanet(archive, exoplanet);
      exoplanetRepository.save(exoplanet);
    }
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
}
