package com.example.apphoctapchotre.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apphoctapchotre.Fragment.GioiThieuMonFragment;

public class GTKHPagerAdapter extends FragmentStateAdapter {

    public GTKHPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return GioiThieuMonFragment.newInstance("english");
            case 1:
                return GioiThieuMonFragment.newInstance("math");
            default:
                return GioiThieuMonFragment.newInstance("fun");
        }
    }
}

