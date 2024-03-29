package com.skilland.game.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilland.game.demo.config.security.filters.JWTAuthenticationFilter;
import com.skilland.game.demo.config.security.filters.JWTAuthorizationFilter;
import com.skilland.game.demo.config.security.properties.SecurityProperties;
import com.skilland.game.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final SecurityProperties securityProperties;

    private final ObjectMapper objectMapper;


    public SecurityConfig(UserService userService,
                          PasswordEncoder passwordEncoder,
                          SecurityProperties securityProperties,
                          ObjectMapper objectMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    public void init() {
        setupDefaultAdmins();
    }*/

    /*private void setupDefaultAdmins() {
        List<SaveUserRequest> requests = securityProperties.getAdmins().entrySet().stream()
                .map(entry -> new SaveUserRequest(
                        entry.getValue().getEmail(),
                        entry.getValue().getPassword(),
                        entry.getKey()))
                .peek(admin -> log.info("Default admin found: {} <{}>", admin.getFirstName(), admin.getEmail()))
                .collect(Collectors.toList());
        userService.mergeAdmins(requests);
    }*/

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // open static resources
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // open swagger-
                .antMatchers("/").permitAll()
                .antMatchers("/game/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // allow user registration
                .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                // by default, require authentication
                .antMatchers(HttpMethod.POST, "/api/v1/users/admins").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/categories/add").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                // login filter
                .addFilter(jwtAuthenticationFilter())
                // jwt-verification filter
                .addFilter(jwtAuthorizationFilter())
                // for unauthorized requests return 401
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                // allow cross-origin requests for all endpoints
                .cors().configurationSource(corsConfigurationSource())
                .and()
                // we don't need CSRF protection when we use JWT
                // (if you can steal Authorization header value, your can steal X-CSRF as well)
                .csrf().disable()
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    private CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    private JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        var filter = new JWTAuthenticationFilter(authenticationManager(), objectMapper);
        filter.setFilterProcessesUrl("/api/v1/token");
        return filter;
    }

    private JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager(), securityProperties.getJwt());
    }


}
