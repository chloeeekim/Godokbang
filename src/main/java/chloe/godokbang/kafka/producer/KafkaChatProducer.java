package chloe.godokbang.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaChatProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "CHAT";

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
