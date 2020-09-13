package com.mj1666.providers3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private  int numoftabs;
    public PageAdapter(@NonNull FragmentManager fm, int numoftabs) {
        super(fm);
        this.numoftabs = numoftabs;
    }

    @NonNull



    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new electrician();
            case 1:
                return new cook();
            case 2:
                return new plumber();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }

    public  int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
