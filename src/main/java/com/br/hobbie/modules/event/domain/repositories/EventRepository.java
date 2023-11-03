package com.br.hobbie.modules.event.domain.repositories;

import com.br.hobbie.modules.event.domain.entities.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("""
                select e from Event e join e.categories c where c.id in (
                    select t.id from Player p join p.interests t where p.id = :playerId
                ) and e.active = true and e.admin.id != :playerId and e.startDate > current_date
                and e.id not in (
                    select e.id from Event e join e.participants p where p.id = :playerId
                ) and e.id not in (
                    select e.id from Event e join e.requests r where r.player.id = :playerId
                )
            """)
    Collection<Event> findAllMatchingWithInterests(Long playerId);

    @Query("""
                select e from Event e join e.categories c where c.name in :categoriesNames and e.active = true and e.admin.id != :playerId
            """)
    Collection<Event> findAllByCategoriesNamesContainingIgnoreCase(Collection<String> categoriesNames, Long playerId);
}
