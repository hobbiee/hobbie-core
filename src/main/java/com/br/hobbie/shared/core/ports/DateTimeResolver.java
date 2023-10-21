package com.br.hobbie.shared.core.ports;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DateTimeResolver {


    LocalTime startTime();

    LocalTime endTime();

    LocalDate date();
}
