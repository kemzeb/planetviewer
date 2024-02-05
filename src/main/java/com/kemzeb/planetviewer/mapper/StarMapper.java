package com.kemzeb.planetviewer.mapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.planetviewer.controller.StarRestController;
import com.kemzeb.planetviewer.document.StarDocument;
import com.kemzeb.planetviewer.dto.CelestialBodySearchHit;
import com.kemzeb.planetviewer.dto.StarDto;
import com.kemzeb.planetviewer.entity.Star;
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
