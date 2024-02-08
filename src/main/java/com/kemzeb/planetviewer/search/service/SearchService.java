package com.kemzeb.planetviewer.search.service;

import com.kemzeb.planetviewer.exoplanet.entity.ExoplanetDocument;
import com.kemzeb.planetviewer.exoplanet.mapper.ExoplanetMapper;
import com.kemzeb.planetviewer.search.dto.CelestialBodySearchHit;
import com.kemzeb.planetviewer.search.filter.exoplanet.CriteriaBuilderStrategy;
import com.kemzeb.planetviewer.search.filter.exoplanet.ExoplanetCriteriaBuilderStrategy;
import com.kemzeb.planetviewer.search.filter.exoplanet.StarCriteriaBuilderStrategy;
import com.kemzeb.planetviewer.search.filter.parser.ParsedFilter;
import com.kemzeb.planetviewer.star.entity.StarDocument;
import com.kemzeb.planetviewer.star.mapper.StarMapper;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private static final String TYPE_EXOPLANET = "exoplanet";
  private static final String TYPE_STAR = "star";
  private static final int MAX_RESULT_WINDOW = 10_000;
  private final SearchOperations searchOperations;
  private final ExoplanetMapper exoplanetMapper;
  private final StarMapper starMapper;

  /**
   * Returns a paginated search result given a query.
   *
   * @throws jakarta.validation.ValidationException thrown if not given a valid type or the pageable
   *     exceeds ElasticSearch's max result window limit.
   */
  public Page<CelestialBodySearchHit> search(
      Pageable pageable, String keyword, List<ParsedFilter> filters, Optional<String> maybeType) {

    throwIfPageableExceedsMaxResultWindow(pageable);

    Page<CelestialBodySearchHit> page = Page.empty();

    if (maybeType.isEmpty()) {
      page = searchDefault(pageable, keyword);
    } else if (maybeType.get().equals(TYPE_EXOPLANET)) {
      page = searchForExoplanets(pageable, keyword, filters);
    } else if (maybeType.get().equals(TYPE_STAR)) {
      page = searchForStars(pageable, keyword, filters);
    } else {
      throw new ValidationException(
          String.format("\"%s\" is not a valid celestial type.", maybeType.get()));
    }

    return page;
  }

  /**
   * Performs the default search behavior. Currently this means we just perform a search query on
   * {@link ExoplanetDocument}'s index.
   */
  private Page<CelestialBodySearchHit> searchDefault(Pageable pageable, String keyword) {
    return searchForExoplanets(pageable, keyword, List.of());
  }

  private Page<CelestialBodySearchHit> searchForExoplanets(
      Pageable pageable, String keyword, List<ParsedFilter> filters) {

    CriteriaBuilderStrategy builder = new ExoplanetCriteriaBuilderStrategy();
    Criteria criteria = builder.buildCriteria(keyword, filters);
    Query query = new CriteriaQuery(criteria).setPageable(pageable);

    SearchHits<ExoplanetDocument> searchHits =
        searchOperations.search(query, ExoplanetDocument.class);

    SearchPage<ExoplanetDocument> searchPage =
        SearchHitSupport.searchPageFor(searchHits, query.getPageable());

    return searchPage.map(exoplanetMapper::toCelestialBodySearchHit);
  }

  private Page<CelestialBodySearchHit> searchForStars(
      Pageable pageable, String keyword, List<ParsedFilter> filters) {

    CriteriaBuilderStrategy builder = new StarCriteriaBuilderStrategy();
    Criteria criteria = builder.buildCriteria(keyword, filters);
    Query query = new CriteriaQuery(criteria).setPageable(pageable);

    SearchHits<StarDocument> searchHits = searchOperations.search(query, StarDocument.class);

    SearchPage<StarDocument> searchPage =
        SearchHitSupport.searchPageFor(searchHits, query.getPageable());

    return searchPage.map(starMapper::toCelestialBodySearchHit);
  }

  /**
   * Throws {@link jakarta.validation.ValidationException} if the pageable produces a {@code from}
   * ElasticSearch parameter value that is greater than the max.
   *
   * <p>Since Spring Data ES doesn't create a special exception for this if we were to submit a
   * query, we need to handle this ourselves.
   *
   * @throws jakarta.validation.ValidationException thrown if pageable computes a from parameter
   *     that is > MAX_RESULT_WINDOW.
   */
  private void throwIfPageableExceedsMaxResultWindow(Pageable pageable) {
    if (pageable.isUnpaged()) {
      throw new RuntimeException("Pageable should contain pagination information.");
    }

    // "from" is just an offset, so if page num = 2 and page size = 3, then from = 2 * 3 = 6.
    int from = pageable.getPageNumber() * pageable.getPageSize();

    if (from > MAX_RESULT_WINDOW) {
      throw new ValidationException(
          String.format(
              "Page number \"%d\" and page size \"%d\" computes a form"
                  + " parameter that exceeds ElasticSearch max result window.",
              pageable.getPageNumber(), pageable.getPageSize()));
    }
  }
}
