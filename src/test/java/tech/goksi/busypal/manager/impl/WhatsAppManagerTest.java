package tech.goksi.busypal.manager.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.model.mobile.PhoneNumber;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import tech.goksi.busypal.client.WhatsAppClient;
import tech.goksi.busypal.exceptions.WhatsAppNotConnectedException;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.model.whatsapp.WhatsAppMessageInfo;
import tech.goksi.busypal.orchestrator.WhatsAppSessionOrchestrator;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

class WhatsAppManagerTest {

  private WhatsAppManager manager;
  private WhatsAppClient client;
  private WhatsAppSessionOrchestrator orchestrator;
  private Whatsapp api;

  @BeforeEach
  void setup() {
    client = mock(WhatsAppClient.class);
    orchestrator = mock(WhatsAppSessionOrchestrator.class);
    manager = new WhatsAppManagerImpl(orchestrator, client);
    var attributes = mock(RequestAttributes.class);
    when(attributes.getSessionId()).thenReturn("testSession");
    RequestContextHolder.setRequestAttributes(attributes);
    api = mock(Whatsapp.class);
  }

  @Test
  void sendMessage_shouldThrowIfNoWaSessionAttached() {
    when(orchestrator.getSession(eq("testSession"))).thenReturn(null);
    assertThrows(WhatsAppNotConnectedException.class, () -> {
      manager.sendMessage(null, null, null);
    });
  }

  @Test
  void sendMessage_shouldSendUnQuotedMessageIfNull() {
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    manager.sendMessage("testJid", "testMsg", null);
    verify(client).sendMessage(eq(api), eq("testJid"), eq("testMsg"));
  }

  @Test
  void sendMessage_shouldSendQuotedMessage() {
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    manager.sendMessage(
        "testJid",
        "testMsg",
        new WhatsAppMessageInfo("quotedJid", "quotedId")
    );
    verify(client)
        .sendMessage(eq(api), eq("testJid"), eq("testMsg"), eq("quotedJid"),
            eq("quotedId"));
  }

  @Test
  void createSession_shouldCallOrchestrator() {
    manager.createSession("test");
    verify(orchestrator).createNewSession(eq("test"));
  }

  @Test
  void removeSession_shouldCallOrchestrator() {
    manager.removeSession("test");
    verify(orchestrator).removeSession(eq("test"));
  }

  @Test
  void migrateSession_shouldCallOrchestrator() {
    manager.migrateSession("test", "test1");
    verify(orchestrator).migrateSession(eq("test"), eq("test1"));
  }

  @Test
  void isConnected_shouldReturnFalseIfNoWaSession() {
    when(orchestrator.getSession(eq("testSession"))).thenReturn(null);
    assertFalse(manager.isConnected("testSession"));
  }

  @Test
  void isConnected_shouldReturnFalseIfWaNotConnected() {
    when(api.isConnected()).thenReturn(false);
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    assertFalse(manager.isConnected("testSession"));
  }

  @Test
  void isConnected_shouldReturnTrueIfWaConnected() {
    when(api.isConnected()).thenReturn(true);
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    assertTrue(manager.isConnected("testSession"));
  }

  @Test
  void getDetails_shouldReturnNullIfWaNotAttached() {
    when(orchestrator.getSession(eq("testSession"))).thenReturn(null);
    WhatsAppPrincipal actual = manager.getDetails("testSession");
    assertNull(actual);
  }

  @Test
  void getDetails_shouldReturnNullIfNoPhoneNumber() {
    Store store = mock(Store.class);
    when(store.phoneNumber()).thenReturn(Optional.empty());
    when(api.store()).thenReturn(store);
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    WhatsAppPrincipal actual = manager.getDetails("testSession");
    assertNull(actual);
  }

  @Test
  void getDetails_shouldReturnPrincipal() {
    Store store = mock(Store.class);
    when(store.phoneNumber()).thenReturn(PhoneNumber.of(38123123112L));
    when(api.store()).thenReturn(store);
    when(orchestrator.getSession(eq("testSession"))).thenReturn(api);
    WhatsAppPrincipal actual = manager.getDetails("testSession");
    String actualPhone = actual.phoneNumber();
    String expectedPhone = "38123123112";
    assertEquals(expectedPhone, actualPhone);
  }
}
