package com.br.hobbie.shared.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CloneUtils {

    public static <T> T clone(Class<T> clazz, T object) {
        // instantiate a class without calling its constructor
        T clone = null;
        try {
            var constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            clone = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not clone object");
        }

        // copy all fields from object to clone
        T finalClone = clone;

        if (clone == null || object == null) {
            return null;
        }

        Arrays.stream(object.getClass()
                        .getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(finalClone, field.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return clone;
    }
}
