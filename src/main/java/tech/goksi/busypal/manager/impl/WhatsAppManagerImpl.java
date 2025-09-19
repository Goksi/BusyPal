package tech.goksi.busypal.manager.impl;

import tech.goksi.busypal.client.WhatsAppClient;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;

import java.util.concurrent.CompletableFuture;

public class WhatsAppManagerImpl implements WhatsAppManager {

    private final WhatsAppClient client;

    public WhatsAppManagerImpl(WhatsAppClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<WhatsAppMessageInfo> sendMessage(
            String jid,
            String message,
            WhatsAppMessageInfo quoteMessageInfo
    ) {
        return client.sendMessage(jid, message, quoteMessageInfo.jid(), quoteMessageInfo.id());
    }

}
