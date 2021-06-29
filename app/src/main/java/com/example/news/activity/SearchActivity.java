package com.example.news.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询新闻
 */

public class SearchActivity extends AppCompatActivity implements NewsAdapter.CallBack{

    // 1. 初始化搜索框变量
    private ImageButton back;
    private ImageButton search;
    private EditText context;

    private TextView title;
    private ImageView img;
    private TextView author;
    private TextView date;
    private List<News> newslist = new ArrayList<>();
    private MyDatabaseHelper helper;
    private NewsAdapter adapter;
    private ListView searchlist;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        text = intent.getStringExtra("context");
        back = (ImageButton)findViewById(R.id.backtonews);
        search = (ImageButton)findViewById(R.id.searchnews);
        context = (EditText)findViewById(R.id.search_context);
        helper = new MyDatabaseHelper(this,"UserDB.db",null,6);
        context.setText(text);
        initView();
        initNews();


        searchlist.setAdapter(adapter);


        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newslist.get(position).getNews_url();
                Intent intent = new Intent(SearchActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("text","");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = context.getText().toString();
                getNewsFromDB(text);

            }
        });

    }

    private void initView() {
        searchlist = (ListView)findViewById(R.id.searchlist);
        adapter = new NewsAdapter(this,R.layout.news_item,newslist,this);
    }

    @Override
    public void click(View view) {
        int position = Integer.parseInt(view.getTag().toString());

    }


    public void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                String current_sql_sel = "select * from See_News where news_title like '%" + text +"%'";
                Cursor cursor = db.query("See_News",null,"news_title like '%" + text +"%' ",null,null,null,null);
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
                            newslist.add(news);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "无相关新闻！", Toast.LENGTH_SHORT).show();
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

            }
        }).start();
    }

    public void getNewsFromDB(String context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                newslist.clear();
                SQLiteDatabase db = helper.getReadableDatabase();

                Cursor cursor = db.query("See_News",null,"news_title like '%" + text +"%' ",null,null,null,null);

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
                            newslist.add(news);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "无相关新闻！", Toast.LENGTH_SHORT).show();
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

            }
        }).start();

    }

}