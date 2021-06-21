package com.example.news.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.news.entity.LoadListView;
import com.example.news.R;
import com.example.news.activity.ShowNewsActivity;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;
//import com.example.news.database.OperationDao;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyBitmapUtils;
import com.example.news.util.MyDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ITFragment extends BaseFragment implements LoadListView.ILoadListener,
        LoadListView.RLoadListener, NewsAdapter.CallBack{
    final String url = "http://api.tianapi.com/it/index?key=5247ca753ac53750629e9a01664d99c2&num=30";

    private View view;
    private LoadListView mListView;
    private List<News> newsList;
    private MyDatabaseHelper Helper ;
//    private OperationDao dao;

    private NewsAdapter adapter;

    private MyBitmapUtils myBitmapUtils;

    public ITFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Helper = new MyDatabaseHelper(AppContext.getContext(),"UserDB.db",null,6);
        myBitmapUtils = new MyBitmapUtils(getContext());
        Helper = new MyDatabaseHelper(getContext(),"UserDB.db",null,6);
//        dao = new OperationDao(Helper);
        setupViews();
        if (!HttpUtils.isNetworkAvalible(getContext())) {
            //HttpUtils.checkNetwork(getActivity());
            Toast.makeText(getContext(), "当前没有可以使用的网络，请检查网络设置！", Toast.LENGTH_SHORT).show();

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
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);

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
            newsList.clear();
            int count = new Random().nextInt(10)+1;
            for (int i = count; i < count+10; i++) {
                JSONObject json_news = jsonArray.getJSONObject(i);
                String imgUrl = json_news.getString("picUrl");
                /**
                 * 采取三级缓存策略加载图片
                 */

                Bitmap bitmap = myBitmapUtils.getBitmap(imgUrl);
                /**
                 * 不采取缓存策略
                 */
                //Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
                String title = json_news.getString("title");
                String date = json_news.getString("ctime");
                String author_name = json_news.getString("description");
                String url = json_news.getString("url");

                News news = new News(bitmap, title, url, imgUrl, date, author_name);
//                add_news(news,"channel_Anime");
//                dao.add_news(news,"channel_Anime");
                SQLiteDatabase db = Helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                //组装数据
                values.put("news_url", url);
                values.put("news_title", title);
                values.put("news_date", date);
                values.put("news_author", author_name);
                values.put("news_picurl", imgUrl);

//                db.insert("Anime_News", null, values);
                db.insert("All_News",null,values);

                db.close();

                newsList.add(news);

            }
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

    private void parseJSONWithGSON_Refresh(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");
//            newsList.clear();
            int count = new Random().nextInt(10)+1;
            for (int i = count;i<count+10;i++) {
                JSONObject json_news = jsonArray.getJSONObject(i);
                String imgUrl = json_news.getString("picUrl");
                Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
                String title = json_news.getString("title");
                String date = json_news.getString("ctime");
                String author_name = json_news.getString("description");
                String url = json_news.getString("url");

                News news = new News(bitmap, title, url, date, imgUrl, author_name);
                newsList.add(news);
            }


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

    private void parseJSONWithGSON_Load(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

                JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(29)+1);
                String imgUrl = json_news.getString("picUrl");
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
        //上拉加载接口
        mListView.setInterface(this);
        mListView.setReflashInterface(this);
        newsList = new ArrayList<News>();
        adapter = new NewsAdapter(getActivity(), R.layout.news_item, newsList, this);
        mListView.setAdapter(adapter);


    }

    //实现onLoad()方法。
    @Override
    public void onLoad() {
        //添加延时效果模拟数据加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initNewDatas();//得到新数据
                mListView.loadCompleted();
//                InsertToDB(newsList);
            }
        }, 2000);
    }

    //  实现的刷新方法
    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRefreshDatas();//得到新数据
                mListView.reflashComplete();
//                InsertToDB(newsList);
            }
        }, 2000);

    }

    @Override
    public void click(View view) {
        Toast.makeText(getActivity(), "该新闻已删除！", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        adapter.notifyDataSetChanged();
    }

    public void InsertToDB(List<News> list) {
        SQLiteDatabase db = Helper.getWritableDatabase();
        for (int i = 0;i<list.size();i++) {
            if (!isEqual(list.get(i).getNews_title())) {
                ContentValues values = new ContentValues();
                values.put("news_url", list.get(i).getNews_url());
                values.put("news_title", list.get(i).getNews_title());
                values.put("news_date", list.get(i).getDate());
                values.put("news_author", list.get(i).getAuthor_name());
                values.put("news_picurl", list.get(i).getNews_picurl());
                db.insert("All_News",null,values);
            }
        }
        db.close();
    }

    public boolean isEqual(String newtitle){
        SQLiteDatabase db = Helper.getReadableDatabase();
        List<String> list = new ArrayList<>();
        boolean iseuqal = false;
        Cursor cursor = db.rawQuery("select news_title from All_News ",null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex("news_title"));
                    list.add(title);
                }while (cursor.moveToNext());
            }
        }
        else return false;
        cursor.close();
        for (int i = 0;i<list.size();i++) {
            iseuqal = newtitle.equals(list.get(i));
            if (iseuqal)
                break;
        }
        db.close();
        return iseuqal;
//        public boolean queryEqual_Title(String tables,String title){
//        SQLiteDatabase db = Helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + tables + " where title = ?",new String[]{title});
//        Integer count = cursor.getCount();
//        db.close();
//        if (count == 0)
//            return false;
//        else
//            return true;
//    }
    }
}
