package com.br.hobbie.modules.player.application;

import com.br.hobbie.modules.player.domain.entities.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
class ResolveExistentTagsTest {

    private static final Tag soccer = new Tag("soccer");
    private static final Tag football = new Tag("football");
    private static final Tag basketball = new Tag("basketball");


    @Autowired
    private ResolveExistentTags resolveExistentTags;

    @Autowired
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        manager.persist(soccer);
        manager.persist(football);
        manager.persist(basketball);
    }


    @AfterEach
    void tearDown() {
        manager.remove(soccer);
        manager.remove(football);
        manager.remove(basketball);
    }

    @Test
    @DisplayName("Should return an existent tag when trying to add a tag that already exists")
    @Transactional
    void should_ReturnAnExistentTag_When_TryingToAddATagThatAlreadyExists() {
        // GIVEN
        var tags = new Tag[]{soccer, football, basketball};

        // WHEN
        var existentTags = resolveExistentTags.resolve(tags);

        // THEN
        Assertions.assertEquals(3, existentTags.size());
        Stream.of(tags).forEach(tag -> Assertions.assertNotNull(tag.getId()));
    }

   
}