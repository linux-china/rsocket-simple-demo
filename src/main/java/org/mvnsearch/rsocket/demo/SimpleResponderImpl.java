package org.mvnsearch.rsocket.demo;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * simple responder implementation
 *
 * @author linux_china
 */
public class SimpleResponderImpl extends AbstractRSocket {
    private String id;
    private RSocket requester;
    private Mono<Void> comboOnClose;

    public SimpleResponderImpl(ConnectionSetupPayload setupPayload, RSocket requester) {
        //CompositeMetadata compositeMetadata = new CompositeMetadata(setupPayload.metadata(), false);
        this.id = UUID.randomUUID().toString();
        this.requester = requester;
        this.comboOnClose = Mono.first(super.onClose(), requester.onClose());
    }

    @Override
    public Mono<Void> onClose() {
        return this.comboOnClose;
    }

    public String getId() {
        return id;
    }

    public RSocket getRequester() {
        return requester;
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return Mono.deferWithContext((context -> {
            String dataUtf8 = payload.getDataUtf8();
            if (context.hasKey("counter")) {
                context.put("counter", ((Integer) context.get("counter") + 1));
            } else {
                context.put("counter", 1);
            }
            return Mono.just(DefaultPayload.create(String.format("Hello %s! Request: %s", dataUtf8, context.get("counter"))));
        }));
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("FireAndForget:" + payload.getDataUtf8());
        return Mono.empty();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return Flux.just("first", "second", "third", "fourth").map(DefaultPayload::create);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.just(DefaultPayload.create("first", "metadata"), DefaultPayload.create("second", "metadata"));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        System.out.println("metadata push received: " + payload.getMetadataUtf8());
        return Mono.empty();
    }

}
