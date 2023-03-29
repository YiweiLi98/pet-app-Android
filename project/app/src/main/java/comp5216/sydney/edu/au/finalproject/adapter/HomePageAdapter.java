package comp5216.sydney.edu.au.finalproject.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.finalproject.FriendFragment;
import comp5216.sydney.edu.au.finalproject.HomeFragment;
import comp5216.sydney.edu.au.finalproject.MeFragment;
import comp5216.sydney.edu.au.finalproject.NewFragment;
import comp5216.sydney.edu.au.finalproject.pojo.Post;


public class HomePageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public HomePageAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm, BEHAVIOR_SET_USER_VISIBLE_HINT);
        fragments = fragmentList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }
}
