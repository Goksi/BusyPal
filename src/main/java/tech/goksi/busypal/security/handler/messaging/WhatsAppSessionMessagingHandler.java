package tech.goksi.busypal.security.handler.messaging;

import org.springframework.stereotype.Component;
import tech.goksi.busypal.event.publisher.EventPublisher;

@Component
public class WhatsAppSessionMessagingHandler {

  private final EventPublisher publisher;

  public WhatsAppSessionMessagingHandler(EventPublisher publisher) {
    this.publisher = publisher;
  }

  public void handleAutomaticLogin(String sessionId) {
    publisher.publishWhatsAppLoggedIn(sessionId);
  }
}
