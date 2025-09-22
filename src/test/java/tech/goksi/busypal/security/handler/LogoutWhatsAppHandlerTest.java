package tech.goksi.busypal.security.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import tech.goksi.busypal.manager.WhatsAppManager;

class LogoutWhatsAppHandlerTest {

  private WhatsAppManager manager;
  private LogoutWhatsAppHandler handler;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private Authentication authentication;

  @BeforeEach
  void setup() {
    manager = mock(WhatsAppManager.class);
    handler = new LogoutWhatsAppHandler(manager);
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    authentication = mock(Authentication.class);
  }

  @Test
  void logout_shouldNotRemoveWaSessionIfNoHttpSession() {
    handler.logout(request, response, authentication);
    verify(manager, never()).removeSession(anyString());
  }

  @Test
  void logout_shouldRemoveWaSession() {
    String testId = "testId";
    MockHttpSession session = new MockHttpSession(null, testId);
    request.setSession(session);
    handler.logout(request, response, authentication);
    verify(manager).removeSession(eq(testId));
  }
}
