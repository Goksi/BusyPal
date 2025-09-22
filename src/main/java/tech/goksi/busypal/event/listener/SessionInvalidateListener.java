package tech.goksi.busypal.event.listener;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.manager.WhatsAppManager;

@Component
public class SessionInvalidateListener implements ApplicationListener<HttpSessionDestroyedEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionInvalidateListener.class);

  private final WhatsAppManager manager;

  public SessionInvalidateListener(WhatsAppManager manager) {
    this.manager = manager;
  }

  @Override
  public void onApplicationEvent(@NotNull HttpSessionDestroyedEvent event) {
    String sessionId = event.getSession().getId();
    LOGGER.debug(
        "Session with id {} invalidated, removing any whatsapp sessions associated with it...",
        sessionId);
    manager.removeSession(sessionId);
  }
}
