package com.jenu.event;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FlightInstanceEventProducer {

    private final KafkaTemplate<String,FlightInstanceCreatedEvent> kafkaTemplate;

    public void sendFlightInstanceCreatedEvent(FlightInstanceCreatedEvent event) {
        kafkaTemplate.send("flight.instance.created", event);
    }






}
