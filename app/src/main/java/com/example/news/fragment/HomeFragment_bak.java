package com.example.news.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.adapter.CategoryAdapter;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment_bak#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment_bak extends Fragment implements NewsAdapter.CallBack{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "HomeFragment_bak";

    private View view;

    private List<String> categoryList = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ListView listView;
    private CategoryAdapter categoryAdapter;

    private NewsAdapter newsAdapter;

    public HomeFragment_bak() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment_bak.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment_bak newInstance(String param1, String param2) {
        HomeFragment_bak fragment = new HomeFragment_bak();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_main_bak, container, false);
        view = inflater.inflate(R.layout.fragment_home_bak, container, false);
        initCategory();
        initNews();
        initView();

        newsAdapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        listView = view.findViewById(R.id.list_view);
        listView.setAdapter(newsAdapter);

        return view;
    }

    private void initNews() {
        for (int i = 0; i < 30; i++) {
//            News news = new News(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "这是新闻" + (i + 1) + "的标题");
//            newsList.add(news);
        }
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);
    }

    private void initCategory() {
        String[] categories = {
                "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"
        };
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            categoryList.add(category);
        }
    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "该新闻已删除！", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        newsAdapter.notifyDataSetChanged();
    }
}