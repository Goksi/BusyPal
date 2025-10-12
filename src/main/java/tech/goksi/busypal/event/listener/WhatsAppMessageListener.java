package tech.goksi.busypal.event.listener;

import it.auties.whatsapp.api.Listener;
import it.auties.whatsapp.model.info.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.busypal.manager.BusyManager;
import tech.goksi.busypal.manager.WhatsAppManager;

public class WhatsAppMessageListener implements Listener {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppMessageListener.class);

  private final String sessionId;
  private final WhatsAppManager whatsAppManager;
  private final BusyManager busyManager;

  public WhatsAppMessageListener(String sessionId, WhatsAppManager whatsAppManager,
      BusyManager busyManager) {
    this.sessionId = sessionId;
    this.whatsAppManager = whatsAppManager;
    this.busyManager = busyManager;
  }

  @Override
  public void onNewMessage(MessageInfo<?> info) {
    if (!busyManager.isBusy(sessionId)) {
      LOGGER.debug("Session with id {} received a message but is not busy", sessionId);
      return;
    }
    LOGGER.debug("Session with id {} received a message and is busy", sessionId);

  }
}
