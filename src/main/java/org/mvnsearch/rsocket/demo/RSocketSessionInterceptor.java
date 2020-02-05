package org.mvnsearch.rsocket.demo;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.plugins.RSocketInterceptor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * RSocket session interceptor
 *
 * @author linux_china
 */
public class RSocketSessionInterceptor implements RSocketInterceptor {
    @Override
    public RSocket apply(RSocket source) {
        return new AbstractRSocket() {
            private SessionContext sessionContext = new SessionContext();

            @Override
            public Mono<Payload> requestResponse(Payload payload) {
                return source.requestResponse(payload).subscriberContext(sessionContext::putAll);
            }

            @Override
            public Mono<Void> fireAndForget(Payload payload) {
                return source.fireAndForget(payload).subscriberContext(sessionContext::putAll);
            }

            @Override
            public Flux<Payload> requestStream(Payload payload) {
                return source.requestStream(payload).subscriberContext(sessionContext::putAll);
            }

            @Override
            public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
                return source.requestChannel(payloads).subscriberContext(sessionContext::putAll);
            }

            @Override
            public Mono<Void> metadataPush(Payload payload) {
                return source.metadataPush(payload).subscriberContext(sessionContext::putAll);
            }

            @Override
            public void dispose() {
                source.dispose();
            }

            @Override
            public boolean isDisposed() {
                return source.isDisposed();
            }

            @Override
            public Mono<Void> onClose() {
                return source.onClose();
            }

            @Override
            public double availability() {
                return source.availability();
            }
        };
    }
}
