package com.kemzeb.planetviewer.star.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kemzeb.planetviewer.star.dto.StarDto;
import com.kemzeb.planetviewer.star.entity.Star;
import com.kemzeb.planetviewer.system.mapper.PlanetarySystemMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StarMapperUnitTest {

  @InjectMocks StarMapper underTest = Mappers.getMapper(StarMapper.class);
  @Mock PlanetarySystemMapper planetarySystemMapper;

  @Test
  public void givenNormalSource_whenMappingStarToStarDto_ThenReturnIt() {
    // Given
    Star star = new Star();
    star.setName("test");
    star.setSpectralType("G3V");

    // When
    StarDto dto = underTest.toStarDto(star);

    // Then
    assertEquals(star.getName(), dto.getName());
    assertEquals(star.getSpectralType(), dto.getSpectralType());
  }
}
