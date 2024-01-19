package com.kemzeb.starviewer.star;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StarRestController.class)
public class StarRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private StarService starService;

  @Test
  public void givenExistingStar_whenFindingStar_thenExpect200() throws Exception {
    // Given
    String name = "HIP 171";

    StarDto star = new StarDto();
    star.setName(name);

    // When
    when(starService.findStar(eq(name))).thenReturn(star);

    // Then
    mockMvc
        .perform(get("/stars/{name}", "HIP%20171").contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/hal+json"))
        .andExpect(jsonPath("$._links.self.href").exists());
  }
}
