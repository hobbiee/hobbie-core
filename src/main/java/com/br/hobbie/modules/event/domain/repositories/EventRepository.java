package com.br.hobbie.modules.event.domain.repositories;

import com.br.hobbie.modules.event.domain.entities.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}
