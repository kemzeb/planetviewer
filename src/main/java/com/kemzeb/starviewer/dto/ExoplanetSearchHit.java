package com.kemzeb.starviewer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "exoplanets")
@Getter
@Setter
@NoArgsConstructor
public class ExoplanetSearchHit extends RepresentationModel<ExoplanetSearchHit> {

  private float hitScore;
  private String name;
  private String stellarHost;
  private String discoveryMethod;
  private Integer discoveryYear;
  private String discoveryFacility;
  private Integer sysNumStars;
  private Integer sysNumPlanets;
  private Integer sysNumMoons;
}
