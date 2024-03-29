package com.kemzeb.planetviewer.root.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.planetviewer.exoplanet.controller.ExoplanetRestController;
import com.kemzeb.planetviewer.root.dto.RootDto;
import com.kemzeb.planetviewer.search.controller.SearchRestController;
import com.kemzeb.planetviewer.star.controller.StarRestController;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootRestController {

  @GetMapping("/")
  public RootDto listAllLinks() {
    RootDto dto = new RootDto();

    dto.add(
        linkTo(methodOn(StarRestController.class).listStars(Optional.empty())).withRel("stars"));
    dto.add(
        linkTo(methodOn(ExoplanetRestController.class).listExoplanets(Optional.empty()))
            .withRel("exoplanets"));
    dto.add(
        linkTo(
                methodOn(SearchRestController.class)
                    .search("", Optional.empty(), Optional.empty(), Optional.empty(), null))
            .withRel("search"));
    dto.add(linkTo(methodOn(RootRestController.class).listAllLinks()).withSelfRel());

    return dto;
  }
}
