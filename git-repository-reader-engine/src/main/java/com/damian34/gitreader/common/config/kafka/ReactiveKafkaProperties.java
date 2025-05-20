package com.damian34.gitreader.common.config.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class ReactiveKafkaProperties {
    private String bootstrapServers;
    private String consumerGroupId;
    private Consumer consumer = new Consumer();
    private Topics topics = new Topics();
    private Reactor reactor = new Reactor();

    public Map<String, Object> createConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.autoOffsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }

    @Data
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
    }

    @Data
    public static class Topics {
        private String gitCredentials;
    }

    @Data
    public static class Reactor {
        private Integer pollTimeout;
    }
} 