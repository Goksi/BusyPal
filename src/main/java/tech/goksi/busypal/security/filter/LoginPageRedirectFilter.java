package tech.goksi.busypal.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import tech.goksi.busypal.BusyPalEndpoint;

/**
 * Servlet filter that redirects authenticated users away from the login page.
 *
 * <p>If an authenticated user accesses the login endpoint, this filter automatically
 * redirects them to the main index page, preventing unnecessary login attempts.
 * Integrates with Spring Security's authentication context.
 *
 * @see org.springframework.web.filter.GenericFilterBean
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
@Component
public class LoginPageRedirectFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest servletRequest = (HttpServletRequest) request;
    HttpServletResponse servletResponse = (HttpServletResponse) response;
    if (servletRequest.getRequestURI().equals(BusyPalEndpoint.LOGIN)
        && !response.isCommitted()
        && isAuthenticated()
    ) {
      servletResponse.sendRedirect(BusyPalEndpoint.INDEX);
    }
    chain.doFilter(request, response);
  }

  private boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      return false;
    }
    return authentication.isAuthenticated();
  }
}
