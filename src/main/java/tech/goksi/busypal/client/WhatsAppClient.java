package tech.goksi.busypal.client;

import it.auties.whatsapp.api.Whatsapp;
import java.util.concurrent.CompletableFuture;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;

public interface WhatsAppClient {

  CompletableFuture<WhatsAppMessageInfo> sendMessage(
      Whatsapp session,
      String jid,
      String message,
      String quotedMessageJid,
      String quotedMessageId
  );

  default CompletableFuture<WhatsAppMessageInfo> sendMessage(Whatsapp session, String jid,
      String message) {
    return sendMessage(session, jid, message, null, null);
  }
}
