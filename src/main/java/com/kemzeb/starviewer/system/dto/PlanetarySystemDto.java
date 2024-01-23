package com.kemzeb.starviewer.system.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object for the PlanetarySystem domain object.
 *
 * @see com.kemzeb.starviewer.system.entity.PlanetarySystem
 */
@Getter
@Setter
@NoArgsConstructor
public class PlanetarySystemDto {

  private Integer numStars;
  private Integer numPlanets;
  private Integer numMoons;
  private Double distanceParsecs;
  private Double galacticLatitudeDegrees;
  private Double galacticLongitudeDegrees;
}
