package com.kemzeb.planetviewer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kemzeb.planetviewer.dto.ExoplanetDto;
import com.kemzeb.planetviewer.dto.StarDto;
import com.kemzeb.planetviewer.service.ExoplanetService;
import com.kemzeb.planetviewer.service.StarService;
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

@WebMvcTest(StarRestController.class)
public class StarRestControllerIntegrationTest {

  @TestConfiguration
  static class TestConfig {

    @Bean
    public PagedModelAssembler<StarDto> pagedModelAssembler() {
      return new PagedModelAssembler<>();
    }
  }

  @Autowired private MockMvc mockMvc;
  @MockBean private StarService starService;
  @MockBean private ExoplanetService exoplanetService;

  @Test
  public void givenExistingStar_whenFindingStar_thenExpect200() throws Exception {
    // Given
    String name = "HIP 171";

    StarDto starDto = new StarDto();
    starDto.setName(name);

    // When
    when(starService.findStar(eq(name))).thenReturn(starDto);

    // Then
    mockMvc
        .perform(get("/stars/{name}", "HIP%20171").contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"));
  }

  @Test
  public void givenExistingStars_whenListingStars_thenExpect200() throws Exception {
    // Given
    Pageable pageable = PageRequest.of(2, 32);

    StarDto a = new StarDto();
    a.setName("a");

    StarDto b = new StarDto();
    b.setName("a");

    Page<StarDto> page = new PageImpl<>(List.of(a, b), pageable, 128L);

    // When
    when(starService.getStarList(any())).thenReturn(page);

    // Then
    mockMvc
        .perform(get("/stars"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        // Make sure a self relation is exposed
        .andExpect(jsonPath("$._links.self.href").exists())
        // ... and a prev action (because we're at page 2)
        .andExpect(jsonPath("$._links.prev.href").exists())
        // ... and a next action (because total elements = 128)
        .andExpect(jsonPath("$._links.next.href").exists())
        // ... and we have resources.
        .andExpect(jsonPath("$._embedded.stars").exists());
  }

  @Test
  public void givenNegativePageNumber_whenListingStars_thenExpect302() throws Exception {
    // Given
    String pageNumber = "-1";

    // When
    // Then
    mockMvc.perform(get("/stars?page=" + pageNumber)).andExpect(status().isFound());
  }

  @Test
  public void givenPageNumberGreaterThanTotalPages_whenListingStars_thenExpect302()
      throws Exception {
    // Given
    String pageNumber = "64";
    Pageable pageable = PageRequest.of(2, 32);
    Page<StarDto> page = new PageImpl<>(List.of(), pageable, 64L);

    // When
    when(starService.getStarList(any())).thenReturn(page);

    mockMvc.perform(get("/stars?page=" + pageNumber)).andExpect(status().isFound());
  }

  @Test
  public void givenExoplanetsThatOrbitStar_whenListingExoplanetsThatOrbitStar_thenExpect200()
      throws Exception {
    // Given
    ExoplanetDto a = new ExoplanetDto();
    a.setName("Aridia");

    List<ExoplanetDto> exoplanets = List.of(a);

    String starName = "Test";

    // When
    when(exoplanetService.findExoplanetsThatOrbitStar(eq(starName))).thenReturn(exoplanets);

    // Then
    mockMvc
        .perform(get("/stars/{name}/planets", starName))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._embedded.exoplanets").exists())
        .andExpect(jsonPath("$._links.self.href").exists())
        .andExpect(jsonPath("$._links.star.href").exists());
  }
}
