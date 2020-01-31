package org.mvnsearch.rsocket.demo;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * RSocket responder factory
 *
 * @author linux_china
 */
public class SimpleResponderFactory {
    public List<SimpleResponderImpl> handlers = new ArrayList<>();

    public SocketAcceptor responder() {
        return (this::createResponder);
    }

    public Mono<RSocket> createResponder(ConnectionSetupPayload setupPayload, RSocket requester) {
        //CompositeMetadata compositeMetadata = new CompositeMetadata(setupPayload.metadata(), false);
        //security authentication
        SimpleResponderImpl handler = new SimpleResponderImpl(setupPayload, requester);
        handlers.add(handler);
        handler.onClose().doFinally(aVoid -> {
            handlers.remove(handler);
        }).subscribe();
        return Mono.just(handler);
    }

}
