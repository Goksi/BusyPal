package tech.goksi.busypal.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tech.goksi.busypal.BusyPalEndpoint;

public class WhatsAppAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final RequestMatcher LOGIN_HANDLER_PATH = PathPatternRequestMatcher.withDefaults()
      .matcher(HttpMethod.POST, BusyPalEndpoint.LOGIN);

  public WhatsAppAuthenticationFilter() {
    super(LOGIN_HANDLER_PATH);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    if (!"POST".equals(request.getMethod())) {
      throw new AuthenticationServiceException("Unsupported login method: " + request.getMethod());
    }
    HttpSession session = request.getSession(false);
    if (session == null) {
      throw new AuthenticationCredentialsNotFoundException(
          "Session not found in authentication request");
    }
    String sessionId = session.getId();
    Authentication token = new WhatsAppAuthenticationToken(sessionId);
    return this.getAuthenticationManager().authenticate(token);
  }
}
