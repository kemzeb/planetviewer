package com.kemzeb.planetviewer.repository;

import com.kemzeb.planetviewer.entity.ExoplanetDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExoplanetDocumentRepository
    extends ElasticsearchRepository<ExoplanetDocument, String> {}
