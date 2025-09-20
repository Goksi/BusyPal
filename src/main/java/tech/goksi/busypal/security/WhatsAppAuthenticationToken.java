package tech.goksi.busypal.security;

import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

public class WhatsAppAuthenticationToken extends AbstractAuthenticationToken {

  private final String sessionId;
  private WhatsAppPrincipal principal;

  public WhatsAppAuthenticationToken(String sessionId) {
    super(Collections.emptyList());
    this.sessionId = sessionId;
    this.setAuthenticated(false);
  }

  public WhatsAppAuthenticationToken(String sessionId, WhatsAppPrincipal principal) {
    super(Collections.emptyList());
    this.sessionId = sessionId;
    this.principal = principal;
    this.setAuthenticated(true);
  }

  @Override
  public String getCredentials() {
    return sessionId;
  }

  @Override
  public WhatsAppPrincipal getPrincipal() {
    return principal;
  }
}
