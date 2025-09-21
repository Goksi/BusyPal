package tech.goksi.busypal.security.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tech.goksi.busypal.security.filter.WhatsAppAuthenticationFilter;

public class WhatsAppAuthenticationConfigurer<H extends HttpSecurityBuilder<H>> extends
    AbstractNonOrderedAuthenticationFilterConfigurer<H, WhatsAppAuthenticationConfigurer<H>, WhatsAppAuthenticationFilter> {


  public WhatsAppAuthenticationConfigurer() {
    super(new WhatsAppAuthenticationFilter(), null);
  }

  @Override
  public WhatsAppAuthenticationConfigurer<H> loginPage(String loginPage) {
    return super.loginPage(loginPage);
  }

  public WhatsAppAuthenticationConfigurer<H> successForwardUrl(String forwardUrl) {
    successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
    return this;
  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return PathPatternRequestMatcher.withDefaults()
        .matcher(HttpMethod.POST, loginProcessingUrl);
  }
}
