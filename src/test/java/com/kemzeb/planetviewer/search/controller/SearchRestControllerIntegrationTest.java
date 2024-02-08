package com.kemzeb.planetviewer.search.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kemzeb.planetviewer.search.dto.CelestialBodySearchHit;
import com.kemzeb.planetviewer.search.service.SearchService;
import com.kemzeb.planetviewer.util.PagedModelAssembler;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

@WebMvcTest(SearchRestController.class)
public class SearchRestControllerIntegrationTest {

  @TestConfiguration
  static class TestConfig {

    @Bean
    public PagedModelAssembler<CelestialBodySearchHit> pagedModelAssembler() {
      return new PagedModelAssembler<>();
    }
  }

  @Autowired private MockMvc mockMvc;
  @MockBean private SearchService searchService;

  @ParameterizedTest
  @ValueSource(strings = {"exoplanet", "star"})
  public void givenType_whenSearching_thenExpect200(String type) throws Exception {
    // Given
    String name = "Test";

    CelestialBodySearchHit searchHit = new CelestialBodySearchHit();
    searchHit.setName(name);

    // When
    when(searchService.search(any(), eq(name), any(), any()))
        .thenReturn(new PageImpl<>(List.of(searchHit)));

    // Then
    mockMvc
        .perform(get("/search").queryParam("q", name).queryParam("type", type))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.hits").exists())
        .andExpect(content().contentType("application/hal+json"));
  }

  @Test
  public void givenNegativePageNumber_whenSearching_thenExpect302() throws Exception {
    // Given
    String pageNumber = "-1";

    // When
    // Then
    mockMvc
        .perform(get("/search").queryParam("q", "test").queryParam("page", pageNumber))
        .andExpect(status().isFound());
  }

  @Test
  public void givenPageNumberGreaterThanTotalPages_whenSearching_thenExpect302() throws Exception {
    // Given
    String pageNumber = "64";
    Pageable pageable = PageRequest.of(2, 32);
    Page<CelestialBodySearchHit> page = new PageImpl<>(List.of(), pageable, 64L);

    // When
    when(searchService.search(any(), any(), any(), any())).thenReturn(page);

    // Then
    mockMvc
        .perform(get("/search").queryParam("q", "test").queryParam("page", pageNumber))
        .andExpect(status().isFound());
  }
}
