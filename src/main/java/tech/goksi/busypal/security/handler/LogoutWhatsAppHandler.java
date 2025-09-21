package tech.goksi.busypal.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.manager.WhatsAppManager;

@Component
public class LogoutWhatsAppHandler implements LogoutHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogoutWhatsAppHandler.class);
  private final WhatsAppManager manager;

  public LogoutWhatsAppHandler(WhatsAppManager manager) {
    this.manager = manager;
  }

  @Override
  public void logout(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    LOGGER.debug("User with id {} is logging out. Removing his WhatsApp session.", session.getId());
    manager.removeSession(session.getId());
  }
}
