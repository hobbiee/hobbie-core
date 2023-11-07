package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.modules.event.infrastructure.http.dtos.request.EventThumbnailFile;
import com.br.hobbie.shared.core.ports.FileUploader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/events")
@RequiredArgsConstructor
public class UploadEventThumbnail {

    private final EventRepository repository;
    private final FileUploader uploader;


    @PatchMapping(value = "/{id}/thumbnail", consumes = "multipart/form-data")
    public ResponseEntity<?> perform(@Valid EventThumbnailFile request, @PathVariable @NotNull @Positive Long id) {
        return repository.findById(id)
                .map(event -> {
                    var thumbnail = uploader.uploadFile(request.file());
                    event.setThumbnail(thumbnail);
                    repository.save(event);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.unprocessableEntity().body("You may provided an invalid event id"));
    }
}
