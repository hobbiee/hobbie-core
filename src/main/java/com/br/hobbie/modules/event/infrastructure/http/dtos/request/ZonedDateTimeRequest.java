package com.br.hobbie.modules.event.infrastructure.http.dtos.request;

import java.time.ZonedDateTime;

public interface ZonedDateTimeRequest {
    ZonedDateTime getStartDate();

    ZonedDateTime getEndDate();
}
