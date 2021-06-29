package com.example.news.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;

import com.example.news.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏新闻
 */
public class CollectionActivity extends AppCompatActivity implements NewsAdapter.CallBack{
    private List<News> newsList = new ArrayList<>();

    private ListView collection;

    private NewsAdapter adapter;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        helper = new MyDatabaseHelper(this, "UserDB.db", null, 6);
        initView();
        initNews();

        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = newsList.get(i).getNews_url();
                Intent intent = new Intent(CollectionActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from Collection_News", null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            String news_url = cursor.getString(cursor.getColumnIndex("news_url"));
                            String news_title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String news_date = cursor.getString(cursor.getColumnIndex("news_date"));
                            String news_author = cursor.getString(cursor.getColumnIndex("news_author"));
                            String news_picurl = cursor.getString(cursor.getColumnIndex("news_picurl"));
//                            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(news_picurl);
                            News news = new News( news_title, news_url, news_picurl, news_date, news_author);
                            newsList.add(0,news);

                        } while (cursor.moveToNext());
                    }
                } else {
//                if (newsList.size() == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "收藏夹为空！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                cursor.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                db.close();
            }
        }).start();

    }

    private void initView() {
        collection = (ListView) findViewById(R.id.listview_collection);
        adapter = new NewsAdapter(this, R.layout.news_item, newsList, this);
    }

    @Override
    public void click(View view) {
        int position = Integer.parseInt(view.getTag().toString());
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("delete from Collection_News where news_title=?",
                new String[]{newsList.get(position).getNews_title()});
        db.close();
        newsList.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "该新闻已被移除收藏夹！", Toast.LENGTH_SHORT).show();

    }
}