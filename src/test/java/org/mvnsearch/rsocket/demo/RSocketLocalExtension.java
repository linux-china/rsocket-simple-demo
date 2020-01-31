package org.mvnsearch.rsocket.demo;

import io.rsocket.Closeable;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.local.LocalServerTransport;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import reactor.core.publisher.Mono;

/**
 * rsocket local junit 5 extension for test
 *
 * @author leijuan
 */
public class RSocketLocalExtension implements BeforeAllCallback, AfterAllCallback {
    private static Closeable localServer;

    @Override
    public void afterAll(ExtensionContext context) {
        localServer.dispose();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        //create a local test server
        localServer = RSocketFactory.receive()
                .acceptor((setupPayload, rSocket) -> Mono.just(new SimpleResponderImpl(setupPayload, rSocket)))
                .transport(LocalServerTransport.create("test"))
                .start()
                .block();
    }

}
