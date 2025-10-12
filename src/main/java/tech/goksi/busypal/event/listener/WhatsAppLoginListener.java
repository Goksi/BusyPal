package tech.goksi.busypal.event.listener;

import it.auties.whatsapp.api.Listener;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.jid.Jid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.busypal.security.handler.messaging.WhatsAppSessionMessagingHandler;

/*TODO: build some abstraction around this*/
public class WhatsAppLoginListener implements Listener {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppLoginListener.class);

  private final String sessionId;
  private final WhatsAppSessionMessagingHandler messagingHandler;

  public WhatsAppLoginListener(String sessionId,
      WhatsAppSessionMessagingHandler messagingHandler) {
    this.sessionId = sessionId;
    this.messagingHandler = messagingHandler;
  }

  @Override
  public void onLoggedIn(Whatsapp whatsapp) {
    doDebugLoginLog(whatsapp);
    messagingHandler.handleAutomaticLogin(sessionId);
  }

  private void doDebugLoginLog(Whatsapp whatsapp) {
    Jid user = whatsapp.store().jid().orElse(null);
    String number = null;
    if (user != null) {
      number = user.toPhoneNumber().orElse(null);
    }
    LOGGER.debug("Phone number {} successfully logged into BusyPal",
        number == null ? "UNKNOWN" : number);
  }
}
