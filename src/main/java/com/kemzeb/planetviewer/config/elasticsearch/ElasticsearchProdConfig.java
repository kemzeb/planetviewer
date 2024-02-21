package com.kemzeb.planetviewer.config.elasticsearch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
@Profile("prod")
public class ElasticsearchProdConfig extends ElasticsearchConfiguration {

  @Value("${planetviewer.elasticsearch.ca-cert-path}")
  private String caCertPath;

  @Value("${planetviewer.elasticsearch.host}")
  private String host;

  @Value("${planetviewer.elasticsearch.port}")
  private String port;

  @Value("${planetviewer.elasticsearch.username}")
  private String username;

  @Value("${planetviewer.elasticsearch.password}")
  private String password;

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder()
        .connectedTo(host + ":" + port)
        .usingSsl(createSslContext())
        .withBasicAuth(username, password)
        .build();
  }

  private SSLContext createSslContext() {
    try {
      InputStream caCertInputStream = new FileInputStream(caCertPath);
      Certificate ca =
          CertificateFactory.getInstance("X.509").generateCertificate(caCertInputStream);

      // Create a custom trust store.
      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

      // Make the trust store empty.
      trustStore.load(null, null);

      // Trust the Elasticsearch CA cert.
      trustStore.setCertificateEntry("esCert", ca);

      TrustManagerFactory trustManagerFactory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

      // Initialize the factory using our custom trust store.
      trustManagerFactory.init(trustStore);

      SSLContext context = SSLContext.getInstance("TLS");

      context.init(null, trustManagerFactory.getTrustManagers(), null);

      return context;
    } catch (KeyManagementException
        | CertificateException
        | KeyStoreException
        | NoSuchAlgorithmException
        | IOException e) {
      throw new RuntimeException("Unable to create SSL context", e);
    }
  }
}
