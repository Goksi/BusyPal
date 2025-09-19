package tech.goksi.busypal.event.debug;

import it.auties.whatsapp.api.Listener;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.jid.Jid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*TODO: create some abstraction around this*/
public class DebugEventListener implements Listener {

  private static final Logger LOGGER = LoggerFactory.getLogger(DebugEventListener.class);

  @Override
  public void onLoggedIn(Whatsapp whatsapp) {
    Jid user = whatsapp.store().jid().orElse(null);
    String number = null;
    if (user != null) {
      number = user.toPhoneNumber().orElse(null);
    }
    LOGGER.debug("Phone number {} successfully logged into BusyPal",
        number == null ? "UNKNOWN" : number);
  }
}
