package com.br.hobbie.shared.core.ports;

import com.br.hobbie.modules.player.domain.entities.Tag;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface ExistentTagsResolver {

    Set<Tag> resolve(Tag[] tags);
}
