package com.kemzeb.planetviewer.star.mapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.planetviewer.search.dto.CelestialBodySearchHit;
import com.kemzeb.planetviewer.star.controller.StarRestController;
import com.kemzeb.planetviewer.star.dto.StarDto;
import com.kemzeb.planetviewer.star.entity.Star;
import com.kemzeb.planetviewer.star.entity.StarDocument;
import com.kemzeb.planetviewer.system.mapper.PlanetarySystemMapper;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = PlanetarySystemMapper.class)
public abstract class StarMapper {

  public abstract StarDto toStarDto(Star star);

  @Mapping(source = "planetarySystem.numStars", target = "sysNumStars")
  @Mapping(source = "planetarySystem.numPlanets", target = "sysNumPlanets")
  @Mapping(source = "planetarySystem.numMoons", target = "sysNumMoons")
  public abstract StarDocument toStarDocument(Star star);

  @Mapping(source = "score", target = "hitScore")
  @Mapping(source = "content.name", target = "name")
  public abstract CelestialBodySearchHit toCelestialBodySearchHit(
      SearchHit<StarDocument> searchHit);

  @AfterMapping
  void addLinks(@MappingTarget StarDto starDto) {
    addLinks(starDto, starDto.getName());
  }

  @AfterMapping
  void addLinks(@MappingTarget CelestialBodySearchHit celestialBodySearchHit) {
    addLinks(celestialBodySearchHit, celestialBodySearchHit.getName());
  }

  private void addLinks(RepresentationModel<?> model, String name) {
    model.add(createSelfLink(name));
    model.add(createPlanetsLink(name));
  }

  private Link createSelfLink(String exoplanetName) {
    String encodedName = URLEncoder.encode(exoplanetName, Charset.defaultCharset());
    return linkTo(StarRestController.class).slash(encodedName).withSelfRel();
  }

  private Link createPlanetsLink(String exoplanetName) {
    String encodedName = URLEncoder.encode(exoplanetName, Charset.defaultCharset());
    return linkTo(methodOn(StarRestController.class).findExoplanetsThatOrbitStar(encodedName))
        .withRel("planets");
  }
}
