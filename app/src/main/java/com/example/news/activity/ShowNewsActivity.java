package com.example.news.activity;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.news.database.Dao;
import com.example.news.R;
import com.example.news.util.MyDatabaseHelper;

/**
 * 加载新闻详情，利用webView显示
 */
public class ShowNewsActivity extends AppCompatActivity {

    private WebView show_news;
    private ImageView collect_news;
    private ImageView good_news;
    private Button comment;
    private EditText context;

    // 添加用户等待显示控件
    private ProgressDialog mDialog;

    private MyDatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        软键盘弹起输入框压缩问题解决：
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_show_news);
        initView();
        helper = new MyDatabaseHelper(this, "UserDB.db", null, 6);
//        Helper = new MyDatabaseHelper(this,"UserDB.db",null,1);

        mDialog = new ProgressDialog(ShowNewsActivity.this);
        mDialog.setMessage("玩命加载ing");
        show_news.setWebViewClient(new WebViewClient() {
            //网页加载时的回调
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }

            }

            //网页停止加载时的回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 如果没有显示，则显示
                if (mDialog.isShowing())
                    mDialog.dismiss();
            }
        });
        show_news.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        final String news_url = intent.getStringExtra("url");
        final String news_title = intent.getStringExtra("title");
        final String news_date = intent.getStringExtra("date");
        final String news_author = intent.getStringExtra("author");
        final String news_picurl = intent.getStringExtra("pic_url");


        show_news.loadUrl(news_url);

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //组装数据
        values.put("news_url", news_url);
        values.put("news_title", news_title);
        values.put("news_date", news_date);
        values.put("news_author", news_author);
        values.put("news_picurl", news_picurl);

        db.insert("See_News", null, values);

        db.close();


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = context.getText().toString();
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("news_url", news_url);
                values.put("news_title", news_title);
                values.put("news_date", news_date);
                values.put("news_author", news_author);
                values.put("news_picurl", news_picurl);
                values.put("context",text);

                db.insert("Comment",null,values);
                db.close();

                Toast.makeText(ShowNewsActivity.this,"评论成功",Toast.LENGTH_LONG).show();
                context.setText("");
            }
        });

        good_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                good_news.setImageResource(R.drawable.dianzan_choose);


                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("news_url", news_url);
                values.put("news_title", news_title);
                values.put("news_date", news_date);
                values.put("news_author", news_author);
                values.put("news_picurl", news_picurl);

                db.insert("Good_News",null,values);
                db.close();

                Toast.makeText(ShowNewsActivity.this,"点赞成功",Toast.LENGTH_LONG).show();
            }
        });

        collect_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collect_news.setImageResource(R.drawable.favorite_selected);


                SQLiteDatabase db = helper.getWritableDatabase();

                ContentValues values = new ContentValues();
                //组装数据
                values.put("news_url", news_url);
                values.put("news_title", news_title);
                values.put("news_date", news_date);
                values.put("news_author", news_author);
                values.put("news_picurl", news_picurl);

                db.insert("Collection_News", null, values);
                db.close();
                Toast.makeText(ShowNewsActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        show_news =findViewById(R.id.show_news);
        collect_news =findViewById(R.id.collect_news);
        good_news = findViewById(R.id.good_news);
        comment = findViewById(R.id.btncomment);
        context = findViewById(R.id.comment);
    }


}
