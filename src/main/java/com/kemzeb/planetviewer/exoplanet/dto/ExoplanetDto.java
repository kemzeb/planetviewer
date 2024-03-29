package com.kemzeb.planetviewer.exoplanet.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "exoplanets")
@Getter
@Setter
@NoArgsConstructor
public class ExoplanetDto extends RepresentationModel<ExoplanetDto> {

  private String name;
  private String hostName;
  private String discoveryMethod;
  private Integer discoveryYear;
  private String discoveryFacility;
  private Double orbitalPeriodDays;
  private Double earthRadius;
  private Double earthMass;
  private Double equilibriumTemperatureKelvin;
}
