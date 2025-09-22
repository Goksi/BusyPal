package tech.goksi.busypal.event;

public class ClientReadyWsEvent extends BaseWsEvent {

  private String cookie;

  public ClientReadyWsEvent() {
    super("client_ready");
  }

  public String getCookie() {
    return cookie;
  }

  public void setCookie(String cookie) {
    this.cookie = cookie;
  }
}
