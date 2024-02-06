package com.kemzeb.planetviewer.exoplanet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kemzeb.planetviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.planetviewer.exoplanet.service.ExoplanetService;
import com.kemzeb.planetviewer.util.PagedModelAssembler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExoplanetRestController.class)
public class ExoplanetRestControllerIntegrationTest {

  @TestConfiguration
  static class TestConfig {

    @Bean
    public PagedModelAssembler<ExoplanetDto> exoplanetPagedModelAssembler() {
      return new PagedModelAssembler<>();
    }
  }

  @Autowired private MockMvc mockMvc;
  @MockBean private ExoplanetService exoplanetService;

  @Test
  public void givenExistingExoplanet_whenFindingExoplanet_thenExpect200() throws Exception {
    // Given
    String name = "Novalis";

    ExoplanetDto exoplanetDto = new ExoplanetDto();
    exoplanetDto.setName(name);

    // When
    when(exoplanetService.findExoplanet(eq(name))).thenReturn(exoplanetDto);

    // Then
    mockMvc
        .perform(get("/planets/{name}", name).contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"));
  }

  @Test
  public void givenExistingStars_whenListingExoplanets_thenExpect200() throws Exception {
    // Given
    Pageable pageable = PageRequest.of(2, 32);

    ExoplanetDto a = new ExoplanetDto();
    a.setName("a");

    ExoplanetDto b = new ExoplanetDto();
    b.setName("a");

    Page<ExoplanetDto> page = new PageImpl<>(List.of(a, b), pageable, 128L);

    // When
    when(exoplanetService.getExoplanetList(any())).thenReturn(page);

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
    Page<ExoplanetDto> page = new PageImpl<>(List.of(), pageable, 64L);

    // When
    when(exoplanetService.getExoplanetList(any())).thenReturn(page);

    mockMvc.perform(get("/planets?page=" + pageNumber)).andExpect(status().isFound());
  }
}
