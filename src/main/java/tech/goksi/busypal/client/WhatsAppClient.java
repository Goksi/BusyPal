package tech.goksi.busypal.client;

import it.auties.whatsapp.api.Whatsapp;
import java.util.concurrent.CompletableFuture;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;

public interface WhatsAppClient {

  /**
   * Sends a WhatsApp message using the specified session and recipient JID.
   * <p>
   * Optionally quotes a previous message by providing its JID and message ID.
   *
   * @param session the WhatsApp session to use for sending
   * @param jid the recipient's WhatsApp JID
   * @param message the message content to send
   * @param quotedMessageJid the JID of the quoted message (nullable)
   * @param quotedMessageId the ID of the quoted message (nullable)
   * @return a CompletableFuture containing the message info after sending
   */
  CompletableFuture<WhatsAppMessageInfo> sendMessage(
      Whatsapp session,
      String jid,
      String message,
      String quotedMessageJid,
      String quotedMessageId
  );

  /**
   * Sends a WhatsApp message using the specified session and recipient JID.
   * <p>
   * This overload does not quote any previous message.
   *
   * @param session the WhatsApp session to use for sending
   * @param jid the recipient's WhatsApp JID
   * @param message the message content to send
   * @return a CompletableFuture containing the message info after sending
   */
  default CompletableFuture<WhatsAppMessageInfo> sendMessage(Whatsapp session, String jid,
      String message) {
    return sendMessage(session, jid, message, null, null);
  }
}
