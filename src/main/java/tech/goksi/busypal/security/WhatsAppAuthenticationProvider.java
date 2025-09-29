package tech.goksi.busypal.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import tech.goksi.busypal.BusyPalProperties;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

/**
 * Authentication provider for WhatsApp-based authentication in Spring Security.
 *
 * <p>Validates WhatsApp sessions and principals,
 * ensuring only allowed phone numbers can authenticate.
 *
 * <p>Integrates with WhatsAppManager and
 * BusyPalProperties for session and configuration management.
 *
 * @see org.springframework.security.authentication.AuthenticationProvider
 * @see tech.goksi.busypal.security.model.WhatsAppPrincipal
 */
@Component
public class WhatsAppAuthenticationProvider implements AuthenticationProvider {

  private final WhatsAppManager manager;
  private final BusyPalProperties properties;

  public WhatsAppAuthenticationProvider(WhatsAppManager manager, BusyPalProperties properties) {
    this.manager = manager;
    this.properties = properties;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String sessionId = authentication.getCredentials().toString();
    if (!manager.isConnected(sessionId)) {
      throw new BadCredentialsException("Current session is not authenticated with whatsapp");
    }
    WhatsAppPrincipal principal = manager.getDetails(sessionId);
    if (principal == null) {
      manager.removeSession(sessionId);
      throw new InsufficientAuthenticationException(
          "Unable to pull whatsapp principal out of session");
    }

    if (!properties.getAllowedPhoneNumbers().contains("*") && !properties.getAllowedPhoneNumbers()
        .contains(principal.phoneNumber())) {
      manager.removeSession(sessionId);
      throw new InsufficientAuthenticationException(
          "Principals phone number is not listed as allowed");
    }
    return new WhatsAppAuthenticationToken(sessionId, principal);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return WhatsAppAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
