package com.sunil.bainsla.kafkapactconsumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "paymentProviderKafka", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V3)
public class KafkaPactConsumerServiceApplicationTests {

    @Pact(consumer = "paymentConsumerKafka")
    MessagePact validMessageFromKafkaProvider(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.uuid("id");
        body.stringType("data");
        body.stringType("paymentDate");
        body.numberType("amount");
        body.stringType("name");
        body.stringType("accountNumber");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");
      //  metadata.put("kafka_topic", "product-order-event");

        return builder.expectsToReceive("A valid Payment created event").withContent(body).toPact();
    }

    @Test
    @PactTestFor(pactMethod = "validMessageFromKafkaProvider")
    public void testValidDateFromProvider(List<Message> messages) throws Exception{

        Payment product = new ObjectMapper().readValue(messages.get(0).contentsAsString(), Payment.class);
        assertThat(messages).isNotEmpty();
        assertThat(new ObjectMapper().readValue(messages.get(0).contentsAsBytes(), Payment.class))
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("data")
                .hasFieldOrProperty("paymentDate")
                .hasFieldOrProperty("amount")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("accountNumber");
    }
}
