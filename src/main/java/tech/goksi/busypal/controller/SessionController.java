package tech.goksi.busypal.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import tech.goksi.busypal.orchestrator.WhatsAppSessionOrchestrator;
import tech.goksi.busypal.utils.QrCodeConsoleLogHandler;

@Controller
public class SessionController {

  private final WhatsAppSessionOrchestrator sessionOrchestrator;

  public SessionController(WhatsAppSessionOrchestrator sessionOrchestrator) {
    this.sessionOrchestrator = sessionOrchestrator;
  }

  @PostMapping("/create-session")
  public String createSession(HttpSession session) {
    sessionOrchestrator.createNewSession(session.getId(), QrCodeConsoleLogHandler.getInstance());
    return "redirect:/?session=created";
  }
}
