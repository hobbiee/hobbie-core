package com.br.hobbie.shared.core.ports;

import org.springframework.web.multipart.MultipartFile;

@FunctionalInterface
public interface FileUploader {

    String uploadFile(MultipartFile file);
}
