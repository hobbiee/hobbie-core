package com.br.hobbie.shared.core.utils;

import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

class CloneUtilsTest {
    static final BigDecimal LATITUDE = BigDecimal.ONE;
    static final BigDecimal LONGITUDE = BigDecimal.TEN;

    static final Set<Tag> TAGS = Set.of(new Tag("tag1"), new Tag("tag2"));

    static final LocalDate DATE = LocalDate.of(1999, 1, 1);

    static Player player;

    @BeforeAll
    static void setUp() {
        player = new Player("name", "avatar", BigDecimal.ONE, BigDecimal.TEN, LocalDate.of(1999, 1, 1));
    }

    @Test
    @DisplayName("Should clone an object successfully")
    void testClone_WhenSuccessFull() {
        // GIVEN - player

        // WHEN
        Player clone = CloneUtils.clone(Player.class, player);

        // THEN
        Assertions.assertNotNull(clone);
        Assertions.assertEquals(player.getName(), clone.getName());
        Assertions.assertNotEquals(player, clone);
    }

    @Test
    @DisplayName("Should clone an object with a nested object successfully")
    void testClone_WhenSuccessFullWithNestedObject() {
        // GIVEN - player
        player.addInterests(new Tag("tag1"), new Tag("tag2"));

        // WHEN
        Tag clone = CloneUtils.clone(Tag.class, player.getInterests().iterator().next());

        // THEN

        Assertions.assertNotNull(clone);
        Assertions.assertEquals(player.getInterests().iterator().next().getName(), clone.getName());
    }

    @Test
    @DisplayName("Should not to be able to clone an object without a default constructor")
    void testClone_WhenObjectWithoutDefaultConstructor() {
        // GIVEN
        var unsupportedClass = new ClazzWithoutDefaultConstructor("name");

        // WHEN - THEN
        Assertions.assertThrows(RuntimeException.class, () -> CloneUtils.clone(ClazzWithoutDefaultConstructor.class, unsupportedClass));


    }

    @Test
    @DisplayName("Should not to be able to clone an object without attributes")
    void testClone_WhenObjectWithoutAttributes() {
        // GIVEN
        var unsupportedClass = new ClazzWithoutAttributes();

        // WHEN - THEN
        Assertions.assertThrows(RuntimeException.class, () -> CloneUtils.clone(ClazzWithoutAttributes.class, unsupportedClass));
    }

    class ClazzWithoutDefaultConstructor {
        private String name;

        public ClazzWithoutDefaultConstructor(String name) {
            this.name = name;
        }
    }

    class ClazzWithoutAttributes {
        private String test;

        public ClazzWithoutAttributes() {
        }

        @Override
        public String toString() {
            return "ClazzWithoutAttributes{}";
        }
    }
}