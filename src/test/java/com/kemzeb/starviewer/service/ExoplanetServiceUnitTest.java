package com.kemzeb.starviewer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.kemzeb.starviewer.dto.ExoplanetDto;
import com.kemzeb.starviewer.entity.Exoplanet;
import com.kemzeb.starviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.starviewer.mapper.ExoplanetMapper;
import com.kemzeb.starviewer.repository.ExoplanetRepository;
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
