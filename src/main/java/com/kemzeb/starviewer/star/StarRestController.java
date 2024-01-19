package com.kemzeb.starviewer.star;

import jakarta.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/stars", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StarRestController {

  private final StarService starService;

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public StarDto findStar(@PathVariable String name) {
    String decodedName = tryToUrlDecode(name);
    StarDto star = starService.findStar(decodedName);

    Link selfLink = WebMvcLinkBuilder.linkTo(StarRestController.class).slash(name).withSelfRel();
    star.add(selfLink);

    return star;
  }

  private String tryToUrlDecode(String encoded) {
    try {
      return URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new ValidationException("Stellar name \"" + encoded + "\" could not be URL-decoded.");
    }
  }
}
