package tech.goksi.busypal.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.goksi.busypal.manager.BusyManager;
import tech.goksi.busypal.manager.WhatsAppManager;

@Component
public class WhatsAppSessionCheckFilter extends OncePerRequestFilter {

  private final WhatsAppManager whatsAppManager;
  private final BusyManager busyManager;

  public WhatsAppSessionCheckFilter(WhatsAppManager whatsAppManager, BusyManager busyManager) {
    this.whatsAppManager = whatsAppManager;
    this.busyManager = busyManager;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    var session = request.getSession(false);
    if (session == null) {
      filterChain.doFilter(request, response);
      return;
    }
    var sessionId = session.getId();
    if (whatsAppManager.isManagingSession(sessionId) && !whatsAppManager.isConnected(sessionId)) {
      session.invalidate();
      whatsAppManager.removeSession(sessionId);
      busyManager.setBusy(sessionId, false);
    }
    filterChain.doFilter(request, response);
  }
}
