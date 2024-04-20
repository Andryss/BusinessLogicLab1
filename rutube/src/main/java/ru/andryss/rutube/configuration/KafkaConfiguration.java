package ru.andryss.rutube.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${topic.moderation.requests}")
    private String moderationRequestsTopic;

    @Value("${topic.moderation.resends}")
    private String moderationResendsTopic;

    @Value("${topic.moderation.results}")
    private String moderationResultsTopic;

    private final String typeMappings = "request:ru.andryss.rutube.message.ModerationRequestInfo, " +
            "resend:ru.andryss.rutube.message.ModerationResendInfo";

    @Bean
    public KafkaProducer<String, Object> kafkaProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(JsonSerializer.TYPE_MAPPINGS, typeMappings);
        return new KafkaProducer<>(props, new StringSerializer(), new JsonSerializer<>(new TypeReference<>(){}));
    }

    @Bean
    public ConsumerFactory<String, Object> kafkaConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(JsonDeserializer.TYPE_MAPPINGS, typeMappings);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(new TypeReference<>(){}));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory());
        return factory;
    }

    @Bean
    public NewTopic moderationRequestsTopic() {
        return new NewTopic(moderationRequestsTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic moderationResendsTopic() {
        return new NewTopic(moderationResendsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic moderationResultsTopic() {
        return new NewTopic(moderationResultsTopic, 1, (short) 1);
    }

}
