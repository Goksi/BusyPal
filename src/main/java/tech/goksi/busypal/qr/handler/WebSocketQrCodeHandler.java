package tech.goksi.busypal.qr.handler;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketQrCodeHandler {

  private final SimpMessagingTemplate template;

  public WebSocketQrCodeHandler(SimpMessagingTemplate template) {
    this.template = template;
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
  }
}
