package com.example.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.news.R;
import com.example.news.activity.SearchActivity;
import com.example.news.adapter.FragmentAdapter;
import com.example.news.util.TimeCount;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private View view;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    private FragmentAdapter fragmentAdapter;


    private InternationalFragment international_fragment;
    private VRFragment VR_fragment;
    private SportFragment sport_fragment;
    private CBAFragment CBA_fragment;
    private NBAlFragment NBA_fragment;
    private ITFragment IT_fragment;
    private FootballFragment football_fragment;


    private ImageButton button;
    private EditText words;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == -1) {
                    String text = data.getStringExtra("text");
                    words.setText(text);
                }
            }break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        fragmentChange();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("context",words.getText().toString());
                startActivityForResult(intent,1);
            }
        });
        TimeCount.getInstance().setTime(System.currentTimeMillis());

        return view;
    }

    private void initView() {
//        searchView = view.findViewById(R.id.search_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        button = view.findViewById(R.id.onsearch);
        words = view.findViewById(R.id.key_word);

    }

    private void fragmentChange() {
        fragmentList = new ArrayList<>();

        international_fragment = new InternationalFragment();
        VR_fragment = new VRFragment();
        sport_fragment = new SportFragment();
        CBA_fragment = new CBAFragment();
        NBA_fragment = new NBAlFragment();
        IT_fragment = new ITFragment();
        football_fragment = new FootballFragment();



        fragmentList.add(international_fragment);
        fragmentList.add(VR_fragment);
        fragmentList.add(sport_fragment);
        fragmentList.add(NBA_fragment);
        fragmentList.add(CBA_fragment);
        fragmentList.add(IT_fragment);
        fragmentList.add(football_fragment);

        titleList = new ArrayList<>();

        titleList.add("国际");
        titleList.add("VR");
        titleList.add("体育");
        titleList.add("NBA");
        titleList.add("CBA");
        titleList.add("IT");
        titleList.add("足球");

        fragmentAdapter = new FragmentAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentAdapter);

        //将tabLayout与viewPager连起来
        tabLayout.setupWithViewPager(viewPager);

    }


}