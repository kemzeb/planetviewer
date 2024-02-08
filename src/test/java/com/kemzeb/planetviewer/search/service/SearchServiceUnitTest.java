package com.kemzeb.planetviewer.search.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kemzeb.planetviewer.exoplanet.mapper.ExoplanetMapper;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchOperations;

@ExtendWith(MockitoExtension.class)
public class SearchServiceUnitTest {

  @InjectMocks private SearchService underTest;
  @Mock private ExoplanetMapper exoplanetMapper;
  @Mock private SearchOperations searchOperations;

  @Test
  public void
      givenPageableThatComputesFormParamBeyondMaxResultWindow_whenSearching_thenThrowException() {
    // Given
    // Should be higher than whatever we set max_result_window when configuring ElasticSearch.
    Pageable pageable = PageRequest.of(400, 32);

    // When
    // Then
    assertThrows(
        ValidationException.class,
        () -> underTest.search(pageable, "test", List.of(), Optional.empty()));
  }

  @Test
  public void givenInvalidType_whenSearching_thenThrowException() {
    // Given
    // Should be higher than whatever we set max_result_window when configuring ElasticSearch.
    Pageable pageable = PageRequest.of(0, 32);

    // When
    // Then
    assertThrows(
        ValidationException.class,
        () -> underTest.search(pageable, "test", List.of(), Optional.of("invalid")));
  }
}
