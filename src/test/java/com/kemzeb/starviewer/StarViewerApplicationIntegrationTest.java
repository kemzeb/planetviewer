package com.kemzeb.starviewer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("elasticsearch")
class StarViewerApplicationIntegrationTest {

  @Test
  void contextLoads() {}
}
