package tech.goksi.busypal.event.publisher;

import tech.goksi.busypal.event.BaseWsEvent;

public interface EventPublisher {

  void publishEvent(String session, String destination, BaseWsEvent event);

  void publishQrCodeEvent(String session, String qrData);

  void publishQrCodeExpiredEvent(String session);

  void publishWhatsAppLoggedInEvent(String session);
}
