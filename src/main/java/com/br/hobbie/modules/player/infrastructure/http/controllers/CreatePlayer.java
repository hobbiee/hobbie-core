package com.br.hobbie.modules.player.infrastructure.http.controllers;

import com.br.hobbie.modules.player.domain.repositories.PlayerRepository;
import com.br.hobbie.modules.player.infrastructure.http.dtos.request.CreatePlayerRequest;
import com.br.hobbie.shared.core.ports.ExistentTagsResolver;
import com.br.hobbie.shared.core.ports.FileUploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/players")
@RequiredArgsConstructor
public class CreatePlayer {

    private final PlayerRepository playerRepository;
    private final ExistentTagsResolver existentTagsResolver;
    private final FileUploader uploader;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Void> handle(@Valid CreatePlayerRequest request) {
        var player = request.toEntity(existentTagsResolver, uploader);
        playerRepository.save(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
