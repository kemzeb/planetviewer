package com.kemzeb.starviewer.exoplanet;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.starviewer.exoplanet.entity.ExoplanetRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @Test
  public void givenExistingStars_whenListingExoplanets_thenExpect200() throws Exception {
    // Given
    Pageable pageable = PageRequest.of(2, 32);

    Exoplanet a = new Exoplanet();
    a.setName("a");

    Exoplanet b = new Exoplanet();
    b.setName("a");

    Page<Exoplanet> page = new PageImpl<Exoplanet>(List.of(a, b), pageable, 128L);

    // When
    when(exoplanetRepository.findAll(isA(Pageable.class))).thenReturn(page);

    // Then
    mockMvc
        .perform(get("/planets"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        // Make sure a self relation is exposed
        .andExpect(jsonPath("$._links.self.href").exists())
        // ... and a prev action (because we're at page 2)
        .andExpect(jsonPath("$._links.prev.href").exists())
        // ... and a next action (because total elements = 128)
        .andExpect(jsonPath("$._links.next.href").exists())
        // ... and we have resources.
        .andExpect(jsonPath("$._embedded.exoplanets").exists());
  }

  @Test
  public void givenNegativePageNumber_whenListingExoplanets_thenExpect302() throws Exception {
    // Given
    String pageNumber = "-1";

    // When
    // Then
    mockMvc.perform(get("/planets?page=" + pageNumber)).andExpect(status().isFound());
  }

  @Test
  public void givenPageNumberGreaterThanTotalPages_whenListingExoplanets_thenExpect302()
      throws Exception {
    // Given
    String pageNumber = "64";
    Pageable pageable = PageRequest.of(2, 32);
    Page<Exoplanet> page = new PageImpl<Exoplanet>(List.of(), pageable, 64L);

    // When
    when(exoplanetRepository.findAll(isA(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/planets?page=" + pageNumber)).andExpect(status().isFound());
  }
}
