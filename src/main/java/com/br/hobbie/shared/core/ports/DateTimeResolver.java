package com.br.hobbie.shared.core.ports;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateTimeResolver {


    LocalTime getStartTime();

    LocalTime getEndTime();

    LocalDate getDate();
}
