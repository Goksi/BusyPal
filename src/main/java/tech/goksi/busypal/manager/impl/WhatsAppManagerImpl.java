package tech.goksi.busypal.manager.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.jid.JidProvider;
import tech.goksi.busypal.exceptions.WhatsAppAvailabilityException;
import tech.goksi.busypal.exceptions.WhatsAppNotConnectedException;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.model.QuoteMessageInfo;

import java.util.concurrent.CompletableFuture;

public class WhatsAppManagerImpl implements WhatsAppManager {

    private static final String RESILIENCE_PROFILE = "whatsapp";

    private final Whatsapp whatsapp;

    public WhatsAppManagerImpl(Whatsapp whatsapp) {
        this.whatsapp = whatsapp;
    }

    @CircuitBreaker(name = RESILIENCE_PROFILE, fallbackMethod = "fallback")
    @RateLimiter(name = RESILIENCE_PROFILE)
    @Retry(name = RESILIENCE_PROFILE)
    @Override
    public CompletableFuture<? extends MessageInfo<?>> sendMessage(
            String jid,
            String message,
            QuoteMessageInfo quoteMessageInfo
    ) {
        checkConnection();
        JidProvider jidProvider = Jid.of(jid);
        if (quoteMessageInfo == null) {
            return whatsapp.sendChatMessage(jidProvider, message);
        }
        var chatJid = Jid.of(quoteMessageInfo.jid());
        var quotedMessageOptional = whatsapp.store().findMessageById(chatJid, quoteMessageInfo.id());
        if (quotedMessageOptional.isPresent()) {
            var quotedMessage = quotedMessageOptional.get();
            return whatsapp.sendChatMessage(jidProvider, message, quotedMessage);
        } else {
            return whatsapp.sendMessage(jidProvider, message);
        }

    }

    private void fallback(Exception exception) {
        throw new WhatsAppAvailabilityException("Failed to call whatsapp api !", exception);
    }

    private void checkConnection() {
        if (!whatsapp.isConnected()) {
            throw new WhatsAppNotConnectedException("Can't call whatsapp if client is not connected !");
        }
    }
}
