package com.example.pranav.splitdo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private PersonalTasksFragment mFragment1;
    private GroupsFragment mFragment2;
    private Context mContext;

    public TabsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        mContext = context;
    }

    public void setFragments(PersonalTasksFragment fragment1, GroupsFragment fragment2){
        mFragment1 =fragment1;
        mFragment2 = fragment2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return mFragment1;
            case 1:
                return mFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return mContext.getResources().getString(R.string.personal_tasks);
            case 1:
                return mContext.getResources().getString(R.string.groups);
            default:
                return null;
        }
    }
}
