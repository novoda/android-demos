package com.novoda.spritz.sample;

import android.content.Intent;
import android.net.Uri;
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

import com.airbnb.lottie.LottieAnimationView;
import com.novoda.spritz.Spritz;
import com.novoda.spritz.SpritzStep;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int PAGES_COUNT = 3;

    private Spritz spritz;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LottieAnimationView lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView.setImageAssetsFolder("images");

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        spritz = Spritz
                .with(lottieAnimationView)
                .withSteps(
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(500, TimeUnit.MILLISECONDS)
                                .build(),
                        new SpritzStep.Builder()
                                .withAutoPlayDuration(1, TimeUnit.SECONDS)
                                .withSwipeDuration(500, TimeUnit.MILLISECONDS)
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

        Button linkButton = findViewById(R.id.btn_blog);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.novoda.com/blog/onboard-your-users-with-lottie-of-spritz/"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
