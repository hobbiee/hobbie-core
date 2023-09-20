package com.br.hobbie.modules.player.domain.entities;

import java.util.Arrays;

public enum Category {
    SOCCER,
    BASKETBALL,
    VOLLEYBALL,
    TENNIS,
    BEACH_TENNIS,
    BEACH_SOCCER,
    BEACH_VOLLEYBALL,
    ;


    static Category buildFrom(String value) {
        // if the value is not found, it will return a new Category with the value in uppercase

        return Arrays.stream(Category.values())
                .filter(category -> category.name().equalsIgnoreCase(value))
                .findAny()
                .orElse(Category.valueOf(value.toUpperCase()));
    }

    static void buildFrom(String... values) {
        Arrays.stream(values)
                .forEach(Category::buildFrom);
    }
}
