package tech.goksi.busypal.qr.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketQrCodeHandler {

  private final SimpMessagingTemplate template;
  private final Map<String, String> qrMapping;

  public WebSocketQrCodeHandler(SimpMessagingTemplate template) {
    this.template = template;
    this.qrMapping = new HashMap<>();
  }

  public void handle(String sessionId, String qrData) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
        .create(SimpMessageType.MESSAGE);
    headerAccessor.setSessionId(sessionId);
    headerAccessor.setLeaveMutable(true);
    template.convertAndSendToUser(
        sessionId,
        "/topic/qr",
        qrData,
        headerAccessor.getMessageHeaders()
    );
    qrMapping.put(sessionId, qrData);
  }

  public void handleFromCache(String sessionId) {
    String qrData = qrMapping.get(sessionId);
    if (qrData == null) {
      return;
    }
    handle(sessionId, qrData);
  }

  public void clearCache(String sessionId) {
    qrMapping.remove(sessionId);
  }
}
