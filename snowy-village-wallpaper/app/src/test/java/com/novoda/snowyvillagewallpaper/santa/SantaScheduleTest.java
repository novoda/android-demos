package com.novoda.snowyvillagewallpaper.santa;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SantaScheduleTest {

    @Mock
    private Clock mockClock;

    @Mock
    private RoundDelay mockRoundDelay;

    private SantaSchedule santaSchedule;

    @Before
    public void setUp() {
        initMocks(this);
        when(mockRoundDelay.getNextRoundDelay()).thenReturn(0);
        santaSchedule = new SantaSchedule(mockClock, mockRoundDelay);
    }

    @Test
    public void givenCurrentTimeIsBeforeChristmas_WhenCheckingIfSantaIsInTown_ThenReturnFalse() {
        givenTheCurrentDateIs(2015, Calendar.DECEMBER, 23);
        santaSchedule.calculateNextVisitTime();

        boolean isSantaInTown = santaSchedule.isSantaInTown();

        assertThat(isSantaInTown).isFalse();
    }

    @Test
    public void givenCurrentTimeIsDuringChristmas_WhenCheckingIfSantaIsInTown_ThenReturnTrue() {
        givenTheCurrentDateIs(2015, Calendar.DECEMBER, 25);
        santaSchedule.calculateNextVisitTime();

        boolean isSantaInTown = santaSchedule.isSantaInTown();

        assertThat(isSantaInTown).isTrue();
    }

    @Test
    public void givenCurrentTimeIsAfterChristmas_WhenCheckingIfSantaIsInTown_ThenReturnFalse() {
        givenTheCurrentDateIs(2015, Calendar.DECEMBER, 26);
        santaSchedule.calculateNextVisitTime();

        boolean isSantaInTown = santaSchedule.isSantaInTown();

        assertThat(isSantaInTown).isFalse();
    }

    private void givenTheCurrentDateIs(int year, int month, int day) {
        Calendar daysBeforeChristmas = Calendar.getInstance();
        daysBeforeChristmas.set(year, month, day);
        when(mockClock.getCurrentTime()).thenReturn(daysBeforeChristmas.getTimeInMillis());
    }

}
