package tech.goksi.busypal.security.configurer;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

public abstract class AbstractNonOrderedAuthenticationFilterConfigurer<B extends HttpSecurityBuilder<B>, T extends AbstractNonOrderedAuthenticationFilterConfigurer<B, T, F>, F extends AbstractAuthenticationProcessingFilter>
    extends AbstractHttpConfigurer<T, B> {

  private F authFilter;

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  private SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

  private AuthenticationSuccessHandler successHandler = this.defaultSuccessHandler;

  private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;

  private boolean customLoginPage;

  private String loginPage;

  private String loginProcessingUrl;

  private AuthenticationFailureHandler failureHandler;

  private String failureUrl;


  protected AbstractNonOrderedAuthenticationFilterConfigurer() {
    setLoginPage("/login");
  }

  protected AbstractNonOrderedAuthenticationFilterConfigurer(F authenticationFilter,
      String defaultLoginProcessingUrl) {
    this();
    this.authFilter = authenticationFilter;
    if (defaultLoginProcessingUrl != null) {
      loginProcessingUrl(defaultLoginProcessingUrl);
    }
  }


  public final T defaultSuccessUrl(String defaultSuccessUrl) {
    return defaultSuccessUrl(defaultSuccessUrl, false);
  }


  public final T defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysUse) {
    SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
    handler.setDefaultTargetUrl(defaultSuccessUrl);
    handler.setAlwaysUseDefaultTargetUrl(alwaysUse);
    this.defaultSuccessHandler = handler;
    return successHandler(handler);
  }


  public T loginProcessingUrl(String loginProcessingUrl) {
    this.loginProcessingUrl = loginProcessingUrl;
    this.authFilter.setRequiresAuthenticationRequestMatcher(
        createLoginProcessingUrlMatcher(loginProcessingUrl));
    return getSelf();
  }

  public T securityContextRepository(SecurityContextRepository securityContextRepository) {
    this.authFilter.setSecurityContextRepository(securityContextRepository);
    return getSelf();
  }

  protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

  public final T authenticationDetailsSource(
      AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
    this.authenticationDetailsSource = authenticationDetailsSource;
    return getSelf();
  }

  public final T successHandler(AuthenticationSuccessHandler successHandler) {
    this.successHandler = successHandler;
    return getSelf();
  }

  public final T failureUrl(String authenticationFailureUrl) {
    T result = failureHandler(new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
    this.failureUrl = authenticationFailureUrl;
    return result;
  }

  public final T failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
    this.failureUrl = null;
    this.failureHandler = authenticationFailureHandler;
    return getSelf();
  }

  @Override
  public void init(B http) throws Exception {
    updateAuthenticationDefaults();
    registerDefaultAuthenticationEntryPoint(http);
  }

  @SuppressWarnings("unchecked")
  protected final void registerDefaultAuthenticationEntryPoint(B http) {
    registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);
  }

  @SuppressWarnings("unchecked")
  protected final void registerAuthenticationEntryPoint(B http,
      AuthenticationEntryPoint authenticationEntryPoint) {
    ExceptionHandlingConfigurer<B> exceptionHandling = http.getConfigurer(
        ExceptionHandlingConfigurer.class);
    if (exceptionHandling == null) {
      return;
    }
    exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
        getAuthenticationEntryPointMatcher(http));
  }

  protected final RequestMatcher getAuthenticationEntryPointMatcher(B http) {
    ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(
        ContentNegotiationStrategy.class);
    if (contentNegotiationStrategy == null) {
      contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
    }
    MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
        MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
        MediaType.TEXT_PLAIN);
    mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
    RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
        new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
  }

  @Override
  public void configure(B http) throws Exception {
    PortMapper portMapper = http.getSharedObject(PortMapper.class);
    if (portMapper != null) {
      this.authenticationEntryPoint.setPortMapper(portMapper);
    }
    PortResolver portResolver = getBeanOrNull(http, PortResolver.class);
    if (portResolver != null) {
      this.authenticationEntryPoint.setPortResolver(portResolver);
    }
    RequestCache requestCache = http.getSharedObject(RequestCache.class);
    if (requestCache != null) {
      this.defaultSuccessHandler.setRequestCache(requestCache);
    }
    this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
    this.authFilter.setAuthenticationSuccessHandler(this.successHandler);
    this.authFilter.setAuthenticationFailureHandler(this.failureHandler);
    if (this.authenticationDetailsSource != null) {
      this.authFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
    }
    SessionAuthenticationStrategy sessionAuthenticationStrategy = http
        .getSharedObject(SessionAuthenticationStrategy.class);
    if (sessionAuthenticationStrategy != null) {
      this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
    }
    RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
    if (rememberMeServices != null) {
      this.authFilter.setRememberMeServices(rememberMeServices);
    }
    this.authFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
    F filter = postProcess(this.authFilter);
    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }

  protected T loginPage(String loginPage) {
    setLoginPage(loginPage);
    updateAuthenticationDefaults();
    this.customLoginPage = true;
    return getSelf();
  }

  protected final void setAuthenticationFilter(F authFilter) {
    this.authFilter = authFilter;
  }

  private void setLoginPage(String loginPage) {
    this.loginPage = loginPage;
    this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
  }

  protected final AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return this.authenticationEntryPoint;
  }

  protected final String getLoginProcessingUrl() {
    return this.loginProcessingUrl;
  }

  protected final String getFailureUrl() {
    return this.failureUrl;
  }

  protected final void updateAuthenticationDefaults() {
    if (this.loginProcessingUrl == null) {
      loginProcessingUrl(this.loginPage);
    }
    if (this.failureHandler == null) {
      failureUrl(this.loginPage + "?error");
    }
    LogoutConfigurer<B> logoutConfigurer = getBuilder().getConfigurer(LogoutConfigurer.class);
    logoutConfigurer.logoutSuccessUrl(this.loginPage + "?logout");
  }

  private <C> C getBeanOrNull(B http, Class<C> clazz) {
    ApplicationContext context = http.getSharedObject(ApplicationContext.class);
    if (context == null) {
      return null;
    }
    return context.getBeanProvider(clazz).getIfUnique();
  }

  @SuppressWarnings("unchecked")
  private T getSelf() {
    return (T) this;
  }
}
