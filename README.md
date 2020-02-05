RSocket Simple Demo
===================

RSocket simple demo for responder creation, unit test for local etc.

# Responder Creation

![RSocket Responder](rsocket_responder.png)

# RSocket Session

请参考： [RSocketSessionInterceptor](src/main/java/org/mvnsearch/rsocket/demo/RSocketSessionInterceptor.java)

# Local Test with JUnit 5

RSocketLocalExtension & RSocketLocalTest

# Logback configuration for debug

```xml
<logger name="io.rsocket.FrameLogger" level="DEBUG"/>
```