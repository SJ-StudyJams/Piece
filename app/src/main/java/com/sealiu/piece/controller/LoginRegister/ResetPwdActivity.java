package com.sealiu.piece.controller.LoginRegister;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sealiu.piece.R;

import java.util.ArrayList;
import java.util.List;

public class ResetPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TextView tv_tab0, tv_tab1, line_tab;
    private int moveOne = 0;
    private boolean isScrolling = false;
    private boolean isBackScrolling = false;
    private long startTime = 0;
    private long currentTime = 0;
    private static final String TAG = "ResetPwdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        viewPager = (ViewPager) findViewById(R.id.resetPwd_viewPager);
        initView();
        initLine();
    }

    /**
     * 重新设定line的宽度
     */
    private void initLine() {
        /**
         * 获取屏幕的宽度
         */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;

        /**
         * 重新设置下划线的宽度
         */
        ViewGroup.LayoutParams lp = line_tab.getLayoutParams();
        lp.width = screenW / 2;
        line_tab.setLayoutParams(lp);
        moveOne = lp.width; // 滑动一个页面的距离
    }

    private void initView() {
        ResetPwdByEmailFragment fragment1 = new ResetPwdByEmailFragment();
        ResetPwdByPhoneFragment fragment2 = new ResetPwdByPhoneFragment();
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        tv_tab0 = (TextView) findViewById(R.id.tv_tab0);
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);

        viewPager.setCurrentItem(0);

        tv_tab0.setTextColor(getResources().getColor(R.color.colorAccent));
        tv_tab1.setTextColor(getResources().getColor(R.color.secondaryText));
        Log.i(TAG, "color change 3");

        tv_tab0.setOnClickListener(this);
        tv_tab1.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentTime = System.currentTimeMillis();
                if (isScrolling && (currentTime - startTime > 200)) {
                    movePositionX(position, moveOne * positionOffset);
                    startTime = currentTime;
                }
                if (isBackScrolling) {
                    movePositionX(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        movePositionX(0);
                        tv_tab0.setTextColor(getResources().getColor(R.color.colorAccent));
                        tv_tab1.setTextColor(getResources().getColor(R.color.secondaryText));
                        Log.i(TAG, "color change 0");
                        break;
                    case 1:
                        movePositionX(1);
                        tv_tab0.setTextColor(getResources().getColor(R.color.secondaryText));
                        tv_tab1.setTextColor(getResources().getColor(R.color.colorAccent));
                        Log.i(TAG, "color change 1");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:
                        isScrolling = true;
                        isBackScrolling = false;
                        break;
                    case 2:
                        isScrolling = false;
                        isBackScrolling = true;
                        break;
                    default:
                        isScrolling = false;
                        isBackScrolling = false;
                        break;
                }
            }
        });

        line_tab = (TextView) findViewById(R.id.line_tab);
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }



    /** * 下划线跟随手指的滑动而移动
     * @param toPosition
     * @param positionOffsetPixels
     */
    private void movePositionX(int toPosition, float positionOffsetPixels) {
        // TODO Auto-generated method stub
        float curTranslationX = line_tab.getTranslationX();
        float toPositionX = moveOne * toPosition + positionOffsetPixels;
        ObjectAnimator animator = ObjectAnimator.ofFloat(line_tab, "translationX", curTranslationX, toPositionX);
        animator.setDuration(500);
        animator.start(); }

    /** 下划线滑动到新的选项卡中
     * @param toPosition
     */

    private void movePositionX(int toPosition) {
        // TODO Auto-generated method stub
        movePositionX(toPosition, 0);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_tab0:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab1:
                viewPager.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}
