package tech.goksi.busypal.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import tech.goksi.busypal.security.WhatsAppAuthenticationToken;

class WhatsAppAuthenticationFilterTest {

  private WhatsAppAuthenticationFilter filter;
  private AuthenticationManager manager;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setup() {
    filter = new WhatsAppAuthenticationFilter();
    manager = mock(AuthenticationManager.class);
    filter.setAuthenticationManager(manager);
    this.request = new MockHttpServletRequest();
    this.response = new MockHttpServletResponse();
  }

  @Test
  void attemptAuthentication_shouldThrowIfMethodNotPost() {
    request.setMethod("GET");
    assertThrows(AuthenticationServiceException.class, () -> {
      filter.attemptAuthentication(request, response);
    });
  }

  @Test
  void attemptAuthentication_shouldThrowIfSessionIsNull() {
    request.setMethod("POST");

    assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
      filter.attemptAuthentication(request, response);
    });
  }

  @Test
  void attemptAuthentication_shouldAuthenticateWithCorrectToken() {
    request.setMethod("POST");
    String testSessionId = "test_id";
    var session = new MockHttpSession(null, testSessionId);
    request.setSession(session);
    ArgumentCaptor<WhatsAppAuthenticationToken> captor = ArgumentCaptor.forClass(
        WhatsAppAuthenticationToken.class);
    filter.attemptAuthentication(request, response);
    verify(manager).authenticate(captor.capture());
    var token = captor.getValue();
    assertEquals(testSessionId, token.getCredentials());
  }
}
