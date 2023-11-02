package com.br.hobbie.modules.player.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.modules.player.domain.repositories.TagRepository;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
class ResolveExistentTagsTest {

    private static final Tag soccer = new Tag("soccer");
    private static final Tag football = new Tag("football");
    private static final Tag basketball = new Tag("basketball");
    private static Set<Event> events = Collections.emptySet();
    private static Player player;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    private ResolveExistentTags resolveExistentTags;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.saveAll(Set.of(soccer, football, basketball));
    }


    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
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

    @Test
    @DisplayName("Should extract the distinct interests in an event set")
    @Transactional
    void shouldExtract_DistinctInterests_In_An_EventSet() {
        // GIVEN
        player = PlayerEventTestFactory.createParticipant();
        Stream.of(soccer, football, basketball).forEach(player::addInterest);
        events = PlayerEventTestFactory.createManyEvents();
        var expected = events.stream()
                .filter(this::playerDoesNotHaveInterestOnCategories)
                .collect(Collectors.toSet())
                .size();


        // when
        var tags = resolveExistentTags.extractDistinctTags(events, player);
        Assertions.assertEquals(expected, tags.size());
    }

    private boolean playerDoesNotHaveInterestOnCategories(Event event) {
        return event.getCategories().stream().anyMatch(tag -> !player.hasInterestIn(tag));
    }
}