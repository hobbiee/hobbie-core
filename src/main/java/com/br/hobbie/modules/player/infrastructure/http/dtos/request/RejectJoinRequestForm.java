package com.br.hobbie.modules.player.infrastructure.http.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RejectJoinRequestForm(
        @NotNull
        @Positive
        Long adminId,

        @NotNull
        @Positive
        Long playerToRejectId,

        @NotNull
        @Positive
        Long eventId
) {
}
