package com.kemzeb.planetviewer.repository;

import com.kemzeb.planetviewer.document.StarDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarDocumentRepository extends ElasticsearchRepository<StarDocument, String> {}
