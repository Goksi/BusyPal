package tech.goksi.busypal.event.listener;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tech.goksi.busypal.manager.WhatsAppManager;

@SpringBootTest
class SessionInvalidateListenerTest {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  @MockitoBean
  private WhatsAppManager manager;

  @Test
  void onApplicationEvent_shouldRemoveSession() {
    var session = new MockHttpSession(null, "testId");
    applicationEventPublisher.publishEvent(new HttpSessionDestroyedEvent(session));
    verify(manager).removeSession(eq("testId"));
  }
}
