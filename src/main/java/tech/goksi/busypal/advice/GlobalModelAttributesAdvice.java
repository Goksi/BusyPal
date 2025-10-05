package tech.goksi.busypal.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import tech.goksi.busypal.security.model.WhatsAppPrincipal;

@ControllerAdvice
public class GlobalModelAttributesAdvice {

  @ModelAttribute("currentPhone")
  public String phoneNumber(Authentication authentication) {
    if (authentication == null) {
      return null;
    }
    var principal = authentication.getPrincipal();
    if (principal instanceof WhatsAppPrincipal(String phoneNumber)) {
      return phoneNumber;
    }
    return null;
  }

  @ModelAttribute("sessionId")
  public String sessionId(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    return session != null ? session.getId() : "";
  }
}
