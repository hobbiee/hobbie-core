package com.br.hobbie.modules.events.domain.repositories;

import com.br.hobbie.modules.events.domain.entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
