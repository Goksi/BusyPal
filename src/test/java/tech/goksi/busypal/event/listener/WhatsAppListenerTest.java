package tech.goksi.busypal.event.listener;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.model.jid.Jid;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import tech.goksi.busypal.TestMemoryAppender;
import tech.goksi.busypal.security.handler.messaging.WhatsAppSessionMessagingHandler;

class WhatsAppListenerTest {

  private WhatsAppListener listener;
  private TestMemoryAppender appender;

  @BeforeEach
  void setup() {
    listener = new WhatsAppListener("testSession", mock(WhatsAppSessionMessagingHandler.class));
    appender = new TestMemoryAppender();
    Logger logger = (Logger) LoggerFactory.getLogger(WhatsAppListener.class);
    logger.setLevel(Level.DEBUG);
    logger.addAppender(appender);
    appender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
    appender.start();
  }

  @Test
  void onLoggedIn_shouldLogUnknownNumberIfNoUser() {
    var api = mock(Whatsapp.class);
    var store = mock(Store.class);
    when(store.jid()).thenReturn(Optional.empty());
    when(api.store()).thenReturn(store);
    listener.onLoggedIn(api);
    assertTrue(appender.contains("Phone number UNKNOWN successfully logged into BusyPal"));
  }

  @Test
  void onLoggedIn_shouldLogUnknownNumberIfNoNumber() {
    var api = mock(Whatsapp.class);
    var store = mock(Store.class);
    var jid = mock(Jid.class);
    when(jid.toPhoneNumber()).thenReturn(Optional.empty());
    when(store.jid()).thenReturn(Optional.of(jid));
    when(api.store()).thenReturn(store);
    listener.onLoggedIn(api);
    assertTrue(appender.contains("Phone number UNKNOWN successfully logged into BusyPal"));
  }

  @Test
  void onLoggedIn_shouldLogNumber() {
    var api = mock(Whatsapp.class);
    var store = mock(Store.class);
    var jid = mock(Jid.class);
    when(jid.toPhoneNumber()).thenReturn(Optional.of("4321"));
    when(store.jid()).thenReturn(Optional.of(jid));
    when(api.store()).thenReturn(store);
    listener.onLoggedIn(api);
    assertTrue(appender.contains("Phone number 4321 successfully logged into BusyPal"));
  }
}
