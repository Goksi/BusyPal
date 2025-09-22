package tech.goksi.busypal.controller;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tech.goksi.busypal.event.ClientReadyWsEvent;
import tech.goksi.busypal.manager.WhatsAppManager;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebSocketControllerTest {

  @LocalServerPort
  private int port;
  @MockitoBean
  private WhatsAppManager manager;
  private WebSocketStompClient client;

  @BeforeEach
  void setup() {
    client = new WebSocketStompClient(
        new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    client.setMessageConverter(new MappingJackson2MessageConverter());
  }

  @Test
  void clientReady_shouldCreateSession()
      throws ExecutionException, InterruptedException, TimeoutException {
    String connectionUrl = "ws://localhost:%d/ws".formatted(port);
    String testSession = "testSession";
    ClientReadyWsEvent readyWsEvent = new ClientReadyWsEvent();
    readyWsEvent.setCookie(testSession);

    StompSession stompSession = client.connectAsync(connectionUrl,
        new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.SECONDS);

    stompSession.send("/busypal/ready", readyWsEvent);

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      verify(manager).createSession(eq(readyWsEvent.getCookie()));
    });
  }
}
