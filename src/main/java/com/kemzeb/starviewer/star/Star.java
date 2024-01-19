package com.kemzeb.starviewer.star;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Star {

  /** Common stellar name. */
  @Id private String name;

  /** Distance from Earth to the system measured in parsecs. */
  @NotBlank private String distanceParsecs;

  /** The stellar classification. */
  @NotBlank private String spectralType;
}
