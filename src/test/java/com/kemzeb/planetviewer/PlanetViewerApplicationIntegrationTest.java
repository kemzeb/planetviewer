package com.kemzeb.planetviewer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("elasticsearch")
class PlanetViewerApplicationIntegrationTest {

  @Test
  void contextLoads() {}
}
