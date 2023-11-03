package com.br.hobbie.shared.core.ports;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public interface ExistentTagsResolver {

    Set<Tag> resolve(Tag[] tags);

    Set<Tag> extractDistinctTags(Collection<Event> events, Player player);
}
