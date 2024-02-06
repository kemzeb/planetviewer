package com.kemzeb.planetviewer.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "hits")
@Getter
@Setter
@NoArgsConstructor
public class CelestialBodySearchHit extends RepresentationModel<CelestialBodySearchHit> {

  private float hitScore;
  private String name;
}
