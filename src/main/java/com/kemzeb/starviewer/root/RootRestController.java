package com.kemzeb.starviewer.root;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.starviewer.exoplanet.controller.ExoplanetRestController;
import com.kemzeb.starviewer.star.controller.StarRestController;
import java.util.Optional;
import org.springframework.hateoas.IanaLinkRelations;
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
        linkTo(methodOn(RootRestController.class).listAllLinks()).withRel(IanaLinkRelations.SELF));

    return dto;
  }
}
