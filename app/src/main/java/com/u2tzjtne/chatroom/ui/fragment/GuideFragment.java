package com.u2tzjtne.chatroom.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.u2tzjtne.chatroom.R;


/**
 * Created by JK on 2017/11/14.
 */

public class GuideFragment extends Fragment {

    private AppCompatTextView sectionLabel;
    private AppCompatTextView sectionIntro;
    private ImageView sectionImg;


    private int page;

    // The fragment argument representing
    // the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";


    public GuideFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GuideFragment newInstance(int sectionNumber) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        initViews(view);

        switch (page) {
            case 0:
                sectionImg.setBackgroundResource(R.drawable.ic_beenhere_black_24dp);
                sectionLabel.setText(R.string.guide_section_1);
                sectionIntro.setText(R.string.guide_intro_1);
                break;
            case 1:
                sectionImg.setBackgroundResource(R.drawable.ic_camera_black_24dp);
                sectionLabel.setText(R.string.guide_section_2);
                sectionIntro.setText(R.string.guide_intro_2);
                break;
            case 2:
                sectionImg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
                sectionLabel.setText(R.string.guide_section_3);
                sectionIntro.setText(R.string.guide_intro_3);
                break;
            default:
                break;
        }

        return view;
    }

    private void initViews(View view) {
        sectionLabel = view.findViewById(R.id.section_label);
        sectionIntro = view.findViewById(R.id.section_intro);
        sectionImg = view.findViewById(R.id.section_img);
    }
}
