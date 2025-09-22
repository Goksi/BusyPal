package tech.goksi.busypal.security.filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import tech.goksi.busypal.BusyPalEndpoint;
import tech.goksi.busypal.security.WhatsAppAuthenticationToken;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

class LoginPageRedirectFilterTest {

  private LoginPageRedirectFilter filter;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private FilterChain chain;

  @BeforeEach
  void setup() {
    filter = new LoginPageRedirectFilter();
    request = new MockHttpServletRequest();
    response = spy(new MockHttpServletResponse());
    chain = spy(new MockFilterChain());
  }

  @Test
  void doFilter_shouldContinueChainIfNotLoginUri() throws ServletException, IOException {
    request.setRequestURI("/test");
    filter.doFilter(request, response, chain);
    verify(response, never()).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

  @Test
  void doFilter_shouldContinueChainIfResponseCommited() throws ServletException, IOException {
    request.setRequestURI(BusyPalEndpoint.LOGIN);
    response.setCommitted(true);
    filter.doFilter(request, response, chain);
    verify(response, never()).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

  @Test
  void doFilter_shouldContinueChainIfNoAuthentication() throws ServletException, IOException {
    request.setRequestURI(BusyPalEndpoint.LOGIN);
    response.setCommitted(false);
    SecurityContextHolder.setContext(new SecurityContextImpl());
    filter.doFilter(request, response, chain);
    verify(response, never()).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

  @Test
  void doFilter_shouldContinueChainIfAnonymousAuthentication()
      throws ServletException, IOException {
    request.setRequestURI(BusyPalEndpoint.LOGIN);
    response.setCommitted(false);
    SecurityContextHolder.setContext(new SecurityContextImpl(mock(AnonymousAuthenticationToken.class)));
    filter.doFilter(request, response, chain);
    verify(response, never()).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

  @Test
  void doFilter_shouldContinueChainIfUnauthenticated()
      throws ServletException, IOException {
    request.setRequestURI(BusyPalEndpoint.LOGIN);
    response.setCommitted(false);
    var token = new WhatsAppAuthenticationToken("session");
    SecurityContextHolder.setContext(new SecurityContextImpl(token));
    filter.doFilter(request, response, chain);
    verify(response, never()).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

  @Test
  void doFilter_shouldContinueChainAndRedirectIfAuthenticated()
      throws ServletException, IOException {
    request.setRequestURI(BusyPalEndpoint.LOGIN);
    response.setCommitted(false);
    var token = new WhatsAppAuthenticationToken("session", new WhatsAppPrincipal("phone"));
    SecurityContextHolder.setContext(new SecurityContextImpl(token));
    filter.doFilter(request, response, chain);
    verify(response).sendRedirect(anyString());
    verify(chain).doFilter(eq(request), eq(response));
  }

}
