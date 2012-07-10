package novoda.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import com.example.google.tv.leftnavbar.LeftNavBar;
import com.example.google.tv.leftnavbar.LeftNavBarService;

public class LeftNavBarWrapper {

    private LeftNavBar mLeftNavBar = null;

    private Activity mActivity = null;

    private static final ActionBar.TabListener BLANK_LISTENER = new ActionBar.TabListener() {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        }
    };

    LeftNavBarWrapper(Activity activity) {

        this.mActivity = activity;
    }

    public LeftNavBar newInstance() {

        return (LeftNavBarService.instance()).getLeftNavBar(mActivity);
    }

    public void leftNarBarInit(LeftNavBar bar) {

        setupTopActionBar();

        bar.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.leftnav_bar_background_dark));

        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(LeftNavBar.DISPLAY_SHOW_HOME);
        bar.setDisplayOptions(LeftNavBar.DISPLAY_SHOW_TITLE);
        bar.setDisplayOptions(LeftNavBar.DISPLAY_AUTO_EXPAND);

        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        bar.setShowHideAnimationEnabled(true);
    }

    public LeftNavBar getLeftNavBar() {
        if (mLeftNavBar == null) {
            mLeftNavBar = new LeftNavBar(mActivity);
            mLeftNavBar.setOnClickHomeListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return mLeftNavBar;
    }

    private void setupTopActionBar() {
        ActionBar bar = getLeftNavBar();
        bar.setTitle(R.string.app_name);
        setupCustomNavBarView();

        setupNavBarTabs();
    }

    private void setupCustomNavBarView() {
        getLeftNavBar().setCustomView(R.layout.custom_view);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(0);
        params.width = params.height = nextDimension(0);
        params.gravity = nextGravity(nextGravity(0, true), false);
        applyCustomParams(params);
    }

    private void applyCustomParams(ActionBar.LayoutParams params) {
        ActionBar bar = getLeftNavBar();
        bar.setCustomView(bar.getCustomView(), params);
    }

    private static int nextDimension(int dimension) {
        switch (dimension) {
            case 40:
                return 100;
            case 100:
                return ActionBar.LayoutParams.MATCH_PARENT;
            case ActionBar.LayoutParams.MATCH_PARENT:
            default:
                return 40;
        }
    }

    private static int nextGravity(int gravity, boolean horizontal) {
        int hGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        int vGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        if (horizontal) {
            switch (hGravity) {
                case Gravity.LEFT:
                    hGravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    hGravity = Gravity.RIGHT;
                    break;
                case Gravity.RIGHT:
                default:
                    hGravity = Gravity.LEFT;
                    break;
            }
        } else {
            switch (vGravity) {
                case Gravity.TOP:
                    vGravity = Gravity.CENTER_VERTICAL;
                    break;
                case Gravity.CENTER_VERTICAL:
                    vGravity = Gravity.BOTTOM;
                    break;
                case Gravity.BOTTOM:
                default:
                    vGravity = Gravity.TOP;
                    break;
            }
        }
        return hGravity | vGravity;
    }

    private void setupNavBarTabs() {
        ActionBar bar = getLeftNavBar();
        bar.removeAllTabs();

        bar.addTab(bar.newTab().setText(R.string.taba).setIcon(R.drawable.tab_a)
                .setTabListener(BLANK_LISTENER), false);
        bar.addTab(bar.newTab().setText(R.string.tabb).setIcon(R.drawable.tab_b)
                .setTabListener(BLANK_LISTENER), false);
        bar.addTab(bar.newTab().setText(R.string.tabc).setIcon(R.drawable.tab_c)
                .setTabListener(BLANK_LISTENER), false);
        bar.addTab(bar.newTab().setText(R.string.tabd).setIcon(R.drawable.tab_d)
                .setTabListener(BLANK_LISTENER), false);
    }
}
