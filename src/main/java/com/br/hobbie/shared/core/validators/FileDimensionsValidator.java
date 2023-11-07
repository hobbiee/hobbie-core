package com.br.hobbie.shared.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

public class FileDimensionsValidator implements ConstraintValidator<FileDimensions, MultipartFile> {
    private int minWidth;
    private int minHeight;

    @Override
    public void initialize(FileDimensions constraintAnnotation) {
        minWidth = constraintAnnotation.minWidth();
        minHeight = constraintAnnotation.minHeight();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            var image = ImageIO.read(value.getInputStream());

            return image.getWidth() >= minWidth && image.getHeight() >= minHeight;
        } catch (Exception e) {
            return false;
        }
    }
}
