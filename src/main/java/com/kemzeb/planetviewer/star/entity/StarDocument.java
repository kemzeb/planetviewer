package com.kemzeb.planetviewer.star.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents a document in the "star" index.
 *
 * @see com.kemzeb.planetviewer.star.entity.Star
 */
@Document(indexName = "star")
@Data
public class StarDocument {

  @Id private String name;

  @Field(type = FieldType.Text)
  private String spectralType;

  @Field(type = FieldType.Double)
  private Double effectiveTemperatureKelvin;

  @Field(type = FieldType.Double)
  private Double solarRadius;

  @Field(type = FieldType.Double)
  private Double solarMass;

  @Field(type = FieldType.Double)
  private Double ageGyr;

  @Field(type = FieldType.Integer)
  private Integer sysNumStars;

  @Field(type = FieldType.Integer)
  private Integer sysNumPlanets;

  @Field(type = FieldType.Integer)
  private Integer sysNumMoons;
}
