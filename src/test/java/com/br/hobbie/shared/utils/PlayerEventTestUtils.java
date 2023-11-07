package com.br.hobbie.shared.utils;

import com.br.hobbie.modules.player.domain.entities.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

public class PlayerEventTestUtils {
    public static <T> T extractSomeField(Class<?> clazz, String fieldName, Object instance, Class<T> type) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return type.cast(field.get(instance));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assignId(Player player, Long value) {
        Arrays.stream(player.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            if (Objects.equals(field.getName(), "id")) {
                try {
                    field.set(player, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static File buildFile() {
        // get the directory path using System
        var path = System.getProperty("user.dir") + "/src/test/resources/files";
        var file = new File(path + "/text.txt");
        if (!file.exists()) {
            // creates the file
            try {
                Files.createDirectories(file.getParentFile().toPath());
                Files.createFile(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }
}
