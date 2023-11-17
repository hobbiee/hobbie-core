package com.br.hobbie.shared.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "hobbie-addons")
public class HobbieAuthenticationProperties {
    private IssuerProperties[] issuers = {};

    public record IssuerProperties(String uri) {
    }
}
