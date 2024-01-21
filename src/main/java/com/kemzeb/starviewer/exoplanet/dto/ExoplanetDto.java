package com.kemzeb.starviewer.exoplanet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class ExoplanetDto extends RepresentationModel<ExoplanetDto> {

  private String name;
  private String hostName;
  private String discoveryMethod;
  private String discoveryYear;
  private String discoveryFacility;
  private String orbitalPeriodDays;
  private String earthRadius;
  private String earthMass;
  private String equilibriumTemperatureKelvin;
  private Double sysDistanceParsecs;
  private Integer sysNumStars;
  private Integer sysNumPlanets;
  private Integer sysNumMoons;
}
