package com.kemzeb.starviewer.exoplanet.entity;

import com.kemzeb.starviewer.star.Star;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * Represents a single exoplanet along with it's host star, system data, and other data. This is
 * essentially how an exoplanet is represented in the "Planetary Systems and Composite Data" table
 * in the Exoplanet Archive (with some missing columns that were deemed not necessary).
 *
 * <p>This table is not normalized mainly because there doesn't seem to be a way to reliably
 * identify a star system given the existing public data, but we still want to use the system data
 * provided by the archive.
 */
@Entity
@Data
public class Exoplanet {

  @Id private String name;

  @ManyToOne
  @JoinColumn(name = "host_name")
  private Star hostName;

  @Column(nullable = false)
  private String discoveryMethod;

  @Column(nullable = false)
  private String discoveryYear;

  @Column(nullable = false)
  private String discoveryFacility;

  private String orbitalPeriodDays;
  private String earthRadius;
  private String earthMass;
  private String equilibriumTemperatureKelvin;

  @Column(nullable = false)
  private Double sysDistanceParsecs;

  @Column(nullable = false)
  private Integer sysNumStars;

  @Column(nullable = false)
  private Integer sysNumPlanets;

  @Column(nullable = false)
  private Integer sysNumMoons;
}
