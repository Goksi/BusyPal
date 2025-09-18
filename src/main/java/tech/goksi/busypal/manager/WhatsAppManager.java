package tech.goksi.busypal.manager;

import it.auties.whatsapp.model.info.MessageInfo;
import tech.goksi.busypal.model.QuoteMessageInfo;

import java.util.concurrent.CompletableFuture;

public interface WhatsAppManager {

    /**
     * Sends a message to the specified WhatsApp JID.
     *
     * @param jid the WhatsApp JID (user or group identifier)
     * @param message the message content to send
     * @param quoteMessageInfo information about the quoted message, if any
     * @return a CompletableFuture containing the sent MessageInfo
     */
    CompletableFuture<? extends MessageInfo<?>> sendMessage(String jid, String message, QuoteMessageInfo quoteMessageInfo);
}
