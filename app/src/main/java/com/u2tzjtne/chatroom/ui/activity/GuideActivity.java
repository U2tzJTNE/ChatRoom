package com.u2tzjtne.chatroom.ui.activity;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.u2tzjtne.chatroom.R;
import com.u2tzjtne.chatroom.ui.adapter.GuidePagerAdapter;
import com.u2tzjtne.chatroom.utils.Const;
import com.u2tzjtne.chatroom.utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.imageButtonPre)
    ImageButton imageButtonPre;
    @BindView(R.id.imageButtonNext)
    ImageButton imageButtonNext;
    @BindView(R.id.imageViewIndicator0)
    ImageView imageViewIndicator0;
    @BindView(R.id.imageViewIndicator1)
    ImageView imageViewIndicator1;
    @BindView(R.id.imageViewIndicator2)
    ImageView imageViewIndicator2;
    @BindView(R.id.buttonFinish)
    AppCompatButton buttonFinish;


    private ImageView[] indicators;
    private int bgColors[];
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initViews();
        initData();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int colorUpdate = (Integer) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position : position + 1]);
                viewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateIndicators(position);
                viewPager.setBackgroundColor(bgColors[position]);
                imageButtonPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                imageButtonNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                buttonFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initViews() {
        GuidePagerAdapter pagerAdapter = new GuidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        buttonFinish.setText(R.string.guide_finish_button_description);
        indicators = new ImageView[]{
                imageViewIndicator0,
                imageViewIndicator1,
                imageViewIndicator2};
    }

    private void initData() {
        bgColors = new int[]{ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.cyan_500),
                ContextCompat.getColor(this, R.color.light_blue_500)};
    }

    //更新指示器
    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.guide_indicator_selected : R.drawable.guide_indicator_unselected
            );
        }
    }

    @OnClick({R.id.imageButtonPre, R.id.imageButtonNext, R.id.buttonFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageButtonPre:
                currentPosition -= 1;
                viewPager.setCurrentItem(currentPosition, true);
                break;
            case R.id.imageButtonNext:
                currentPosition += 1;
                viewPager.setCurrentItem(currentPosition, true);
                break;
            case R.id.buttonFinish:
                //记录是否是第一次启动
                SPUtil.putBoolean(Const.FIRST_LAUNCH, false);
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}