package com.novoda.snowyvillagewallpaper.santa;

import java.util.Random;
import java.util.concurrent.TimeUnit;

class RoundDelay {

    private static final int MIN_INTERVAL_BETWEEN_ROUNDS = (int) TimeUnit.SECONDS.toMillis(5);
    private static final int MAX_INTERVAL_BETWEEN_ROUNDS = (int) TimeUnit.SECONDS.toMillis(10);

    private final Random random;

    public RoundDelay() {
        random = new Random();
    }

     public int getNextRoundDelay() {
         return MIN_INTERVAL_BETWEEN_ROUNDS
                 + random.nextInt(MAX_INTERVAL_BETWEEN_ROUNDS - MIN_INTERVAL_BETWEEN_ROUNDS);
     }
}
