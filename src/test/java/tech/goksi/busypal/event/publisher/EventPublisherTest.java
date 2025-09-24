package tech.goksi.busypal.event.publisher;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import tech.goksi.busypal.event.QrCodeExpiredWsEvent;
import tech.goksi.busypal.event.QrCodeWsEvent;
import tech.goksi.busypal.event.WhatsAppLoggedInWsEvent;
import tech.goksi.busypal.event.publisher.impl.SimpEventPublisher;

class EventPublisherTest {

  private EventPublisher publisher;
  private SimpMessagingTemplate template;

  @BeforeEach
  void setup() {
    template = mock(SimpMessagingTemplate.class);
    publisher = new SimpEventPublisher(template);
  }

  @Test
  void publishQrCodeEvent_shouldCorrectlyPublishEvent() {
    publisher.publishQrCodeEvent("session", "qrCode");
    ArgumentCaptor<QrCodeWsEvent> eventCaptor = ArgumentCaptor.forClass(QrCodeWsEvent.class);
    ArgumentCaptor<MessageHeaders> headersCaptor = ArgumentCaptor.forClass(MessageHeaders.class);
    verify(template).convertAndSendToUser(
        eq("session"),
        eq("/topic/qr"),
        eventCaptor.capture(),
        headersCaptor.capture()
    );

    var eventSent = eventCaptor.getValue();
    var headers = headersCaptor.getValue();
    assertAll(
        () -> assertEquals("qrCode", eventSent.getQr()),
        () -> assertEquals("wa_qr_code", eventSent.getEventType()),
        () -> assertEquals("session", headers.get("simpSessionId"))
    );
  }


  @Test
  void publishQrCodeExpiredEvent_shouldCorrectlyPublishEvent() {
    publisher.publishQrCodeExpiredEvent("session");
    ArgumentCaptor<QrCodeExpiredWsEvent> eventCaptor = ArgumentCaptor.forClass(QrCodeExpiredWsEvent.class);
    ArgumentCaptor<MessageHeaders> headersCaptor = ArgumentCaptor.forClass(MessageHeaders.class);
    verify(template).convertAndSendToUser(
        eq("session"),
        eq("/topic/qr"),
        eventCaptor.capture(),
        headersCaptor.capture()
    );

    var eventSent = eventCaptor.getValue();
    var headers = headersCaptor.getValue();
    assertAll(
        () -> assertEquals("wa_qr_expired", eventSent.getEventType()),
        () -> assertEquals("session", headers.get("simpSessionId"))
    );
  }

  @Test
  void publishWhatsAppLoggedInEvent_shouldCorrectlyPublishEvent() {
    publisher.publishWhatsAppLoggedInEvent("someSession");
    ArgumentCaptor<WhatsAppLoggedInWsEvent> eventCaptor = ArgumentCaptor.forClass(
        WhatsAppLoggedInWsEvent.class);
    ArgumentCaptor<MessageHeaders> headersCaptor = ArgumentCaptor.forClass(MessageHeaders.class);
    verify(template).convertAndSendToUser(
        eq("someSession"),
        eq("/topic/wa"),
        eventCaptor.capture(),
        headersCaptor.capture()
    );

    var eventSent = eventCaptor.getValue();
    var headers = headersCaptor.getValue();
    assertAll(
        () -> assertEquals("wa_logged_in", eventSent.getEventType()),
        () -> assertEquals("someSession", headers.get("simpSessionId"))
    );
  }
}
