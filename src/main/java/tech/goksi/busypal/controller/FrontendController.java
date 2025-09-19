package tech.goksi.busypal.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tech.goksi.busypal.BusyPalEndpoint;
import tech.goksi.busypal.manager.WhatsAppManager;

@Controller
public class FrontendController {

  private final WhatsAppManager whatsAppManager;

  public FrontendController(WhatsAppManager whatsAppManager) {
    this.whatsAppManager = whatsAppManager;
  }

  @GetMapping(BusyPalEndpoint.INDEX)
  public String index() {
    return "index";
  }

  @GetMapping(BusyPalEndpoint.LOGS)
  public String logs() {
    return "logs";
  }

  @GetMapping(BusyPalEndpoint.LOGIN)
  public String login(HttpSession session) {
    whatsAppManager.createSession(session.getId());
    return "auth/login";
  }
}
