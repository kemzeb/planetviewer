package com.kemzeb.starviewer.star.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kemzeb.starviewer.star.controller.StarRestController;
import com.kemzeb.starviewer.star.entity.Star;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

@Mapper(componentModel = ComponentModel.SPRING)
public interface StarMapper {

  @AfterMapping
  default void addSelfLinkRelation(@MappingTarget StarDto starsDto) {
    String encodedName = URLEncoder.encode(starsDto.getName(), Charset.defaultCharset());
    Link selfLink = linkTo(StarRestController.class).slash(encodedName).withSelfRel();

    starsDto.add(selfLink);
  }

  StarDto toStarDto(Star star);
}
