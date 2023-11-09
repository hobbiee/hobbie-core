package com.br.hobbie.shared.core.adapters;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.br.hobbie.shared.core.ports.FileUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;

@Component
@Profile({"prod, dev"})
@RequiredArgsConstructor
@Slf4j
public class AmazonS3FileUploader implements FileUploader {

    private final AmazonS3 amazonS3;
    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        log.info("Initializing file {} upload to Amazon S3", file.getOriginalFilename());
        Assert.notNull(file.getOriginalFilename(), "File name cannot be null");
        var fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        File fileToUpload = new File(Objects.requireNonNull(fileName));
        try (var fileOutputStream = new FileOutputStream(fileToUpload)) {
            fileOutputStream.write(file.getBytes());
            var putObjectRequest = new PutObjectRequest(bucketName, fileName, fileToUpload);
            amazonS3.putObject(putObjectRequest);
            Files.delete(fileToUpload.toPath());
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (java.io.IOException e) {
            log.error("Error uploading file {} to Amazon S3", file.getOriginalFilename(), e);
        }
        return Optional.empty().toString();
    }
}
