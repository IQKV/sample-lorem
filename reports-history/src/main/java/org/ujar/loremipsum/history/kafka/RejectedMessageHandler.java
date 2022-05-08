package org.ujar.loremipsum.history.kafka;

import java.util.List;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;
import org.ujar.loremipsum.history.kafka.exception.ConsumerRecordProcessingException;

@Slf4j
public class RejectedMessageHandler extends SeekToCurrentErrorHandler {

  private final String rejectedTopic;
  private final KafkaTemplate<String, String> rejectedMessageKafkaTemplate;

  public RejectedMessageHandler(String rejectedTopic, KafkaTemplate<String, String> rejectedMessageKafkaTemplate) {
    super(new FixedBackOff(500, 1));
    this.rejectedTopic = rejectedTopic;
    this.rejectedMessageKafkaTemplate = rejectedMessageKafkaTemplate;
  }

  @Override
  public void handle(Exception thrownException, @NonNull List<ConsumerRecord<?, ?>> consumerRecords,
     @NonNull Consumer<?, ?> consumer, @NonNull MessageListenerContainer container) {
    Throwable cause = thrownException.getCause();
    if (cause instanceof DeserializationException) {
      consumerRecords.forEach(consumerRecord -> {
        log.error("Exception occurred while handle message: {}", consumerRecord.value(), cause);
        String key = Objects.isNull(consumerRecord.key()) ? null : consumerRecord.key().toString();
        rejectedMessageKafkaTemplate.send(rejectedTopic, key, new String(((DeserializationException) cause).getData()));
      });
    } else if (cause instanceof ConsumerRecordProcessingException) {
      consumerRecords.forEach(consumerRecord -> {
        log.error("Exception occurred while handle message: {}", consumerRecord.value(), cause);
        String key = Objects.isNull(consumerRecord.key()) ? null : consumerRecord.key().toString();
        rejectedMessageKafkaTemplate.send(rejectedTopic, key, consumerRecord.value().toString());
      });
    } else {
      super.handle(thrownException, consumerRecords, consumer, container);
    }
  }
}
