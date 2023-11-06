package com.br.hobbie.modules.event.infrastructure.http.controllers;

import com.br.hobbie.modules.event.domain.repositories.EventRepository;
import com.br.hobbie.shared.core.ports.FileUploader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/events")
@RequiredArgsConstructor
public class UploadEventThumbnail {

    private final EventRepository repository;
    private final FileUploader uploader;


    @PatchMapping(value = "/{id}/thumbnail", consumes = "multipart/form-data")
    public void perform(@Valid @NotNull MultipartFile file, @PathVariable @NotNull @Positive Long id) {
        repository.findById(id)
                .ifPresent(event -> {
                    var thumbnail = uploader.uploadFile(file);
                    event.setThumbnail(thumbnail);
                    repository.save(event);
                });
    }
}
