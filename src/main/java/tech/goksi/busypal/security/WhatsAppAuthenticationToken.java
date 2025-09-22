package tech.goksi.busypal.security;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

public class WhatsAppAuthenticationToken extends AbstractAuthenticationToken
    implements Serializable {

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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    WhatsAppAuthenticationToken that = (WhatsAppAuthenticationToken) o;
    return Objects.equals(sessionId, that.sessionId) && Objects.equals(principal,
        that.principal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), sessionId, principal);
  }
}
