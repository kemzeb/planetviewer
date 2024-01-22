package com.kemzeb.starviewer.star.entity;

import com.kemzeb.starviewer.system.entity.StarSystem;
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
  @JoinColumn(name = "star_system", nullable = false)
  private StarSystem starSystem;

  /** The stellar classification. */
  private String spectralType;
}
