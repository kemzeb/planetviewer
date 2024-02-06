package com.kemzeb.planetviewer.star.repo;

import com.kemzeb.planetviewer.star.entity.StarDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarDocumentRepository extends ElasticsearchRepository<StarDocument, String> {}
