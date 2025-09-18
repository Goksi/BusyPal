package tech.goksi.busypal.orchestrator;

import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebHistorySetting;
import it.auties.whatsapp.api.Whatsapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.goksi.busypal.BusyPalProperties;
import tech.goksi.busypal.event.debug.DebugEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service for orchestrating WhatsApp sessions.
 * Manages creation, retrieval, and removal of WhatsApp connections per JSESSIONID.
 */
@Service
public class WhatsAppSessionOrchestrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppSessionOrchestrator.class);

    private final Map<String, Whatsapp> sessions;
    private final BusyPalProperties properties;

    /**
     * Constructs a new WhatsAppSessionOrchestrator.
     *
     * @param properties application properties
     */
    public WhatsAppSessionOrchestrator(BusyPalProperties properties) {
        this.sessions = new HashMap<>();
        this.properties = properties;
    }

    /**
     * Creates a new WhatsApp session for the given JSESSIONID.
     *
     * @param sessionId the JSESSIONID identifier
     * @param qrHandler handler for QR code authentication
     */
    public void createNewSession(String sessionId, QrHandler qrHandler) {
        Whatsapp.webBuilder()
                .newConnection(sessionId)
                .historySetting(WebHistorySetting.discard(false))
                .name(properties.getDevice().getName())
                .unregistered(qrHandler)
                .addListener(new DebugEventListener())
                .connect()
                .orTimeout(properties.getLoginTimeout(), TimeUnit.SECONDS)
                .whenComplete((whatsapp, throwable) -> {
                    if (throwable != null) {
                        LOGGER.debug("User with id {} login timeout !", sessionId.toString());
                    } else {
                        sessions.put(sessionId, whatsapp);
                    }
                });
    }

    /**
     * Retrieves the WhatsApp session for the given JSESSIONID.
     *
     * @param sessionId the JSESSIONID identifier
     * @return the WhatsApp session, or null if not found
     */
    public Whatsapp getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Removes and disconnects the WhatsApp session for the given JSESSIONID.
     *
     * @param sessionId the JSESSIONID identifier
     */
    public void removeSession(String sessionId) {
        disconnectSession(sessionId);
        sessions.remove(sessionId);
    }

    /**
     * Disconnects the WhatsApp session for the given JSESSIONID.
     *
     * @param sessionId the JSESSIONID identifier
     */
    public void disconnectSession(String sessionId) {
        var session = sessions.get(sessionId);
        if (session != null) {
            session.disconnect();
        }
    }
}
