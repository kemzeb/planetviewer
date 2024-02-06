package com.kemzeb.planetviewer.exoplanet.repo;

import com.kemzeb.planetviewer.exoplanet.entity.ExoplanetDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExoplanetDocumentRepository
    extends ElasticsearchRepository<ExoplanetDocument, String> {}
