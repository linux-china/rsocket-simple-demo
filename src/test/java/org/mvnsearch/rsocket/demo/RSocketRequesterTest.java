package org.mvnsearch.rsocket.demo;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.uri.UriTransportRegistry;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * RSocket requester test
 *
 * @author linux_china
 */
public class RSocketRequesterTest {
    private static RSocket requester;

    @BeforeAll
    public static void setUp() throws Exception {
        requester = RSocketFactory
                .connect()
                .dataMimeType(WellKnownMimeType.TEXT_PLAIN.getString())
                .transport(UriTransportRegistry.clientForUri("tcp://127.0.0.1:42252"))
                .start()
                .block();
    }

    @AfterAll
    public static void tearDown() {
        requester.dispose();
    }

    @Test
    public void testRequestResponse() throws Exception {
        requester.requestResponse(DefaultPayload.create("Your Name", ""))
                .subscribe(payload -> {
                    System.out.println(payload.getDataUtf8());
                });
        Thread.sleep(500000);
    }

    @Test
    public void testRequestStream() throws Exception {
        requester.requestStream(DefaultPayload.create("Your Name", ""))
                .limitRate(2)
                .subscribe(payload -> {
                    System.out.println(payload.getDataUtf8());
                });
        Thread.sleep(2000);
    }

    @Test
    public void testRequestChannel() throws Exception {
        Flux<Payload> result = requester.requestChannel(Flux.just(DefaultPayload.create("yourName")));
        result.subscribe(payload -> {
            System.out.println(payload.getDataUtf8());
        });
        Thread.sleep(2000);
    }

    @Test
    public void testMetadataPush() throws Exception {
        requester.metadataPush(DefaultPayload.create("", "metadata push")).subscribe();
        Thread.sleep(1000);
    }

}
