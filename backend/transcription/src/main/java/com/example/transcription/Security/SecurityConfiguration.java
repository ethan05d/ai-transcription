package com.example.transcription.Security;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfiguration(CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    // Security Chain to pass through with oauth2
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         return http.csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(auth -> {
                     auth.anyRequest().authenticated();
                 })
                 .oauth2Login(oauth -> oauth
                         .successHandler(authenticationSuccessHandler))
                 .logout(logout -> logout.logoutSuccessUrl("http://localhost:3000?logout=true"))
                 .build();

    }

    @Bean
    public ServletContextInitializer cookieSecurityInitializer() {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setSecure(true);  // Only send cookies over HTTPS
            servletContext.getSessionCookieConfig().setHttpOnly(true);  // Prevent JS access to cookies
        };
    }


}
