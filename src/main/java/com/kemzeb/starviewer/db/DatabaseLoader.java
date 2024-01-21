package com.kemzeb.starviewer.db;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.star.entity.StarRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ConditionalOnProperty(name = "starviewer.should-cli-runner-run", havingValue = "true")
@RequiredArgsConstructor
public class DatabaseLoader {

  @Value("${starviewer.mission-stars-exocat-json-filename}")
  private String starArchiveFilename;

  private final ObjectMapper objectMapper;

  @Bean
  CommandLineRunner initDatabase(StarRepository repository) {
    return args -> {
      buildStarTable(repository);
    };
  }

  private void buildStarTable(StarRepository repository) {
    if (repository.count() > 0) {
      return;
    }

    if (starArchiveFilename.isBlank()) {
      throw new RuntimeException("starviewer.mission-stars-exocat-json-filename property not set.");
    }

    // TODO: Fetch from the archive URL.

    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource resource = resourceLoader.getResource("classpath:" + starArchiveFilename);
    String content = tryToStringifyResource(resource);
    List<ArchiveStar> archiveStars =
        tryToParseJson(content, new TypeReference<List<ArchiveStar>>() {});

    for (ArchiveStar star : archiveStars) {
      Star starEntity = new Star();
      starEntity.setName(star.starName);
      starEntity.setDistanceParsecs(star.stDist);
      starEntity.setSpectralType(star.stSpttype);

      repository.save(starEntity);
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
    }
  }
}
