package tech.goksi.busypal.security.handler.messaging;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.goksi.busypal.event.publisher.EventPublisher;

class WhatsAppSessionMessagingHandlerTest {

  private WhatsAppSessionMessagingHandler handler;
  private EventPublisher publisher;

  @BeforeEach
  void setup() {
    publisher = mock(EventPublisher.class);
    handler = new WhatsAppSessionMessagingHandler(publisher);
  }

  @Test
  void handleAutomaticLogin_shouldSendAutomaticLoginEvent() {
    handler.handleAutomaticLogin("sessionTest");
    verify(publisher).publishWhatsAppLoggedInEvent(eq("sessionTest"));
  }
}
