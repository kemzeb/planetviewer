package com.kemzeb.starviewer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
public class PlanetarySystem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private Integer numStars;
  private Integer numPlanets;
  private Integer numMoons;

  /** Distance to the system in units of parsecs. */
  private Double distanceParsecs;

  /** Galactic latitude of the system in units of decimal degrees. */
  private Double galacticLatitudeDegrees;

  /** Galactic longitude of the system in units of decimal degrees. */
  private Double galacticLongitudeDegrees;
}
