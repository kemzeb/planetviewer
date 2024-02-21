package com.kemzeb.planetviewer.config.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
@Profile({"dev", "test"})
public class ElasticsearchDevConfig extends ElasticsearchConfiguration {

  @Value("${planetviewer.elasticsearch.host}")
  private String host;

  @Value("${planetviewer.elasticsearch.port}")
  private String port;

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder().connectedTo(host + ":" + port).build();
  }
}
