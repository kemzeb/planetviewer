package com.kemzeb.planetviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

// TODO: Determine security configurations based on Spring Data ElasticSearch docs.

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

  @Value("${planetviewer.elasticsearch.host}")
  private String host;

  @Value("${planetviewer.elasticsearch.port}")
  private String port;

  /**
   * This sets up the "Imperative Rest Client" configuration mentioned in the docs.
   *
   * @see <a
   *     href=https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/clients.html>Docs
   *     Reference</a>
   */
  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder().connectedTo(host + ":" + port).build();
  }
}
