package com.novoda.spritz.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.novoda.spritz.Spritz;
import com.novoda.spritz.SpritzStep;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DebugActivity extends AppCompatActivity {

    private static final int PAGES_COUNT = 3;

    private Spritz spritz;
    private ViewPager viewPager;
    private TextView debugText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug);

        LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        spritz = Spritz
                .with(lottieAnimationView)
                .withSteps(
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(1, TimeUnit.SECONDS)
                                .build(),
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(1, TimeUnit.SECONDS)
                                .build(),
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .build()
                )
                .build();

        Button continueButton = findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem = viewPager.getCurrentItem() + 1;
                if (PAGES_COUNT > nextItem) {
                    viewPager.setCurrentItem(nextItem);
                }
            }
        });
        Button previousButton = findViewById(R.id.btn_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevItem = viewPager.getCurrentItem() - 1;
                if (prevItem >= 0) {
                    viewPager.setCurrentItem(prevItem);
                }
            }
        });

        debugText = findViewById(R.id.debug_text);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                debugText.setText(String.format(Locale.ENGLISH, "%f", position + positionOffset + 1));
            }

            @Override
            public void onPageSelected(int position) {
                // do nothing
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // do nothing
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

            }
        });
        spritz.attachTo(viewPager);
        spritz.startPendingAnimations();
    }

    @Override
    protected void onStop() {
        spritz.detachFrom(viewPager);
        super.onStop();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return new AnimationFragment();
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

    }

    public static class AnimationFragment extends Fragment {

        public AnimationFragment() {
            // default constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.page, container, false);
        }

    }
}
