package com.kemzeb.starviewer.mapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kemzeb.starviewer.controller.ExoplanetRestController;
import com.kemzeb.starviewer.document.ExoplanetDocument;
import com.kemzeb.starviewer.dto.ExoplanetDto;
import com.kemzeb.starviewer.dto.ExoplanetSearchHit;
import com.kemzeb.starviewer.entity.Exoplanet;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.hateoas.Link;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class ExoplanetMapper {

  @Mapping(source = "stellarHost.name", target = "hostName")
  public abstract ExoplanetDto toExoplanetDto(Exoplanet exoplanet);

  @Mapping(source = "stellarHost.name", target = "stellarHost")
  @Mapping(source = "stellarHost.planetarySystem.numStars", target = "sysNumStars")
  @Mapping(source = "stellarHost.planetarySystem.numPlanets", target = "sysNumPlanets")
  @Mapping(source = "stellarHost.planetarySystem.numMoons", target = "sysNumMoons")
  public abstract ExoplanetDocument toExoplanetDocument(Exoplanet exoplanet);

  @Mapping(source = "score", target = "hitScore")
  @Mapping(source = "content.discoveryMethod", target = "discoveryMethod")
  @Mapping(source = "content.discoveryFacility", target = "discoveryFacility")
  @Mapping(source = "content.discoveryYear", target = "discoveryYear")
  @Mapping(source = "content.name", target = "name")
  @Mapping(source = "content.stellarHost", target = "stellarHost")
  @Mapping(source = "content.sysNumStars", target = "sysNumStars")
  @Mapping(source = "content.sysNumPlanets", target = "sysNumPlanets")
  @Mapping(source = "content.sysNumMoons", target = "sysNumMoons")
  public abstract ExoplanetSearchHit toExoplanetSearchHit(SearchHit<ExoplanetDocument> searchHit);

  @AfterMapping
  void addSelfLinkRelation(@MappingTarget ExoplanetDto exoplanetDto) {
    exoplanetDto.add(createSelfLink(exoplanetDto.getName()));
  }

  @AfterMapping
  void addSelfLinkRelation(@MappingTarget ExoplanetSearchHit exoplanetSearchHit) {
    exoplanetSearchHit.add(createSelfLink(exoplanetSearchHit.getName()));
  }

  private Link createSelfLink(String exoplanetName) {
    String encodedName = URLEncoder.encode(exoplanetName, Charset.defaultCharset());
    return linkTo(ExoplanetRestController.class).slash(encodedName).withSelfRel();
  }
}
