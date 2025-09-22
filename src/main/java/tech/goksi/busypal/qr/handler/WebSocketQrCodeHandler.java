package tech.goksi.busypal.qr.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.event.publisher.EventPublisher;

@Component
public class WebSocketQrCodeHandler {

  private final EventPublisher publisher;
  private final Map<String, String> qrMapping;

  public WebSocketQrCodeHandler(EventPublisher publisher) {
    this.publisher = publisher;
    this.qrMapping = new HashMap<>();
  }

  public void handle(String sessionId, String qrData) {
    publisher.publishQrCodeEvent(sessionId, qrData);
    qrMapping.put(sessionId, qrData);
  }

  public void handleFromCache(String sessionId) {
    String qrData = qrMapping.get(sessionId);
    if (qrData == null) {
      return;
    }
    handle(sessionId, qrData);
  }

  public void handleExpired(String sessionId) {
    publisher.publishQrCodeExpiredEvent(sessionId);
  }

  public void clearCache(String sessionId) {
    qrMapping.remove(sessionId);
  }
}
