package com.kemzeb.starviewer.star.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "stars")
@Getter
@Setter
@NoArgsConstructor
public class StarDto extends RepresentationModel<StarDto> {

  @NotBlank private String name;
  @NotNull private Double distanceParsecs;
  private String spectralType;
}
