package com.novoda.snowyvillagewallpaper;

import android.util.Log;

import java.io.IOException;

import net.rbgrn.android.glwallpaperservice.GLWallpaperService;

/**
 * Draws a wallpaper much like the regular wallpapers which slide behind
 * the home screen with a parallax effect. The difference with this wallpaper
 * is that it supports multiple images with different widths, for a much more
 * intricate-looking parallax effect.
 * <p/>
 * The main thing is that it's configurable, so you can use any set of images.
 */
public class ParallaxWallpaper extends GLWallpaperService {

    public static final String TAG = "ParallaxWallpaper";

    private ParallaxWallpaperRenderer renderer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new ParallaxEngine();
    }

    class ParallaxEngine extends GLEngine {

        ParallaxEngine() {
            super();
            renderer = new ParallaxWallpaperRenderer(ParallaxWallpaper.this.getAssets());
            setRenderer(renderer);

            // TODO: use RENDERMODE_WHEN_DIRTY if not showing snow
            setRenderMode(RENDERMODE_CONTINUOUSLY);

            initLayers();
            requestRender();
        }

        public void initLayers() {
            try {
                renderer.reloadLayers();
                renderer.resizeLayers();
            } catch (IOException e) {
                Log.e(TAG, "Error loading textures", e);
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            renderer.setOffset(xOffset, xPixels);
            requestRender();
        }
    }
}
