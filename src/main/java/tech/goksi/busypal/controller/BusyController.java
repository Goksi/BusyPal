package tech.goksi.busypal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.goksi.busypal.manager.BusyManager;

@RestController
public class BusyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BusyController.class);

  private final BusyManager busyManager;

  public BusyController(BusyManager busyManager) {
    this.busyManager = busyManager;
  }

  @PostMapping(value = "/api/busy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void setBusy(HttpServletRequest request, @RequestParam("busy") boolean busy) {
    HttpSession session = request.getSession(false);
    String sessionId = session != null ? session.getId() : null;
    if (sessionId == null) {
      throw new IllegalStateException("Session ID must not be null");
    }
    LOGGER.debug("User with session id {} set their busy status to {}", sessionId, busy);
    busyManager.setBusy(sessionId, busy);
  }
}
