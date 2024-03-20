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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.andryss.rutube.message.ModerationRequestInfo;
import ru.andryss.rutube.message.ModerationResultInfo;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${topic.moderation.requests}")
    private String moderationRequestsTopic;

    @Value("${topic.moderation.results}")
    private String moderationResultsTopic;

    @Bean
    public KafkaProducer<String, ModerationRequestInfo> requestProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaProducer<>(props, new StringSerializer(), new JsonSerializer<>(new TypeReference<>(){}));
    }

    @Bean
    public ConsumerFactory<String, ModerationResultInfo> resultConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(new TypeReference<>(){}));
    }

    @Bean
    public NewTopic moderationRequestsTopic() {
        return new NewTopic(moderationRequestsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic moderationResultsTopic() {
        return new NewTopic(moderationResultsTopic, 1, (short) 1);
    }

}
