package com.kemzeb.planetviewer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.kemzeb.planetviewer.dto.ExoplanetDto;
import com.kemzeb.planetviewer.entity.Exoplanet;
import com.kemzeb.planetviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.planetviewer.mapper.ExoplanetMapper;
import com.kemzeb.planetviewer.repository.ExoplanetRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExoplanetServiceUnitTest {

  @InjectMocks private ExoplanetService underTest;
  @Mock private ExoplanetMapper exoplanetMapper;
  @Mock private ExoplanetRepository starRepository;

  @Test
  public void givenNonExistentExoplanet_whenFindingExoplanet_throwException() {
    // Given
    String name = "the exoplanet with no name";

    // When
    when(starRepository.findById(name)).thenReturn(Optional.empty());

    // Then
    assertThrows(ExoplanetNotFoundException.class, () -> underTest.findExoplanet(name));
  }

  @Test
  public void givenExistingExoplanet_whenFindingExoplanet_throwReturnIt() {
    // Given
    String name = "Eudora";
    Exoplanet exoplanet = new Exoplanet();

    // When
    when(starRepository.findById(name)).thenReturn(Optional.of(exoplanet));
    when(exoplanetMapper.toExoplanetDto(exoplanet)).thenReturn(new ExoplanetDto());

    ExoplanetDto dto = underTest.findExoplanet(name);

    // Then
    assertEquals(exoplanet.getName(), dto.getName());
  }
}
