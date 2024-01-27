package com.kemzeb.starviewer.entity;

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
  @JoinColumn(name = "stellar_host", nullable = false)
  private Star stellarHost;

  @Column(nullable = false)
  private String discoveryMethod;

  @Column(nullable = false)
  private String discoveryYear;

  @Column(nullable = false)
  private String discoveryFacility;

  private Double orbitalPeriodDays;
  private Double earthRadius;
  private Double earthMass;
  private Double equilibriumTemperatureKelvin;
}
