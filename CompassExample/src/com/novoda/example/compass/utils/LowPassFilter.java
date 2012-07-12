package com.novoda.example.compass.utils;

public class LowPassFilter {

    private static final float ALPHA = 0.2f;

    public static float[] filter(float[] input, float[] prev) {
        if (input == null || prev == null)
            throw new NullPointerException("input and prev float arrays must be non-NULL");
        if (input.length != prev.length)
            throw new IllegalArgumentException("input and prev must be the same length");
        for (int i = 0; i < input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }
}
