package com.kemzeb.starviewer.star;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.kemzeb.starviewer.util.Constants;
import jakarta.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
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

  @GetMapping(produces = "application/hal+json")
  public PagedModel<StarDto> listStars(
      @RequestParam(name = "page", defaultValue = "0") int pageNumber) {

    Page<StarDto> page =
        starService.getStarList(PageRequest.of(pageNumber, Constants.DEFAULT_PAGE_SIZE));

    PageMetadata metadata =
        new PageMetadata(
            page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());

    return PagedModel.of(page.getContent(), metadata, createPaginatedLinks(page));
  }

  @GetMapping(path = "/{name}", produces = "application/hal+json")
  public StarDto findStar(@PathVariable String name) {
    String decodedName = tryToUrlDecode(name);
    StarDto star = starService.findStar(decodedName);

    return star;
  }

  private String tryToUrlDecode(String encoded) {
    try {
      return URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new ValidationException("Stellar name \"" + encoded + "\" could not be URL-decoded.");
    }
  }

  private List<Link> createPaginatedLinks(Page<StarDto> page) {
    List<Link> links = new ArrayList<>();

    links.add(
        linkTo(methodOn(StarRestController.class).listStars(page.getNumber()))
            .withRel(IanaLinkRelations.SELF));

    if (page.hasPrevious()) {
      Pageable prev = page.previousPageable();
      links.add(
          linkTo(methodOn(StarRestController.class).listStars(prev.getPageNumber()))
              .withRel(IanaLinkRelations.PREV));
    }

    if (page.hasNext()) {
      Pageable next = page.nextPageable();
      links.add(
          linkTo(methodOn(StarRestController.class).listStars(next.getPageNumber()))
              .withRel(IanaLinkRelations.NEXT));
    }

    return links;
  }
}
