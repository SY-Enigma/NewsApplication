package com.example.news.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;
import com.example.news.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SeeActivity extends AppCompatActivity implements NewsAdapter.CallBack{
    private List<News> newsList = new ArrayList<>();

    private ListView seeview;
    private ImageButton claer;

    private NewsAdapter adapter;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);

        initView();
        initNews();
        helper = new MyDatabaseHelper(this,"UserDB.db",null,6);

        seeview.setAdapter(adapter);
        seeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newsList.get(position).getNews_url();
                Intent intent = new Intent(SeeActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        claer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsList.clear();
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("See_News",null,null);
                adapter.notifyDataSetChanged();
                db.close();
            }
        });
    }

    private void initView() {
        seeview = (ListView) findViewById(R.id.listview_see);
        claer = (ImageButton)findViewById(R.id.clear_see);
        adapter = new NewsAdapter(this, R.layout.news_item, newsList, this);
    }

    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Dao mdao = new Dao();
//                newsList = mdao.query_collect();
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from See_News", null);
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
                            news_picurl = convertUrlHttp(news_picurl);
                            News news = new News( news_title, news_url, news_picurl, news_date, news_author);
                            newsList.add(0,news);

                        } while (cursor.moveToNext());
                    }
                } else {
//                if (newsList.size() == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "无记录！", Toast.LENGTH_SHORT).show();
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
//                mdao.close_db();
                db.close();
            }
        }).start();
    }

    @Override
    public void click(View view) {

    }

    public String convertUrlHttp(String url){
        if(url == null || url.length() == 0){
            return "";
        }

        return url.replaceFirst("^(?:https:)?//", "http://");
    }
}