package tech.goksi.busypal.event.publisher.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.event.QrCodeExpiredWsEvent;
import tech.goksi.busypal.event.QrCodeWsEvent;
import tech.goksi.busypal.event.WhatsAppLoggedInWsEvent;

@Component
public class SimpEventPublisher extends AbstractSimpEventPublisher {

  public SimpEventPublisher(SimpMessagingTemplate template) {
    super(template);
  }

  @Override
  public void publishQrCodeEvent(String session, String qrData) {
    publishEvent(session, "/topic/qr", new QrCodeWsEvent(qrData));
  }

  @Override
  public void publishQrCodeExpiredEvent(String session) {
    publishEvent(session, "/topic/qr", new QrCodeExpiredWsEvent());
  }

  @Override
  public void publishWhatsAppLoggedIn(String session) {
    publishEvent(session, "/topic/wa", new WhatsAppLoggedInWsEvent());
  }
}
