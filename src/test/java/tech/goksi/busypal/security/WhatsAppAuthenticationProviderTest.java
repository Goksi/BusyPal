package tech.goksi.busypal.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import tech.goksi.busypal.BusyPalProperties;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

class WhatsAppAuthenticationProviderTest {

  private WhatsAppManager manager;
  private BusyPalProperties properties;
  private AuthenticationProvider authenticationProvider;

  @BeforeEach
  void setup() {
    manager = mock(WhatsAppManager.class);
    properties = new BusyPalProperties();
    properties.setAllowedPhoneNumbers(new HashSet<>());
    authenticationProvider = new WhatsAppAuthenticationProvider(manager, properties);
  }

  @Test
  void support_shouldReturnFalseIfNotWaToken() {
    assertFalse(authenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
  }

  @Test
  void support_shouldReturnTrueWaToken() {
    assertTrue(authenticationProvider.supports(WhatsAppAuthenticationToken.class));
  }

  @Test
  void authenticate_shouldThrowIfNotConnectedToWa() {
    String sessionId = "testId";
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    when(manager.isConnected(eq(sessionId))).thenReturn(false);
    assertThrows(BadCredentialsException.class, () -> {
      authenticationProvider.authenticate(token);
    });
  }

  @Test
  void authenticate_shouldThrowIfCantGetDetails() {
    String sessionId = "testId";
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    when(manager.isConnected(eq(sessionId))).thenReturn(true);
    assertThrows(InsufficientAuthenticationException.class, () -> {
      authenticationProvider.authenticate(token);
    });
  }

  @Test
  void authenticate_shouldThrowIfPhoneNotAddedToProperties() {
    String sessionId = "testId";
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    when(manager.isConnected(eq(sessionId))).thenReturn(true);
    properties.setAllowedPhoneNumbers(Set.of("1234"));
    WhatsAppPrincipal principal = new WhatsAppPrincipal("45678");
    when(manager.getDetails(eq(sessionId))).thenReturn(principal);
    assertThrows(InsufficientAuthenticationException.class, () -> {
      authenticationProvider.authenticate(token);
    });
  }

  @Test
  void authenticate_shouldSuccessIfPhoneAddedToProperties() {
    String sessionId = "testId";
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    when(manager.isConnected(eq(sessionId))).thenReturn(true);
    properties.setAllowedPhoneNumbers(Set.of("1234"));
    WhatsAppPrincipal principal = new WhatsAppPrincipal("1234");
    when(manager.getDetails(eq(sessionId))).thenReturn(principal);
    Authentication expectedToken = new WhatsAppAuthenticationToken(sessionId, principal);
    Authentication actualToken = authenticationProvider.authenticate(token);
    assertEquals(expectedToken, actualToken);
  }

  @Test
  void authenticate_shouldSuccessIfWildcardAddedToProperties() {
    String sessionId = "testId";
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    when(manager.isConnected(eq(sessionId))).thenReturn(true);
    properties.setAllowedPhoneNumbers(Set.of("*"));
    WhatsAppPrincipal principal = new WhatsAppPrincipal("1234");
    when(manager.getDetails(eq(sessionId))).thenReturn(principal);
    Authentication expectedToken = new WhatsAppAuthenticationToken(sessionId, principal);
    Authentication actualToken = authenticationProvider.authenticate(token);
    assertEquals(expectedToken, actualToken);
  }
}
