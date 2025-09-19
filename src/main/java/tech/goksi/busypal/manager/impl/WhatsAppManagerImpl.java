package tech.goksi.busypal.manager.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import tech.goksi.busypal.client.WhatsAppClient;
import tech.goksi.busypal.exceptions.WhatsAppNotConnectedException;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;
import tech.goksi.busypal.orchestrator.WhatsAppSessionOrchestrator;

import java.util.concurrent.CompletableFuture;

@Service
public class WhatsAppManagerImpl implements WhatsAppManager {

    private final WhatsAppSessionOrchestrator sessionOrchestrator;
    private final WhatsAppClient client;

    public WhatsAppManagerImpl(WhatsAppSessionOrchestrator sessionOrchestrator, WhatsAppClient client) {
        this.sessionOrchestrator = sessionOrchestrator;
        this.client = client;
    }

    @Override
    public CompletableFuture<WhatsAppMessageInfo> sendMessage(
            String jid,
            String message,
            WhatsAppMessageInfo quoteMessageInfo
    ) {
        String currentSession = getCurrentJSessionId();
        var whatsAppSession = sessionOrchestrator.getSession(currentSession);
        if (whatsAppSession == null) {
            throw new WhatsAppNotConnectedException("Trying to call whatsapp manager without whatsapp session bound to busypal session !");
        }
        return client.sendMessage(whatsAppSession, jid, message, quoteMessageInfo.jid(), quoteMessageInfo.id());
    }

    private String getCurrentJSessionId() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getSessionId();
    }

}
