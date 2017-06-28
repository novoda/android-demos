package com.novoda.snowyvillagewallpaper.santa;

import java.util.Random;

public class SantaTracker {

    private static final float SKY_HEIGHT_FRACTION = 0.6f;
    private static final float TOP_SKY_FRACTION = 0.1f;
    private static final float SPEED = 4.0f;

    private final float skyWidth;
    private final float skyHeight;
    private final float santaWidth;
    private final float santaHeight;

    private float x;
    private float y;
    private Direction direction;

    private final SantaSchedule santaSchedule;

    public SantaTracker(float skyWidth, float skyHeight, float santaWidth, float santaHeight, SantaSchedule santaSchedule) {
        this.skyWidth = skyWidth;
        this.skyHeight = skyHeight;
        this.santaWidth = santaWidth;
        this.santaHeight = santaHeight;
        this.santaSchedule = santaSchedule;

        direction = Direction.TO_RIGHT;
        resetPosition();
        santaSchedule.calculateNextVisitTime();
    }

    private void resetPosition() {
        y = resetYToCentralHeight();
        if (direction == Direction.TO_RIGHT) {
            x = -santaWidth;
        } else {
            x = skyWidth;
        }
    }

    private float resetYToCentralHeight() {
        Random random = new Random();
        return random.nextFloat() * (skyHeight - santaHeight) * SKY_HEIGHT_FRACTION + (skyHeight - santaHeight) * TOP_SKY_FRACTION;
    }

    public void updatePosition() {
        if (direction == Direction.TO_RIGHT) {
            x += SPEED;
        } else {
            x -= SPEED;
        }
        if (x > skyWidth || x < -santaWidth) {
            santaSchedule.calculateNextVisitTime();
            flipDirection();
            resetPosition();
        }
    }

    private void flipDirection() {
        if (direction == Direction.TO_LEFT) {
            direction = Direction.TO_RIGHT;
        } else {
            direction = Direction.TO_LEFT;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean movingToRight() {
        return direction == Direction.TO_RIGHT;
    }

    public boolean isSantaInTown() {
        return santaSchedule.isSantaInTown();
    }

    private enum Direction {
        TO_LEFT,
        TO_RIGHT
    }
}
