package com.kemzeb.starviewer.star.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import com.kemzeb.starviewer.exception.PageNumberOutOfBoundsException;
import com.kemzeb.starviewer.exoplanet.dto.ExoplanetDto;
import com.kemzeb.starviewer.exoplanet.service.ExoplanetService;
import com.kemzeb.starviewer.star.dto.StarDto;
import com.kemzeb.starviewer.star.service.StarService;
import com.kemzeb.starviewer.util.Constants;
import com.kemzeb.starviewer.util.PagedModelAssembler;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/stars")
@RequiredArgsConstructor
public class StarRestController {

  private final StarService starService;
  private final ExoplanetService exoplanetService;
  private final PagedModelAssembler<StarDto> pagedModelAssembler;

  @GetMapping(produces = "application/hal+json")
  public PagedModel<StarDto> listStars(
      @RequestParam(name = "page") Optional<Integer> maybePageNumber) {

    Integer pageNumber = maybePageNumber.orElse(0);

    if (pageNumber < 0) {
      Integer firstPageNumber = 0;
      String newUrl =
          fromMethodCall(on(getClass()).listStars(Optional.of(firstPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Page<StarDto> page =
        starService.getStarList(PageRequest.of(pageNumber, Constants.DEFAULT_PAGE_SIZE));

    if (page.getNumber() >= page.getTotalPages()) {
      Integer lastPageNumber = page.getTotalPages() - 1;
      String newUrl =
          fromMethodCall(on(getClass()).listStars(Optional.of(lastPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Link selfLink =
        linkTo(methodOn(getClass()).listStars(Optional.empty())).withRel(IanaLinkRelations.SELF);

    return pagedModelAssembler.toModel(page, selfLink);
  }

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public StarDto findStar(@PathVariable("name") String encodedName) {
    String decodedName = URLDecoder.decode(encodedName, Charset.defaultCharset());
    return starService.findStar(decodedName);
  }

  @GetMapping(path = "/{name}/planets")
  public CollectionModel<ExoplanetDto> findExoplanetsThatOrbitStar(
      @PathVariable("name") String encodedName) {

    String decodedName = URLDecoder.decode(encodedName, Charset.defaultCharset());
    Link selfLink =
        linkTo(methodOn(getClass()).findExoplanetsThatOrbitStar(encodedName))
            .withRel(IanaLinkRelations.SELF);

    return CollectionModel.of(exoplanetService.findExoplanetsThatOrbitStar(decodedName), selfLink);
  }
}
