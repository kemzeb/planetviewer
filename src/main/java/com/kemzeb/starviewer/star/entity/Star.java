package com.kemzeb.starviewer.star.entity;

import com.kemzeb.starviewer.system.entity.PlanetarySystem;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Star {

  /** Common stellar name. */
  @Id private String name;

  @ManyToOne
  @JoinColumn(name = "planetary_system", nullable = false)
  private PlanetarySystem planetarySystem;

  /** The stellar classification. */
  private String spectralType;

  /**
   * The temperature of the star (measured in Kelvin) "as modeled by a black body emitting the same
   * total amount of electromagnetic radiation".
   */
  private Double effectiveTemperatureKelvin;

  /** The radius of the star, measured in units of radius of the Sun. */
  private Double solarRadius;

  /** The amount of matter contained in the star, measured in units of solar masses. */
  private Double solarMass;

  /** The age of the star (measured in gigayears i.e. 1 Gyr = 1 billion years) */
  private Double ageGyr;
}
