package com.br.hobbie.shared.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    long maxSize;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        maxSize = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return value.getSize() <= maxSize;
    }
}
