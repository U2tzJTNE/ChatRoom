package com.u2tzjtne.chatroom.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.u2tzjtne.chatroom.ui.fragment.GuideFragment;

/**
 * Created by JK on 2017/11/14.
 */

public class GuidePagerAdapter extends FragmentPagerAdapter {
    private final int pagerCount=3;

    public GuidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return pagerCount;
    }
}
