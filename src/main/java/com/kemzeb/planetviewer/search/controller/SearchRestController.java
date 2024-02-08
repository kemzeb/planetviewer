package com.kemzeb.planetviewer.search.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.planetviewer.exception.PageNumberOutOfBoundsException;
import com.kemzeb.planetviewer.search.dto.CelestialBodySearchHit;
import com.kemzeb.planetviewer.search.filter.parser.ParsedFilter;
import com.kemzeb.planetviewer.search.filter.parser.Parser;
import com.kemzeb.planetviewer.search.service.SearchService;
import com.kemzeb.planetviewer.util.Constants;
import com.kemzeb.planetviewer.util.PagedModelAssembler;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class SearchRestController {

  private final SearchService searchService;
  private final PagedModelAssembler<CelestialBodySearchHit> pagedModelAssembler;

  @GetMapping("/search")
  public PagedModel<CelestialBodySearchHit> search(
      @RequestParam("q") String query,
      @RequestParam(name = "filters") Optional<List<String>> maybeFilters,
      @RequestParam("type") Optional<String> maybeType,
      @RequestParam("page") Optional<Integer> maybePage,
      UriComponentsBuilder builder) {

    int pageNumber = maybePage.orElse(0);

    if (pageNumber < 0) {
      Integer firstPageNumber = 0;
      String redirectUrl = buildRedirectUrl(builder, query, maybeType, firstPageNumber);
      throw new PageNumberOutOfBoundsException(redirectUrl);
    }

    List<ParsedFilter> parsedFilters =
        maybeFilters.orElse(List.of()).stream().map(filter -> new Parser(filter).parse()).toList();

    Pageable pageable = PageRequest.of(pageNumber, Constants.DEFAULT_PAGE_SIZE);
    Page<CelestialBodySearchHit> page =
        searchService.search(pageable, query, parsedFilters, maybeType);

    if (page.getTotalPages() > 0 && (page.getNumber() >= page.getTotalPages())) {
      Integer lastPageNumber = page.getTotalPages() - 1;
      String redirectUrl = buildRedirectUrl(builder, query, maybeType, lastPageNumber);
      throw new PageNumberOutOfBoundsException(redirectUrl);
    }

    Link selfLink =
        linkTo(methodOn(getClass()).search(query, maybeFilters, maybeType, Optional.empty(), null))
            .withSelfRel();

    return pagedModelAssembler.toModel(page, selfLink);
  }

  private String buildRedirectUrl(
      UriComponentsBuilder builder, String query, Optional<String> maybeType, Integer pageNumber) {
    return builder
        .path("/search")
        .queryParam("q", query)
        .queryParamIfPresent("type", maybeType)
        .queryParam("page", pageNumber)
        .build()
        .toUriString();
  }
}
