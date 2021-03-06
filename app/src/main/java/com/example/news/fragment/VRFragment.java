package com.example.news.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.news.entity.LoadListView;
import com.example.news.R;
import com.example.news.activity.ShowNewsActivity;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;
//import com.example.news.database.Dao;

//import com.example.news.database.OperationDao;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyBitmapUtils;
import com.example.news.util.MyDatabaseHelper;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VRFragment extends BaseFragment implements LoadListView.ILoadListener,
        LoadListView.RLoadListener, NewsAdapter.CallBack{
    private static final String TAG = "VRFragment";
    final String url = "http://api.tianapi.com/vr/index?key=5247ca753ac53750629e9a01664d99c2&num=30";
    private View view;
    private LoadListView mListView;
//    private PullLoadMoreRecyclerView recyclerView;
    private List<News> newsList;
    private MyDatabaseHelper helper;

    private NewsAdapter adapter;

    private MyBitmapUtils myBitmapUtils;


    public VRFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);
        Log.d(TAG, "onCreateView");
        myBitmapUtils = new MyBitmapUtils(getContext());
//        recyclerView = (PullLoadMoreRecyclerView)view.findViewById(R.id.)
        helper = new MyDatabaseHelper(getContext(),"UserDB.db",null,6);
//        dao = new OperationDao(helper);
        setupViews();
        if (!HttpUtils.isNetworkAvalible(getContext())) {
            //HttpUtils.checkNetwork(getActivity());
            Toast.makeText(getContext(),"????????????????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
        } else {
            initNews();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowNewsActivity.class);
                intent.putExtra("title", newsList.get(i - mListView.getHeaderViewsCount()).getNews_title());
                intent.putExtra("url", newsList.get(i - mListView.getHeaderViewsCount()).getNews_url());
                intent.putExtra("date", newsList.get(i - mListView.getHeaderViewsCount()).getDate());
                intent.putExtra("author", newsList.get(i - mListView.getHeaderViewsCount()).getAuthor_name());
                intent.putExtra("pic_url", newsList.get(i - mListView.getHeaderViewsCount()).getNews_picurl());
                startActivity(intent);
                //??????Activity????????????
                getActivity().overridePendingTransition(R.anim.anim_scale, R.anim.anim_alpha);
            }
        });

        return view;


    }

    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON(jsonData);
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");
            int count = new Random().nextInt(10)+1;
            for (int i = count; i < count+10; i++) {
                JSONObject json_news = jsonArray.getJSONObject(i);
                String imgUrl = json_news.getString("picUrl");
                //????????????????????????????????????
                Bitmap bitmap = myBitmapUtils.getBitmap(imgUrl);
              //?????????????????????
                String title = json_news.getString("title");
                String date = json_news.getString("ctime");
                String author_name = json_news.getString("description");
                String url = json_news.getString("url");
                Log.d(TAG, "url:*-*-*-*-*-*-*" + imgUrl);
                News news = new News(bitmap, title, url, imgUrl, date, author_name);
                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                //????????????
                values.put("news_url", url);
                values.put("news_title", title);
                values.put("news_date", date);
                values.put("news_author", author_name);
                values.put("news_picurl", imgUrl);

                db.insert("Vr_News", null, values);
                db.insert("All_News",null,values);

                db.close();

                newsList.add(news);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSONWithGSON_Load(String jsonData) {

        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");
            JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(25) + 1);
            String imgUrl = json_news.getString("picUrl");
            Log.d(TAG, "url:*-*-*-*-*-*-*" + imgUrl);
            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
            String title = json_news.getString("title");
            String date = json_news.getString("ctime");
            String author_name = json_news.getString("description");
            String url = json_news.getString("url");

            News news = new News(bitmap, title, url, imgUrl, date, author_name);
            newsList.add(news);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void initNewDatas() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON_Load(jsonData);

            }
        }).start();


    }

    private void initRefreshDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                newsList.clear();
                parseJSONWithGSON(jsonData);
            }
        }).start();

    }

    private void setupViews() {

        mListView = view.findViewById(R.id.lv_main);
        //??????????????????
        mListView.setInterface(this);
        mListView.setReflashInterface(this);
        newsList = new ArrayList<News>();
        adapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        mListView.setAdapter(adapter);


    }

    //??????onLoad()?????????
    @Override
    public void onLoad() {
        //????????????????????????????????????
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initNewDatas();//???????????????
                mListView.loadCompleted();
            }
        }, 2000);
    }

    //  ?????????????????????
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRefreshDatas();//???????????????
                mListView.reflashComplete();
            }
        }, 2000);

    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
}
