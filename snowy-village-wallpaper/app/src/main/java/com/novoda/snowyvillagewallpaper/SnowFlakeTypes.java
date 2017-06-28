package com.novoda.snowyvillagewallpaper;

enum SnowFlakeTypes {
    SMALL(1f, 0.09f),
    MEDIUM(2f, 0.26f),
    BIG(3f, 0.40f);

    private final float baseSpeed;
    private final float textureRatio;

    SnowFlakeTypes(float baseSpeed, float textureRatio) {
        this.baseSpeed = baseSpeed;
        this.textureRatio = textureRatio;
    }

    public static int count() {
        return values().length;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getTextureRatio() {
        return textureRatio;
    }
}
