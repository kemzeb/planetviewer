package com.kemzeb.starviewer.root;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RootResourceRestControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void givenNormalScenario_whenListingLinks_thenExpect200() throws Exception {
    // Given
    // When
    // Then
    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._links.stars.href").exists())
        .andExpect(jsonPath("$._links.exoplanets.href").exists())
        .andExpect(jsonPath("$._links.self.href").exists());
  }
}
