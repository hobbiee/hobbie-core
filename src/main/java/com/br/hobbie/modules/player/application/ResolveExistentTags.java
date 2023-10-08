package com.br.hobbie.modules.player.application;

import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.modules.player.domain.repositories.TagRepository;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
}
