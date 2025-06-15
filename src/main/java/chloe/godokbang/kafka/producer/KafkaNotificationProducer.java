package chloe.godokbang.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "NOTIFICATION";

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
