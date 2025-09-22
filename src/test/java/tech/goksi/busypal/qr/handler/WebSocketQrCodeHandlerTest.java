package tech.goksi.busypal.qr.handler;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.goksi.busypal.event.publisher.EventPublisher;

class WebSocketQrCodeHandlerTest {

  private WebSocketQrCodeHandler handler;
  private EventPublisher publisher;

  @BeforeEach
  void setup() {
    publisher = mock(EventPublisher.class);
    handler = new WebSocketQrCodeHandler(publisher);
  }

  @Test
  void handleExpired_shouldPublishEvent() {
    handler.handleExpired("test");
    verify(publisher).publishQrCodeExpiredEvent(eq("test"));
  }

  @Test
  void handle_shouldPublishEvent() {
    handler.handle("test", "testQr");
    verify(publisher).publishQrCodeEvent(eq("test"), eq("testQr"));
  }

  @Test
  void handleFromCache_shouldHandleFromCache() {
    handler.handle("test", "testQr");
    handler.handleFromCache("test");
    verify(publisher, times(2)).publishQrCodeEvent(eq("test"), eq("testQr"));
  }

  @Test
  void handleFromCache_shouldNotHandleIfDoesntExistInCache() {
    handler.handle("test", "testQr");
    handler.clearCache("test");
    handler.handleFromCache("test");
    verify(publisher).publishQrCodeEvent(eq("test"), eq("testQr"));
  }
}
