package com.kemzeb.starviewer.exoplanet.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kemzeb.starviewer.exoplanet.controller.ExoplanetRestController;
import com.kemzeb.starviewer.exoplanet.entity.Exoplanet;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ExoplanetMapper {

  @AfterMapping
  default void addSelfLinkRelation(@MappingTarget ExoplanetDto exoplanetDto) {
    String encodedName = URLEncoder.encode(exoplanetDto.getName(), Charset.defaultCharset());
    Link selfLink = linkTo(ExoplanetRestController.class).slash(encodedName).withSelfRel();

    exoplanetDto.add(selfLink);
  }

  @Mapping(source = "name", target = "hostName")
  ExoplanetDto toExoplanetDto(Exoplanet exoplanet);
}
