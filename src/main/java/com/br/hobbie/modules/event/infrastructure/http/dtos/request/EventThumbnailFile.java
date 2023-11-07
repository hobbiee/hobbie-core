package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import com.br.hobbie.shared.core.validators.FileDimensions;
import com.br.hobbie.shared.core.validators.FileSize;
import com.br.hobbie.shared.core.validators.FilesAllowed;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record EventThumbnailFile(
        @NotNull
        @FilesAllowed(extensions = {"jpg", "jpeg", "png"}, message = "Avatar must be a valid image")
        @FileDimensions(minWidth = 200, minHeight = 200, message = "Avatar must be at least 200x200 pixels")
        @FileSize(max = 5 * 1024 * 1024, message = "Avatar must be at most 5MB")
        MultipartFile file
) {
}
