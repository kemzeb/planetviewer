package com.kemzeb.starviewer.star;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kemzeb.starviewer.star.dto.StarDto;
import com.kemzeb.starviewer.star.dto.StarMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class StarMapperUnitTest {

  @Test
  public void givenNormalSource_whenMappingStarToStarDto_ThenReturnIt() {
    // Given
    Star star = new Star();
    star.setName("test");
    star.setDistanceParsecs(10.12);
    star.setSpectralType("G3V");

    // When
    StarMapper underTest = Mappers.getMapper(StarMapper.class);
    StarDto dto = underTest.toStarDto(star);

    // Then
    assertEquals(star.getName(), dto.getName());
    assertEquals(star.getDistanceParsecs(), dto.getDistanceParsecs());
    assertEquals(star.getSpectralType(), dto.getSpectralType());
  }
}
