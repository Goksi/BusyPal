package tech.goksi.busypal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.goksi.busypal.manager.BusyManager;

@RestController
public class BusyController {

  private final BusyManager busyManager;

  public BusyController(BusyManager busyManager) {
    this.busyManager = busyManager;
  }

  @PostMapping(value = "/api/busy", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void setBusy(HttpServletRequest request, @RequestParam("busy") boolean busy) {
    HttpSession session = request.getSession(false);
    String sessionId = session != null ? session.getId() : null;
    if (sessionId == null) {
      throw new IllegalStateException("Session ID must not be null");
    }

    busyManager.setBusy(sessionId, busy);
  }
}
