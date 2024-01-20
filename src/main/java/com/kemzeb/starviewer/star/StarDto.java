package com.kemzeb.starviewer.star;

import jakarta.validation.constraints.NotBlank;
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
  @NotBlank private String distanceParsecs;
  @NotBlank private String spectralType;
}
