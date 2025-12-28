package com.example.payments_kafka.controller;

import com.example.payments_kafka.model.BalanceResponse;
import com.example.payments_kafka.model.PaymentEvent;
import com.example.payments_kafka.producer.PaymentProducer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentProducer paymentProducer;
    private final KafkaStreams kafkaStreams;

    public PaymentController(PaymentProducer paymentProducer, KafkaStreams kafkaStreams) {
        this.paymentProducer = paymentProducer;
        this.kafkaStreams = kafkaStreams;
    }

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@RequestBody PaymentEvent payment) {
        paymentProducer.sendPayment(payment);
        return ResponseEntity.ok("Payment sent to Kafka");
    }

    @GetMapping("/search")
    public List<BalanceResponse> getBalances() {

        ReadOnlyKeyValueStore<String, Double> store =
                kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(
                                "balances-store",
                                QueryableStoreTypes.keyValueStore()
                        )
                );

        List<BalanceResponse> result = new ArrayList<>();

        try (KeyValueIterator<String, Double> iterator = store.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, Double> kv = iterator.next();
                result.add(new BalanceResponse(kv.key, kv.value));
            }
        }

        return result;
    }
}
