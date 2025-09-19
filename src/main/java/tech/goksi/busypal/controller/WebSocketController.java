package tech.goksi.busypal.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tech.goksi.busypal.event.ClientReadyWsEvent;
import tech.goksi.busypal.manager.WhatsAppManager;

@Controller
public class WebSocketController {

  private final WhatsAppManager manager;

  public WebSocketController(WhatsAppManager manager) {
    this.manager = manager;
  }

  @MessageMapping("ready")
  public void clientReady(ClientReadyWsEvent event) {
    manager.createSession(event.getCookie());
  }
}
