package com.br.hobbie.shared.factory;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidDateValidorTestFactory {

    public static FakeDateTimeResolver validDate() {
        if (LocalTime.now().isBefore(LocalTime.of(23, 0))) {
            return new FakeDateTimeResolver(LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1));
        }

        var startTime = LocalTime.of(23, 0).minusHours(1);
        var endTime = LocalTime.of(23, 0);
        return new FakeDateTimeResolver(LocalDate.now(), startTime, endTime);
    }

    public static FakeDateTimeResolver invalidDateStartsAtMidnight() {
        return new FakeDateTimeResolver(LocalDate.now(), LocalTime.of(0, 0), LocalTime.of(1, 0));
    }

    public static FakeDateTimeResolver invalidDateEndsBeforeStart() {
        return new FakeDateTimeResolver(LocalDate.now().plusDays(1), LocalTime.of(16, 0), LocalTime.of(15, 0));
    }

    public static FakeDateTimeResolver validDateStartsAt23Hours() {
        return new FakeDateTimeResolver(LocalDate.now().plusDays(1), LocalTime.of(23, 0), LocalTime.of(23, 0).plusHours(1));
    }

    public static FakeDateTimeResolver invalidDateTomorrowStartingAfterElevenPM() {
        return new FakeDateTimeResolver(LocalDate.now().plusDays(1), LocalTime.of(23, 0), LocalTime.of(23, 0).plusMinutes(20));
    }
}
