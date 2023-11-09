package com.br.hobbie.shared.core.adapters;

import com.br.hobbie.shared.core.ports.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Profile(value = {"test", "default"})
@Slf4j
public class LocalFileUploader implements FileUploader {
    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Uploading file {} to local storage", file.getOriginalFilename());
        return "http://localhost:8080/files/" + file.getOriginalFilename() + ".png";
    }
}
