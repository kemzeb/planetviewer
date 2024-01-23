package com.kemzeb.starviewer.star.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.starviewer.star.controller.StarRestController;
import com.kemzeb.starviewer.star.entity.Star;
import com.kemzeb.starviewer.system.dto.PlanetarySystemMapper;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING, uses = PlanetarySystemMapper.class)
public interface StarMapper {

  @AfterMapping
  default void addSelfLinkRelation(@MappingTarget StarDto starsDto) {
    String encodedName = URLEncoder.encode(starsDto.getName(), Charset.defaultCharset());

    starsDto.add(linkTo(StarRestController.class).slash(encodedName).withSelfRel());
    starsDto.add(
        linkTo(methodOn(StarRestController.class).findExoplanetsThatOrbitStar(encodedName))
            .withRel("planets"));
  }

  StarDto toStarDto(Star star);
}
