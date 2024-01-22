package com.kemzeb.starviewer.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StarSystem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Double distanceParsecs;
  private Integer numStars;
  private Integer numPlanets;
  private Integer numMoons;
}
