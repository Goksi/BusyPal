package tech.goksi.busypal.orchestrator;

import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.goksi.busypal.BusyPalProperties;
import tech.goksi.busypal.event.debug.DebugEventListener;
import tech.goksi.busypal.qr.handler.WebSocketQrCodeHandler;

/**
 * Service for orchestrating WhatsApp sessions. Manages creation, retrieval, and removal of WhatsApp
 * connections per busypal_session.
 */
@Service
public class WhatsAppSessionOrchestrator {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppSessionOrchestrator.class);

  private final Map<String, Whatsapp> sessions;
  private final Set<String> queuedSessions;
  private final BusyPalProperties properties;
  private final WebSocketQrCodeHandler webSocketQrCodeHandler;

  /**
   * Constructs a new WhatsAppSessionOrchestrator.
   *
   * @param properties application properties
   */
  public WhatsAppSessionOrchestrator(BusyPalProperties properties,
      WebSocketQrCodeHandler webSocketQrCodeHandler) {
    this.sessions = new HashMap<>();
    this.queuedSessions = new HashSet<>();
    this.properties = properties;
    this.webSocketQrCodeHandler = webSocketQrCodeHandler;
  }

  /**
   * Creates a new WhatsApp session for the given busypal_session and send it to user over
   * websocket. If specific busypal_session already contains active queued session, it will be
   * returned from cache
   *
   * @param sessionId the busypal_session identifier
   */
  public void createNewSession(String sessionId) {
    if (queuedSessions.contains(sessionId)) {
      LOGGER.debug(
          "Session request queued for {} already exist, sending old qr trough ws if it exists",
          sessionId);
      webSocketQrCodeHandler.handleFromCache(sessionId);
      return;
    }
    LOGGER.debug("Creating new whatsapp session for busypal session id {}", sessionId);
    Whatsapp.webBuilder()
        .newConnection(sessionId)
        .historySetting(WebHistorySetting.discard(false))
        .name(properties.getDevice().getName())
        .unregistered(qr -> webSocketQrCodeHandler.handle(sessionId, qr))
        .addListener(new DebugEventListener())
        .connect()
        .orTimeout(properties.getLoginTimeout(), TimeUnit.SECONDS)
        .whenComplete((whatsapp, throwable) -> {
          if (throwable != null) {
            LOGGER.debug("User with id {} login timeout !", sessionId);
          } else {
            sessions.put(sessionId, whatsapp);
          }
          queuedSessions.remove(sessionId);
          webSocketQrCodeHandler.clearCache(sessionId);
        });
    queuedSessions.add(sessionId);
  }

  /**
   * Retrieves the WhatsApp session for the given busypal_session.
   *
   * @param sessionId the busypal_session identifier
   * @return the WhatsApp session, or null if not found
   */
  public Whatsapp getSession(String sessionId) {
    return sessions.get(sessionId);
  }

  /**
   * Removes and disconnects the WhatsApp session for the given busypal_session.
   *
   * @param sessionId the busypal_session identifier
   */
  public void removeSession(String sessionId) {
    disconnectSession(sessionId);
    sessions.remove(sessionId);
  }

  /**
   * Disconnects the WhatsApp session for the given busypal_session.
   *
   * @param sessionId the busypal_session identifier
   */
  public void disconnectSession(String sessionId) {
    var session = sessions.get(sessionId);
    if (session != null) {
      session.disconnect();
    }
  }
}
