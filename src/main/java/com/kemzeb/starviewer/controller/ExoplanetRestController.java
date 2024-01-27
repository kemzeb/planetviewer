package com.kemzeb.starviewer.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import com.kemzeb.starviewer.dto.ExoplanetDto;
import com.kemzeb.starviewer.exception.PageNumberOutOfBoundsException;
import com.kemzeb.starviewer.service.ExoplanetService;
import com.kemzeb.starviewer.util.Constants;
import com.kemzeb.starviewer.util.PagedModelAssembler;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/planets")
@RequiredArgsConstructor
public class ExoplanetRestController {

  private final ExoplanetService exoplanetService;
  private final PagedModelAssembler<ExoplanetDto> pagedModelAssembler;

  @GetMapping(produces = "application/hal+json")
  public PagedModel<ExoplanetDto> listExoplanets(
      @RequestParam(name = "page") Optional<Integer> maybePageNumber) {

    int pageNumber = maybePageNumber.orElse(0);

    if (pageNumber < 0) {
      Integer firstPageNumber = 0;
      String newUrl =
          fromMethodCall(on(getClass()).listExoplanets(Optional.of(firstPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Page<ExoplanetDto> page =
        exoplanetService.getExoplanetList(PageRequest.of(pageNumber, Constants.DEFAULT_PAGE_SIZE));

    if (page.getNumber() >= page.getTotalPages()) {
      Integer lastPageNumber = page.getTotalPages() - 1;
      String newUrl =
          fromMethodCall(on(getClass()).listExoplanets(Optional.of(lastPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Link selfLink =
        linkTo(methodOn(getClass()).listExoplanets(Optional.empty()))
            .withRel(IanaLinkRelations.SELF);

    return pagedModelAssembler.toModel(page, selfLink);
  }

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public ExoplanetDto findExoplanet(@PathVariable("name") String encodedName) {
    String decodedName = URLDecoder.decode(encodedName, Charset.defaultCharset());
    return exoplanetService.findExoplanet(decodedName);
  }
}
