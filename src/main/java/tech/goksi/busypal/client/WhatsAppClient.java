package tech.goksi.busypal.client;

import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;

import java.util.concurrent.CompletableFuture;

public interface WhatsAppClient {

    CompletableFuture<WhatsAppMessageInfo> sendMessage(
            String jid,
            String message,
            String quotedMessageJid,
            String quotedMessageId
    );

    default CompletableFuture<WhatsAppMessageInfo> sendMessage(String jid, String message) {
        return sendMessage(jid, message, null, null);
    }
}
