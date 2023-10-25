package com.br.hobbie.modules.player.application;

import com.br.hobbie.modules.player.domain.entities.Tag;
import com.br.hobbie.modules.player.domain.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
class ResolveExistentTagsTest {

    @Autowired
    private ResolveExistentTags resolveExistentTags;

    @Autowired
    private TagRepository tagRepository;

    private Tag[] tags;


    @BeforeEach
    void setUp() {
        tags = new Tag[]{
                new Tag("tag with spaces"),
                new Tag("tag_without_spaces")
        };
    }

    @Test
    @DisplayName("Should resolve existent tags and return a set of them")
    void resolve_ExistentTags_ShouldReturnASetOfTags() {
        // GIVEN
        var firstTagSaved = tagRepository.save(tags[0]);
        tagRepository.save(tags[1]);

        // WHEN
        var result = resolveExistentTags.resolve(tags);

        // THEN
        Assertions.assertTrue(result.contains(tags[0]));
        Assertions.assertTrue(result.contains(tags[1]));
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(firstTagSaved.getId(), result.stream().findFirst().get().getId());
        Assertions.assertNotNull(result.stream().findFirst().get().getId());
    }

    @Test
    @DisplayName("Should create a new tag and return a set of the existent tags and the new one")
    void resolve_NonExistentTags_ShouldReturnASetOfTags() {
        // GIVEN
        tagRepository.save(tags[1]);

        // WHEN
        var result = resolveExistentTags.resolve(tags);

        // THEN
        Assertions.assertTrue(result.contains(tags[0]));
        Assertions.assertTrue(result.contains(tags[1]));
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return a set of tags when all tags are non existent")
    void resolve_AllNonExistentTags_ShouldReturnASetOfTags() {
        // WHEN
        var result = resolveExistentTags.resolve(tags);
        tagRepository.saveAll(result);

        // THEN
        Assertions.assertEquals(2, result.size());
        result.forEach(tag -> Assertions.assertNotNull(tag.getId()));
        Assertions.assertTrue(result.stream().anyMatch(tag -> tag.getName().equals(tags[0].getName())));
    }
}