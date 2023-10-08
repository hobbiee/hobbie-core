package com.br.hobbie.modules.player.domain.repositories;

import com.br.hobbie.modules.player.domain.entities.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    boolean existsByName(String name);

    Collection<Tag> findByNameIgnoreCaseIn(Collection<String> name);
}
