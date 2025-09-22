package tech.goksi.busypal.event;

public class QrCodeExpiredWsEvent extends BaseWsEvent {

  public QrCodeExpiredWsEvent() {
    super("wa_qr_expired");
  }
}
