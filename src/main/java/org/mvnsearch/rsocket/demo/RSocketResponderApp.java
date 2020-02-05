package org.mvnsearch.rsocket.demo;

import io.rsocket.Closeable;
import io.rsocket.RSocketFactory;
import io.rsocket.uri.UriTransportRegistry;

/**
 * RSocket Responder demo app
 *
 * @author linux_china
 */
public class RSocketResponderApp {
    public static void main(String[] args) throws Exception {
        SimpleResponderFactory responderFactory = new SimpleResponderFactory();
        String listenUri = "tcp://0.0.0.0:42252";
        Closeable server = (Closeable) RSocketFactory.receive()
                .addResponderPlugin(new RSocketSessionInterceptor())
                .acceptor(responderFactory.responder())
                .transport(UriTransportRegistry.serverForUri(listenUri))
                .start()
                .block();
        System.out.println("RSocket responder is listening on 42252!");
        server.onClose().block();
    }
}
