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
import com.example.news.adapter.CommentAdapter;
import com.example.news.entity.Comment;
import com.example.news.util.HttpUtils;
import com.example.news.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements CommentAdapter.CallBack{

    private List<Comment> commentList = new ArrayList<>();

    private ListView comments;

    private CommentAdapter adapter;

    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        helper = new MyDatabaseHelper(this, "UserDB.db", null, 6);
        initView();
        initComment();

        comments.setAdapter(adapter);
        comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = commentList.get(position).getNews_url();
                Intent intent = new Intent(CommentActivity.this, ShowNewsActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

    }

    private void initComment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from Comment", null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            //遍历Cursor对象，取出数据并打印
                            String news_url = cursor.getString(cursor.getColumnIndex("news_url"));
                            String news_title = cursor.getString(cursor.getColumnIndex("news_title"));
                            String news_date = cursor.getString(cursor.getColumnIndex("news_date"));
                            String news_author = cursor.getString(cursor.getColumnIndex("news_author"));
                            String news_picurl = cursor.getString(cursor.getColumnIndex("news_picurl"));
                            String context = cursor.getString(cursor.getColumnIndex("context"));
                            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(news_picurl);
                            Comment con = new Comment(context, bitmap,news_title, news_url, news_picurl, news_date, news_author);
                            commentList.add(0,con);

                        } while (cursor.moveToNext());
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "未发表评论", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                cursor.close();
                db.close();
            }
        }).start();
    }

        private void initView () {
            comments = (ListView) findViewById(R.id.listview_comment);
            adapter = new CommentAdapter(this, R.layout.comment_item, commentList, this);
        }

        @Override
        public void click (View view) {
            int position = Integer.parseInt(view.getTag().toString());
            SQLiteDatabase db = helper.getReadableDatabase();
            db.execSQL("delete from Comment where news_title=?",
                    new String[]{commentList.get(position).getNews_title()});
            db.close();
            commentList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "已删除该评论", Toast.LENGTH_SHORT).show();
        }

}