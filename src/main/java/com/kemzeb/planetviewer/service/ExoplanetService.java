package com.kemzeb.planetviewer.service;

import com.kemzeb.planetviewer.document.ExoplanetDocument;
import com.kemzeb.planetviewer.dto.ExoplanetDto;
import com.kemzeb.planetviewer.dto.ExoplanetSearchHit;
import com.kemzeb.planetviewer.entity.Exoplanet;
import com.kemzeb.planetviewer.entity.Star;
import com.kemzeb.planetviewer.exception.ExoplanetNotFoundException;
import com.kemzeb.planetviewer.exception.StarNotFoundException;
import com.kemzeb.planetviewer.mapper.ExoplanetMapper;
import com.kemzeb.planetviewer.repository.ExoplanetRepository;
import com.kemzeb.planetviewer.repository.StarRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExoplanetService {

  private final ExoplanetMapper exoplanetMapper;
  private final ExoplanetRepository exoplanetRepository;
  private final StarRepository starRepository;

  private final ElasticsearchOperations elasticsearchOperations;

  public Page<ExoplanetDto> getExoplanetList(Pageable pageable) {
    Page<Exoplanet> exoplanets = exoplanetRepository.findAll(pageable);
    return exoplanets.map(exoplanetMapper::toExoplanetDto);
  }

  public ExoplanetDto findExoplanet(String name) {
    Exoplanet exoplanet =
        exoplanetRepository.findById(name).orElseThrow(() -> new ExoplanetNotFoundException(name));
    return exoplanetMapper.toExoplanetDto(exoplanet);
  }

  public List<ExoplanetDto> findExoplanetsThatOrbitStar(String name) {
    Star star = starRepository.findById(name).orElseThrow(() -> new StarNotFoundException(name));
    List<Exoplanet> exoplanets = exoplanetRepository.findAllByStellarHost(star);
    return exoplanets.stream().map(exoplanetMapper::toExoplanetDto).toList();
  }

  public Page<ExoplanetSearchHit> searchWith(Pageable pageable, String name) {
    NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();

    if (name.isBlank()) {
      return Page.empty(pageable);
    }

    // By default, let's match just the name.
    nativeQueryBuilder.withQuery(q -> q.match(m -> m.field("name").query(name)));

    nativeQueryBuilder.withPageable(pageable);

    Query query = nativeQueryBuilder.build();
    SearchHits<ExoplanetDocument> searchHits =
        elasticsearchOperations.search(
            query, ExoplanetDocument.class, IndexCoordinates.of("exoplanets"));

    SearchPage<ExoplanetDocument> searchPage =
        SearchHitSupport.searchPageFor(searchHits, query.getPageable());

    return searchPage.map(exoplanetMapper::toExoplanetSearchHit);
  }
}
