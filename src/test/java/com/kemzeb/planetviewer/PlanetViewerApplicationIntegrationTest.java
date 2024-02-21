package com.kemzeb.planetviewer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "default"})
class PlanetViewerApplicationIntegrationTest {

  @Test
  void contextLoads() {}
}
