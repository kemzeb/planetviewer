package com.kemzeb.starviewer.star;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class StarRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private StarRepository starRepository;

  @Test
  public void givenExistingStar_whenFindingStar_thenExpect200() throws Exception {
    // Given
    String name = "HIP 171";

    Star star = new Star();
    star.setName(name);

    // When
    when(starRepository.findById(eq(name))).thenReturn(Optional.of(star));

    // Then
    mockMvc
        .perform(get("/stars/{name}", "HIP%20171").contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._links.self.href").exists());
  }

  @Test
  public void givenExistingStars_whenListingStars_thenExpect200() throws Exception {
    // Given
    Pageable pageable = PageRequest.of(2, 32);

    Star a = new Star();
    a.setName("a");

    Star b = new Star();
    b.setName("a");

    Page<Star> page = new PageImpl<Star>(List.of(a, b), pageable, 128L);

    // When
    when(starRepository.findAll(isA(Pageable.class))).thenReturn(page);

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
}
