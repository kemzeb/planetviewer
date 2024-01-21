package com.kemzeb.starviewer.star.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Star {

  /** Common stellar name. */
  @Id private String name;

  /** Distance from Earth to the system measured in parsecs. */
  @NotNull private Double distanceParsecs;

  /** The stellar classification. */
  private String spectralType;
}
