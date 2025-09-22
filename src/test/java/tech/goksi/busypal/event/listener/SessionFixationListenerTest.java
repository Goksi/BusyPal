package tech.goksi.busypal.event.listener;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tech.goksi.busypal.manager.WhatsAppManager;

@SpringBootTest
class SessionFixationListenerTest {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  @MockitoBean
  private WhatsAppManager manager;

  @Test
  void onApplicationEvent_shouldMigrateSession() {
    var authentication = mock(Authentication.class);
    applicationEventPublisher.publishEvent(new SessionFixationProtectionEvent(
        authentication,
        "oldId",
        "newId")
    );
    verify(manager).migrateSession(eq("oldId"), eq("newId"));
  }
}
