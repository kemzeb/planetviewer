package com.kemzeb.starviewer.repository;

import com.kemzeb.starviewer.document.ExoplanetDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExoplanetDocumentRepository
    extends ElasticsearchRepository<ExoplanetDocument, String> {}
