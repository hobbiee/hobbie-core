package com.br.hobbie.shared.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FilesAllowedValidator implements ConstraintValidator<FilesAllowed, MultipartFile> {
    private String[] extensions;

    @Override
    public void initialize(FilesAllowed constraintAnnotation) {
        extensions = constraintAnnotation.extensions();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) return true;

        var fileName = value.getOriginalFilename();

        if (fileName == null) return false;

        var fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        return Arrays.asList(extensions).contains(fileExtension);
    }
}
