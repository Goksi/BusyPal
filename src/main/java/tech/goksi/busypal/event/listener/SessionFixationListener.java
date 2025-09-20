package tech.goksi.busypal.event.listener;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.manager.WhatsAppManager;

@Component
public class SessionFixationListener implements
    ApplicationListener<SessionFixationProtectionEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionFixationListener.class);

  private final WhatsAppManager manager;

  public SessionFixationListener(WhatsAppManager manager) {
    this.manager = manager;
  }

  @Override
  public void onApplicationEvent(@NotNull SessionFixationProtectionEvent event) {
    LOGGER.debug("Session fixation protection for id {} occurred, migrating whatsapp session...",
        event.getOldSessionId());
    manager.migrateSession(event.getOldSessionId(), event.getNewSessionId());
  }
}
