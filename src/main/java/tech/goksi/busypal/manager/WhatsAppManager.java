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
   * Sends a message to a specified WhatsApp JID (user or group) within the context of a session.
   *
   * @param sessionId         the unique identifier for the user's busy_session
   * @param jid               the WhatsApp JID (user or group identifier) to send the message to
   * @param message           the content of the message to send
   * @param quoteMessageInfo  information about a message to quote/reply to, or null if not quoting
   * @return a CompletableFuture containing the sent WhatsAppMessageInfo upon completion
   */
  CompletableFuture<WhatsAppMessageInfo> sendMessage(
      String sessionId,
      String jid,
      String message,
      WhatsAppMessageInfo quoteMessageInfo
  );

  /**
   * Creates a new WhatsApp session for the specified session ID.
   *
   * @param sessionId the unique identifier for the new session
   */
  void createSession(String sessionId);

  /**
   * Removes the WhatsApp session associated with the given session ID.
   *
   * @param sessionId the identifier of the session to remove
   */
  void removeSession(String sessionId);

  /**
   * Migrates a WhatsApp session from an old session ID to a new one.
   *
   * <p>Used for session fixation attack protection by transferring session state.
   *
   * @param oldSessionId the original session identifier
   * @param newSessionId the new session identifier
   */
  void migrateSession(String oldSessionId, String newSessionId);

  /**
   * Checks if the WhatsApp session with the specified ID is currently connected.
   *
   * @param sessionId the session identifier to check
   * @return true if the session is connected, false otherwise
   */
  boolean isConnected(String sessionId);

  /**
   * Checks if a WhatsApp session with the specified ID is currently being managed.
   *
   * @param sessionId the session identifier to check
   * @return true if a session with the given ID exists and is being managed, false otherwise
   */
  boolean isManagingSession(String sessionId);

  /**
   * Retrieves WhatsApp principal details for the given session ID.
   *
   * @param sessionId the session identifier
   * @return the WhatsAppPrincipal associated with the session, or null if not found
   */
  WhatsAppPrincipal getDetails(String sessionId);
}
