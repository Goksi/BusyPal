package tech.goksi.busypal.manager;

import java.util.concurrent.CompletableFuture;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

/**
 * Interface for managing WhatsApp messaging operations.
 *
 * <p>This manager is designed to be unique per user, ensuring that each user
 * has a dedicated instance
 * for handling their WhatsApp messaging actions.
 */
public interface WhatsAppManager {

  /**
   * Sends a message to the specified WhatsApp JID.
   *
   * @param jid              the WhatsApp JID (user or group identifier)
   * @param message          the message content to send
   * @param quoteMessageInfo information about the quoted message, if any
   * @return a CompletableFuture containing the sent MessageInfo
   */
  CompletableFuture<WhatsAppMessageInfo> sendMessage(String jid, String message,
      WhatsAppMessageInfo quoteMessageInfo);

  void createSession(String sessionId);

  void removeSession(String sessionId);

  void migrateSession(String oldSessionId, String newSessionId);

  boolean isConnected(String sessionId);

  WhatsAppPrincipal getDetails(String sessionId);
}
