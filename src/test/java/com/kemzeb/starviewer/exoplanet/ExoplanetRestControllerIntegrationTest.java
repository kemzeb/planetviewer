package com.kemzeb.starviewer.exoplanet;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.starviewer.exoplanet.entity.ExoplanetRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ExoplanetRestControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private ExoplanetRepository exoplanetRepository;

  @Test
  public void givenExistingExoplanet_whenFindingExoplanet_thenExpect200() throws Exception {
    // Given
    String name = "Novalis";

    Exoplanet exoplanet = new Exoplanet();
    exoplanet.setName(name);

    // When
    when(exoplanetRepository.findById(eq(name))).thenReturn(Optional.of(exoplanet));

    // Then
    mockMvc
        .perform(get("/planets/{name}", name).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._links.self.href").exists());
  }
}
