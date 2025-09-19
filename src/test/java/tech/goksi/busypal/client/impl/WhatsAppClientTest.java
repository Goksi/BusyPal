package tech.goksi.busypal.client.impl;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.goksi.busypal.client.WhatsAppClient;
import tech.goksi.busypal.exceptions.WhatsAppAvailabilityException;
import tech.goksi.busypal.exceptions.WhatsAppNotConnectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WhatsAppClientTest {

    @Mock
    private Whatsapp whatsapp;

    @Mock
    private Store store;

    @Mock
    private ChatMessageInfo messageInfo;

    private WhatsAppClient client;

    @BeforeEach
    void setUp() {
        client = new CobaltWhatsAppClientImpl(whatsapp);
        lenient().when(whatsapp.store()).thenReturn(store);
    }

    @Test
    void sendMessage_whenNotConnected_shouldThrowException() {
        when(whatsapp.isConnected()).thenReturn(false);

        assertThrows(WhatsAppNotConnectedException.class, () ->
                client.sendMessage("123456789@s.whatsapp.net", "Test message", null, null));

        verify(whatsapp).isConnected();
        verify(whatsapp, never()).sendChatMessage(any(), anyString());
    }

    @Test
    void sendMessage_withoutQuote_shouldSendRegularMessage() {
        when(whatsapp.isConnected()).thenReturn(true);
        when(whatsapp.sendChatMessage(any(), anyString()))
                .thenAnswer(inv -> CompletableFuture.completedFuture(messageInfo));

        String jid = "123456789@s.whatsapp.net";
        String message = "Test message";

        var result = client.sendMessage(jid, message, null, null);

        assertNotNull(result);
        verify(whatsapp).isConnected();
        verify(whatsapp).sendChatMessage(Jid.of(jid), message);
        verify(whatsapp, never()).store();
    }

    @Test
    void sendMessage_withQuoteFound_shouldSendQuotedMessage() {
        when(whatsapp.isConnected()).thenReturn(true);
        when(store.findMessageById((Jid) any(), anyString())).thenAnswer(inv -> Optional.of(messageInfo));
        when(whatsapp.sendChatMessage(any(), anyString(), any()))
                .thenAnswer(inv -> CompletableFuture.completedFuture(messageInfo));

        String jid = "123456789@s.whatsapp.net";
        String message = "Test message";

        var result = client.sendMessage(jid, message, jid, "quote123");

        assertNotNull(result);
        verify(whatsapp).isConnected();
        verify(store).findMessageById(Jid.of(jid), "quote123");
        verify(whatsapp).sendChatMessage(Jid.of(jid), message, messageInfo);
    }

    @Test
    void sendMessage_withQuoteNotFound_shouldSendRegularMessage() {
        when(whatsapp.isConnected()).thenReturn(true);
        when(store.findMessageById((Jid) any(), anyString())).thenReturn(Optional.empty());
        when(whatsapp.sendMessage(any(), anyString()))
                .thenAnswer(inv -> CompletableFuture.completedFuture(messageInfo));

        String jid = "123456789@s.whatsapp.net";
        String message = "Test message";

        var result = client.sendMessage(jid, message, jid, "nonexistent");

        assertNotNull(result);
        verify(whatsapp).isConnected();
        verify(store).findMessageById(Jid.of(jid), "nonexistent");
        verify(whatsapp).sendMessage(Jid.of(jid), message);
    }

    @Test
    void fallback_shouldThrowWhatsAppAvailabilityException() {
        Exception testException = new RuntimeException("Test exception");

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            var method = CobaltWhatsAppClientImpl.class.getDeclaredMethod("fallback", Exception.class);
            method.setAccessible(true);
            method.invoke(client, testException);
        });

        assertInstanceOf(WhatsAppAvailabilityException.class, exception.getCause());
    }
}
