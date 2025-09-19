package tech.goksi.busypal.event;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.orchestrator.WhatsAppSessionOrchestrator;

@Component
public class SessionInvalidateListener implements ApplicationListener<HttpSessionDestroyedEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionInvalidateListener.class);

  private final WhatsAppSessionOrchestrator sessionOrchestrator;

  public SessionInvalidateListener(WhatsAppSessionOrchestrator sessionOrchestrator) {
    this.sessionOrchestrator = sessionOrchestrator;
  }

  @Override
  public void onApplicationEvent(@NotNull HttpSessionDestroyedEvent event) {
    String sessionId = event.getSession().getId();
    LOGGER.debug(
        "Session with id {} invalidated, removing any whatsapp sessions associated with it...",
        sessionId);
    sessionOrchestrator.removeSession(sessionId);
  }
}
