package com.example.payments_kafka.config;

import com.example.payments_kafka.model.PaymentEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import com.example.payments_kafka.model.PaymentEvent;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    KeyValueBytesStoreSupplier storeSupplier =
            Stores.persistentKeyValueStore("balances-store");

    @Bean
    public KafkaStreams kafkaStreams(JsonSerde<PaymentEvent> paymentEventSerde) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "payments-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, PaymentEvent> stream =
                builder.stream("payments",
                        Consumed.with(Serdes.String(), paymentEventSerde));

        stream
                .mapValues(e -> e.getType().equalsIgnoreCase("A") ? e.getAmount() : -e.getAmount())
                .groupByKey()
                .reduce(Double::sum,
                        Materialized.<String, Double>as(storeSupplier)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double())
                );

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        return streams;
    }

    @Bean
    public JsonSerde<PaymentEvent> paymentEventSerde() {
        return new JsonSerde<>(PaymentEvent.class);
    }
}

