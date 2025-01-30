/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixlorem.processing.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.iqkv.incubator.sample.mixlorem.processing.config.NetClientProperties;
import com.iqkv.incubator.sample.mixlorem.processing.enums.LengthType;
import com.iqkv.incubator.sample.mixlorem.processing.exception.NetClientCommunicationException;
import com.iqkv.incubator.sample.mixlorem.processing.exception.NetClientMisconfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
