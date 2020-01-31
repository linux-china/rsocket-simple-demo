package org.mvnsearch.rsocket.demo;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.uri.UriTransportRegistry;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * RSocket local test
 *
 * @author linux_china
 */
@ExtendWith(RSocketLocalExtension.class)
public class RSocketLocalTest {
    private static RSocket requester;

    @BeforeAll
    public static void setUp() {
        requester = RSocketFactory.connect()
                .transport(UriTransportRegistry.clientForUri("local:test"))
                .start()
                .block();
    }

    @AfterAll
    public static void tearDown() {
        requester.dispose();
    }


    @Test
    public void testRequestResponse() {
        Mono<Payload> payloadMono = requester.requestResponse(DefaultPayload.create("雷卷", "content-type:text/plain"));
        StepVerifier.create(payloadMono).assertNext(payload -> {
            System.out.println(payload.getDataUtf8());
        }).verifyComplete();
    }

    @Test
    public void testRequestStream() throws Exception {
        Flux<Payload> flux = requester.requestStream(DefaultPayload.create("雷卷", "content-type:text/plain"));
        flux.subscribe(payload -> {
            System.out.println(payload.getDataUtf8());
        });
        Thread.sleep(2000);
    }


    private static RSocket constructRSocket(String url) {
        return RSocketFactory.connect()
                .transport(UriTransportRegistry.clientForUri(url))
                .start()
                .block();
    }
}
