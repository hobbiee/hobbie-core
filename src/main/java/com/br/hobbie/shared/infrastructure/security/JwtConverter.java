package com.br.hobbie.shared.infrastructure.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return new JwtAuthenticationToken(jwt, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        }
        var roles = realmAccess.get("roles");
        var authorities = roles.stream().map(r -> "ROLE_" + r).map(SimpleGrantedAuthority::new).toList();
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
