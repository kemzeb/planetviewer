package com.kemzeb.starviewer.star;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class StarAssembler implements RepresentationModelAssembler<Star, StarDto> {

  @Autowired StarMapper mapper;

  @Override
  public StarDto toModel(Star star) {
    StarDto starDto = mapper.toStarDto(star);
    String encodedName = URLEncoder.encode(starDto.getName(), Charset.defaultCharset());
    Link selfLink = linkTo(StarRestController.class).slash(encodedName).withSelfRel();

    starDto.add(selfLink);

    return starDto;
  }
}
