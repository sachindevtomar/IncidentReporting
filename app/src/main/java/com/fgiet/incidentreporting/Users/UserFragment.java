package com.fgiet.incidentreporting.Users;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fgiet.incidentreporting.R;
import com.fgiet.incidentreporting.TrackRequest;

public class UserFragment extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static TabLayout tabLayout;


    public UserFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView=inflater.inflate(R.layout.activity_user_fragment,container,false);
        tabLayout=(TabLayout)mView.findViewById(R.id.tabs);
        mViewPager=(ViewPager)mView.findViewById(R.id.container1);
        getActivity().setTitle(getString(R.string.report_an_incident));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
            }
        });
        return mView;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                switch (position){
                case 0:
                    Tab1 tab1=new Tab1();
                    return tab1;
                case 1:
                    TrackRequest tab2=new TrackRequest();
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.incident);
                case 1:
                    return getString(R.string.tracker);
            }
            return null;
        }
    }
}
