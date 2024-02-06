package com.kemzeb.planetviewer.exoplanet.mapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kemzeb.planetviewer.exoplanet.controller.ExoplanetRestController;
import com.kemzeb.planetviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.planetviewer.exoplanet.entity.Exoplanet;
import com.kemzeb.planetviewer.exoplanet.entity.ExoplanetDocument;
import com.kemzeb.planetviewer.search.dto.CelestialBodySearchHit;
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
  @Mapping(source = "content.name", target = "name")
  public abstract CelestialBodySearchHit toCelestialBodySearchHit(
      SearchHit<ExoplanetDocument> searchHit);

  @AfterMapping
  void addSelfLinkRelation(@MappingTarget ExoplanetDto exoplanetDto) {
    exoplanetDto.add(createSelfLink(exoplanetDto.getName()));
  }

  @AfterMapping
  void addSelfLinkRelation(@MappingTarget CelestialBodySearchHit stellarObjectSearchHit) {
    stellarObjectSearchHit.add(createSelfLink(stellarObjectSearchHit.getName()));
  }

  private Link createSelfLink(String exoplanetName) {
    String encodedName = URLEncoder.encode(exoplanetName, Charset.defaultCharset());
    return linkTo(ExoplanetRestController.class).slash(encodedName).withSelfRel();
  }
}
