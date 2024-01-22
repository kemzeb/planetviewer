package com.kemzeb.starviewer.util;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A custom implementation heavily based on Spring's {@code PagedResourcesAssembler}. Unlike it, it
 * does not expect the RepresentationModel to be of {@code EntityModel<T>} (it rather expects T to
 * already be a RepresentationModel).
 *
 * <p>This is a much simplified version of the class that inspired this one, taking the necessary
 * behavior and/or modifying to fit our needs.
 *
 * @see org.springframework.data.web.PagedResourcesAssembler
 */
@Component
public class PagedModelAssembler<T extends RepresentationModel<?>>
    implements RepresentationModelAssembler<Page<T>, PagedModel<T>> {

  @Override
  public PagedModel<T> toModel(Page<T> page) {
    return createModel(page, Optional.empty());
  }

  public PagedModel<T> toModel(Page<T> page, Link selfLink) {
    return createModel(page, Optional.of(selfLink));
  }

  private PagedModel<T> createModel(Page<T> page, Optional<Link> link) {
    PagedModel<T> representation = PagedModel.of(page.getContent(), asPageMetadata(page));

    return addPaginationLinks(representation, page, link);
  }

  private PagedModel<T> addPaginationLinks(
      PagedModel<T> representation, Page<T> page, Optional<Link> link) {
    UriTemplate baseUri = getUriTemplate(link);

    if (page.hasPrevious()) {
      Pageable prev = page.previousPageable();
      representation.add(createLink(baseUri, prev.getPageNumber(), IanaLinkRelations.PREV));
    }

    if (page.hasNext()) {
      Pageable next = page.nextPageable();
      representation.add(createLink(baseUri, next.getPageNumber(), IanaLinkRelations.NEXT));
    }

    representation.add(createLink(baseUri, page.getNumber(), IanaLinkRelations.SELF));

    return representation;
  }

  private Link createLink(UriTemplate base, int pageNumber, LinkRelation relation) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());

    builder.queryParam("page", pageNumber);

    return Link.of(builder.build().toUriString(), relation);
  }

  private PageMetadata asPageMetadata(Page<?> page) {
    return new PageMetadata(
        page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
  }

  private UriTemplate getUriTemplate(Optional<Link> baseLink) {
    return UriTemplate.of(baseLink.map(Link::getHref).orElseGet(this::currentRequest));
  }

  private String currentRequest() {
    return ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
  }
}
