package com.br.hobbie.modules.event.application;

import org.springframework.stereotype.Component;

@Component
@FunctionalInterface
public interface RequestExpiration {

    void execute();
}
