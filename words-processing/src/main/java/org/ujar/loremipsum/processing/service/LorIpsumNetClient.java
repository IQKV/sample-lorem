package org.ujar.loremipsum.processing.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.loremipsum.processing.config.NetClientProperties;
import org.ujar.loremipsum.processing.enums.LengthType;
import org.ujar.loremipsum.processing.exception.NetClientCommunicationException;
import org.ujar.loremipsum.processing.exception.NetClientMisconfigurationException;

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
    var request = HttpRequest.newBuilder()
        .uri(uri)
        .headers("Content-Type", "text/plain; charset=utf-8")
        .timeout(Duration.ofSeconds(properties.getRequestTimeout()))
        .GET()
        .build();
    try {
      log.info("{} {}", properties, uri);
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    } catch (IOException | InterruptedException e) {
      throw new NetClientCommunicationException(e);
    }
  }
}
