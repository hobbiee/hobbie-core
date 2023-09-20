package com.br.hobbie.modules.player.domain.repositories;

import com.br.hobbie.modules.player.domain.entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
