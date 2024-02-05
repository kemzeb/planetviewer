package com.kemzeb.planetviewer.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "exoplanet")
@Data
public class ExoplanetDocument {

  @Id private String name;

  @Field(type = FieldType.Text)
  private String stellarHost;

  @Field(type = FieldType.Text)
  private String discoveryMethod;

  @Field(type = FieldType.Integer)
  private Integer discoveryYear;

  @Field(type = FieldType.Text)
  private String discoveryFacility;

  @Field(type = FieldType.Integer)
  private Integer sysNumStars;

  @Field(type = FieldType.Integer)
  private Integer sysNumPlanets;

  @Field(type = FieldType.Integer)
  private Integer sysNumMoons;
}
