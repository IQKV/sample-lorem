package org.ujar.loremipsum.wordsstatistic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.ujar.loremipsum.wordsstatistic.model.Report;

@Configuration
public class KafkaConfiguration {

  @Bean
  public ProducerFactory<String, Report> historyNotifierProducerFactory(KafkaProperties kafkaProperties) {
    var serde = new JsonSerde<>(Report.class, new ObjectMapper());
    var producerProperties = kafkaProperties.getProducer().buildProperties();
    var producerFactory = new DefaultKafkaProducerFactory<String, Report>(producerProperties,
        new StringSerializer(),
        serde.serializer());
    producerFactory.setTransactionIdPrefix(getTransactionPrefix());
    return producerFactory;
  }

  @Bean
  public KafkaTemplate<String, Report> historyNotifierKafkaTemplate(
      ProducerFactory<String, Report> historyNotifierProducerFactory) {
    return new KafkaTemplate<>(historyNotifierProducerFactory);
  }

  private String getTransactionPrefix() {
    return "tx-" + UUID.randomUUID() + "-";
  }
}
