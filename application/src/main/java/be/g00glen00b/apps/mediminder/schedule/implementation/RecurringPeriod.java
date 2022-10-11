package be.g00glen00b.apps.mediminder.schedule.implementation;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public record RecurringPeriod(LocalDate startingAt, Period interval) {
    public boolean isActiveOn(LocalDate date) {
        if (startingAt.isEqual(date)) return true;
        if (date.isBefore(startingAt)) return false;
        int windowEnd = guessPeriods(date) * 2;
        int windowStart = 0;
        int halfway;
        LocalDate halfwayDate;
        do {
            halfway = ((windowEnd - windowStart) / 2) + windowStart;
            halfwayDate = startingAt.plus(interval.multipliedBy(halfway));
            if (halfwayDate.isAfter(date)) {
                windowEnd = halfway;
            } else if (halfwayDate.isBefore(date)) {
                windowStart = halfway;
            } else {
                return true;
            }
        } while ((windowEnd - windowStart) > 1);
        return false;
    }

    private int guessPeriods(LocalDate date) {
        int totalDaysPeriod = (int) ChronoUnit.DAYS.between(startingAt, date);
        int guessTotalDaysInterval = (int) interval.toTotalMonths() * 30 + interval.getDays();
        return totalDaysPeriod / guessTotalDaysInterval;
    }
}
