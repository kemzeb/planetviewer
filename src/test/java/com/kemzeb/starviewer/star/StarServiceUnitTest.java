package com.kemzeb.starviewer.star;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kemzeb.starviewer.exception.StarNotFoundException;
import com.kemzeb.starviewer.star.dto.StarDto;
import com.kemzeb.starviewer.star.dto.StarMapper;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.star.entity.StarRepository;
import com.kemzeb.starviewer.star.service.StarService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StarServiceUnitTest {

  @InjectMocks private StarService underTest;
  @Mock private StarMapper starMapper;
  @Mock private StarRepository starRepository;

  @Test
  public void givenNonExistentStar_whenFindingStar_throwException() {
    // Given
    String name = "the star with no name";

    // When
    when(starRepository.findById(name)).thenReturn(Optional.empty());

    // Then
    assertThrows(StarNotFoundException.class, () -> underTest.findStar(name));
  }

  @Test
  public void givenExistingStar_whenFindingStar_throwReturnIt() {
    // Given
    String name = "CoRoT-1";
    Star star = new Star();

    // When
    when(starRepository.findById(name)).thenReturn(Optional.of(star));
    when(starMapper.toStarDto(any())).thenReturn(new StarDto());

    StarDto dto = underTest.findStar(name);

    // Then
    assertEquals(star.getName(), dto.getName());
  }
}
