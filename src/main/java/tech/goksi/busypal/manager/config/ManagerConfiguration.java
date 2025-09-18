package tech.goksi.busypal.manager.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;
import tech.goksi.busypal.manager.WhatsAppManager;
import tech.goksi.busypal.manager.impl.WhatsAppManageImpl;
import tech.goksi.busypal.orchestrator.WhatsAppSessionOrchestrator;

@Configuration
public class ManagerConfiguration {

    @Bean
    @SessionScope
    public WhatsAppManager whatsAppManager(HttpSession session, WhatsAppSessionOrchestrator orchestrator) {
        var sessionId = session.getId();
        var whatsAppSession = orchestrator.getSession(sessionId);
        return new WhatsAppManageImpl(whatsAppSession);
    }
}
