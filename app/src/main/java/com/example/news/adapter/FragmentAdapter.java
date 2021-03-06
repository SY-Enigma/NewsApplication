package com.example.news.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleList;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    /**
     * 此方法是给tablayout中的tab赋值的，就是显示名称
     *
     * @param position
     * @return
     */

    @Override

    public CharSequence getPageTitle(int position) {

        return titleList.get(position % titleList.size());

    }
}
