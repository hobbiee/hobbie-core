package com.br.hobbie.shared.infrastructure.security;

import com.br.hobbie.shared.config.HobbieAuthenticationProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationManagerResolver(authenticationManagerResolver));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/v1/api/auth/username-password").permitAll()
                .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }

    @Bean
    AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(
            HobbieAuthenticationProperties properties,
            JwtConverter jwtConverter
    ) {
        final Map<String, AuthenticationManager> authenticationManagers = Stream.of(properties.getIssuers())
                .collect(Collectors.toMap(
                        HobbieAuthenticationProperties.IssuerProperties::uri,
                        issuer -> authenticationProvider(issuer.uri(), jwtConverter)::authenticate
                ));

        return new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
    }

    JwtAuthenticationProvider authenticationProvider(String issuerUri,
                                                     JwtConverter converter) {
        var jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
        var provider = new JwtAuthenticationProvider(jwtDecoder);
        provider.setJwtAuthenticationConverter(converter);
        return provider;
    }
}
