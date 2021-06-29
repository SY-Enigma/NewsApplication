package com.example.news.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 点赞功能，从数据库Good_News中获取点赞的新闻
 * 可以进行删除点赞的新闻
 */
public class GoodActivity extends AppCompatActivity implements NewsAdapter.CallBack{
    //利用list数组获取新闻列表
    private List<News> newsList = new ArrayList<>();
    private ListView good;

    private NewsAdapter adapter;
    private MyDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);

        initView();
        initNews();
        helper = new MyDatabaseHelper(this,"UserDB.db",null,6);

        good.setAdapter(adapter);
        good.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newsList.get(position).getNews_url();
                Intent intent = new Intent(GoodActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        good = (ListView) findViewById(R.id.listview_good);
        adapter = new NewsAdapter(this, R.layout.news_item, newsList, this);
    }

    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from Good_News", null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            String news_url = cursor.getString(cursor.getColumnIndex("news_url"));
                            String news_title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String news_date = cursor.getString(cursor.getColumnIndex("news_date"));
                            String news_author = cursor.getString(cursor.getColumnIndex("news_author"));
                            String news_picurl = cursor.getString(cursor.getColumnIndex("news_picurl"));
                            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(news_picurl);
                            News news = new News(bitmap, news_title, news_url, news_picurl, news_date, news_author);
                            newsList.add(0,news);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "无点赞新闻！", Toast.LENGTH_SHORT).show();
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

    @Override
    public void click(View view) {
        int position = Integer.parseInt(view.getTag().toString());
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("delete from Good_News where news_title=?",
                new String[]{newsList.get(position).getNews_title()});
        db.close();
        newsList.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "取消点赞", Toast.LENGTH_SHORT).show();
    }
}