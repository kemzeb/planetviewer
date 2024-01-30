package com.kemzeb.planetviewer.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import com.kemzeb.planetviewer.dto.ExoplanetDto;
import com.kemzeb.planetviewer.dto.ExoplanetSearchHit;
import com.kemzeb.planetviewer.exception.PageNumberOutOfBoundsException;
import com.kemzeb.planetviewer.service.ExoplanetService;
import com.kemzeb.planetviewer.util.Constants;
import com.kemzeb.planetviewer.util.PagedModelAssembler;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  private final PagedModelAssembler<ExoplanetDto> enityPagedModelAssembler;
  private final PagedModelAssembler<ExoplanetSearchHit> searchHitPagedModelAssembler;

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

    Link selfLink = linkTo(methodOn(getClass()).listExoplanets(Optional.empty())).withSelfRel();

    return enityPagedModelAssembler.toModel(page, selfLink);
  }

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public ExoplanetDto findExoplanet(@PathVariable("name") String encodedName) {
    String decodedName = URLDecoder.decode(encodedName, Charset.defaultCharset());
    return exoplanetService.findExoplanet(decodedName);
  }

  // TODO: Support "q" query parameter parsing.

  @GetMapping("/search")
  public PagedModel<ExoplanetSearchHit> search(
      @RequestParam(name = "q", required = true) String query,
      @RequestParam("page") Optional<Integer> maybePageNumber) {

    int pageNumber = maybePageNumber.orElse(0);

    if (pageNumber < 0) {
      Integer firstPageNumber = 0;
      String newUrl =
          fromMethodCall(on(getClass()).search(query, Optional.of(firstPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Page<ExoplanetSearchHit> page =
        exoplanetService.searchWith(PageRequest.of(pageNumber, Constants.DEFAULT_PAGE_SIZE), query);

    if (page.getNumber() >= page.getTotalPages()) {
      Integer lastPageNumber = page.getTotalPages() - 1;
      String newUrl =
          fromMethodCall(on(getClass()).search(query, Optional.of(lastPageNumber)))
              .buildAndExpand()
              .toUriString();
      throw new PageNumberOutOfBoundsException(newUrl);
    }

    Link selfLink = linkTo(methodOn(getClass()).search(query, Optional.empty())).withSelfRel();

    return searchHitPagedModelAssembler.toModel(page, selfLink);
  }
}
