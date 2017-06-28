package com.novoda.snowyvillagewallpaper.santa;

import java.util.Calendar;

public class SantaSchedule {

    private final Clock clock;
    private final RoundDelay roundDelay;

    private long nextTimeInTown;

    public static SantaSchedule newInstance() {
        Clock clock = new Clock();
        RoundDelay roundDelay = new RoundDelay();
        return new SantaSchedule(clock, roundDelay);
    }

    SantaSchedule(Clock clock, RoundDelay roundDelay) {
        this.clock = clock;
        this.roundDelay = roundDelay;
    }

    public void calculateNextVisitTime() {
        if (isChristmas()) {
            nextTimeInTown = clock.getCurrentTime() + roundDelay.getNextRoundDelay();
        } else {
            nextTimeInTown = getNextChristmasMidnight();
        }
    }

    private boolean isChristmas() {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(clock.getCurrentTime());
        return now.get(Calendar.DAY_OF_MONTH) == 25
                && now.get(Calendar.MONTH) == Calendar.DECEMBER;
    }

    private long getNextChristmasMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(clock.getCurrentTime());
        if (isAfterChristmas(calendar)) {
            calendar.add(Calendar.YEAR, 1);
        }
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private boolean isAfterChristmas(Calendar now) {
        return now.get(Calendar.MONTH) == Calendar.DECEMBER && now.get(Calendar.DAY_OF_MONTH) > 25;
    }

    public boolean isSantaInTown() {
        return clock.getCurrentTime() >= nextTimeInTown;
    }

}
