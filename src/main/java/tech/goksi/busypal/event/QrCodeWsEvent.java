package tech.goksi.busypal.event;

public class QrCodeWsEvent extends BaseWsEvent {

  private final String qr;

  public QrCodeWsEvent(String qr) {
    super("wa_qr_code");
    this.qr = qr;
  }

  public String getQr() {
    return qr;
  }
}
