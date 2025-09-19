package tech.goksi.busypal.client.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.jid.JidProvider;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.client.WhatsAppClient;
import tech.goksi.busypal.exceptions.WhatsAppAvailabilityException;
import tech.goksi.busypal.exceptions.WhatsAppNotConnectedException;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;

import java.util.concurrent.CompletableFuture;

@Component
public class CobaltWhatsAppClientImpl implements WhatsAppClient {

    private static final String RESILIENCE_PROFILE = "whatsapp";

    @CircuitBreaker(name = RESILIENCE_PROFILE, fallbackMethod = "fallback")
    @RateLimiter(name = RESILIENCE_PROFILE)
    @Retry(name = RESILIENCE_PROFILE)
    @Override
    public CompletableFuture<WhatsAppMessageInfo> sendMessage(Whatsapp api, String jid, String message, String quotedMessageJid, String quotedMessageId) {
        checkConnection(api);
        JidProvider jidProvider = Jid.of(jid);
        if (quotedMessageJid == null || quotedMessageId == null) {
            return api.sendChatMessage(jidProvider, message).thenApply(this::mapMessageInfo);
        }
        var chatJid = Jid.of(quotedMessageJid);
        var quotedOptionalMessage = api.store().findMessageById(chatJid, quotedMessageId);
        if (quotedOptionalMessage.isPresent()) {
            var quotedMessage = quotedOptionalMessage.get();
            return api.sendChatMessage(jidProvider, message, quotedMessage).thenApply(this::mapMessageInfo);
        } else {
            return api.sendMessage(jidProvider, message).thenApply(this::mapMessageInfo);
        }
    }

    private void checkConnection(Whatsapp api) {
        if (!api.isConnected()) {
            throw new WhatsAppNotConnectedException("Can't call whatsapp if client is not connected !");
        }
    }

    private void fallback(Exception exception) {
        throw new WhatsAppAvailabilityException("Failed to call whatsapp api !", exception);
    }

    private WhatsAppMessageInfo mapMessageInfo(MessageInfo<?> messageInfo) {
        String messageId = messageInfo.id();
        String senderJid = messageInfo.senderJid().toString();
        return new WhatsAppMessageInfo(senderJid, messageId);
    }
}
