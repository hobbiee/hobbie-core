package com.br.hobbie.modules.event.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.event.domain.entities.JoinRequest;
import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.shared.factory.PlayerEventTestFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(SpringExtension.class)
class ExpireJoinRequestsTest {

    private static Player playerAdmin;

    private static Player playerParticipant;

    private static Event event;
    private JoinRequest joinRequest;


    @Autowired
    private ExpireJoinRequests expireJoinRequests;


    @Autowired
    private EntityManager manager;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        playerAdmin = playerRepository.save(PlayerEventTestFactory.createPlayer());
        playerParticipant = playerRepository.save(PlayerEventTestFactory.createParticipant());
        event = eventRepository.save(PlayerEventTestFactory.createStartedEvent(playerAdmin));
        joinRequest = new JoinRequest(playerParticipant, event);
        manager.persist(joinRequest);
    }

    @AfterEach
    void tearDown() {
        manager.remove(joinRequest);
        manager.remove(event);
        manager.remove(playerParticipant);
        manager.remove(playerAdmin);
    }

    @Test
    @DisplayName("Should expire join requests that are pending when the event already started")
    @Transactional
    void expire_joinRequests_whenEventAlreadyStarted() {
        // WHEN
        expireJoinRequests.execute();

        // THEN
        manager.refresh(joinRequest);
        var result = manager.find(JoinRequest.class, joinRequest.getId());
        Assertions.assertTrue(result.isExpired());
    }
}