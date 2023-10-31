package com.br.hobbie.modules.player.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.modules.player.domain.repositories.TagRepository;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResolveExistentTags implements ExistentTagsResolver {

    private final TagRepository repository;

    @Override
    public Set<Tag> resolve(Tag[] tags) {
        HashSet<Tag> existentTags = repository.findByNameIgnoreCaseIn(
                Arrays.stream(tags)
                        .map(Tag::getName)
                        .toList()
        ).stream().collect(HashSet::new, HashSet::add, HashSet::addAll);

        var tagsToSave = new HashSet<Tag>();

        Arrays.stream(tags)
                .filter(tag -> !repository.existsByName(tag.getName()))
                .forEach(tagsToSave::add);

        existentTags.addAll(tagsToSave);

        return existentTags;
    }

    @Override
    public Set<Tag> extractDistinctTags(Collection<Event> events, Player player) {
        return events.stream()
                .map(event -> event.getCategories().stream()
                        .filter(category -> !player.getInterests().contains(category))
                        .collect(Collectors.toSet()))
                .reduce(new HashSet<>(), (acc, tags) -> {
                    acc.addAll(tags);
                    return acc;
                });
    }
}
