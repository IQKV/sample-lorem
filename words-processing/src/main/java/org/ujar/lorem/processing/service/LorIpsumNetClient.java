package org.ujar.lorem.processing.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.lorem.processing.config.NetClientProperties;
import org.ujar.lorem.processing.enums.LengthType;
import org.ujar.lorem.processing.exception.NetClientCommunicationException;
import org.ujar.lorem.processing.exception.NetClientMisconfigurationException;

@Service
@Slf4j
public class LorIpsumNetClient {
  private final NetClientProperties properties;
  private final HttpClient httpClient;

  public LorIpsumNetClient(NetClientProperties properties) {
    this.properties = properties;
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(properties.getConnectTimeout()))
        .build();
  }

  public String getText(Integer paragraphsNum, LengthType lengthType) {
    URI uri;
    try {
      uri = new URI(properties.getApiServerUrlTemplate()
          .replace("{paragraphsNum}", paragraphsNum.toString())
          .replace("{lengthType}", lengthType.getType()));
    } catch (URISyntaxException e) {
      throw new NetClientMisconfigurationException(e);
    }
    final var request = HttpRequest.newBuilder()
        .uri(uri)
        .headers("Content-Type", "text/plain; charset=utf-8")
        .timeout(Duration.ofSeconds(properties.getRequestTimeout()))
        .GET()
        .build();

    log.info("{} {}", properties, uri);

    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    } catch (IOException e) {
      throw new NetClientCommunicationException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new NetClientCommunicationException(e);
    }
  }
}
