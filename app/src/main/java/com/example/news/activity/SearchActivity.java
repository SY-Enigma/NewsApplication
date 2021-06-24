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


public class SearchActivity extends AppCompatActivity implements NewsAdapter.CallBack{

    // 1. 初始化搜索框变量
//    private SearchView searchView;
    private ImageButton back;
    private ImageButton search;
    private EditText context;

    private TextView title;
    private ImageView img;
    private TextView author;
    private TextView date;
    private List<News> newslist = new ArrayList<>();
    private MyDatabaseHelper helper;
//    private LinearLayoutManager manager;
    private NewsAdapter adapter;
    private ListView searchlist;
//    private RecyclerView recyclerView;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        text = intent.getStringExtra("context");
//        newslist = new ArrayList<>();
//        searchView = (SearchView) findViewById(R.id.search_view);
        back = (ImageButton)findViewById(R.id.backtonews);
        search = (ImageButton)findViewById(R.id.searchnews);
        context = (EditText)findViewById(R.id.search_context);
//        recyclerView = (RecyclerView)findViewById(R.id.searchlist);
        helper = new MyDatabaseHelper(this,"UserDB.db",null,6);
        context.setText(text);
        initView();
        initNews();

//        manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
//        adapter = new SearchAdapter(newslist);
//        recyclerView.setAdapter(adapter);
//        getNewsFromDB(text);
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
//                adapter.notifyDataSetChanged();
            }
        });
//        // 4. 设置点击搜索按键后的操作（通过回调接口）
//        // 参数 = 搜索框输入的内容
//        searchView.setOnClickSearch(new ICallBack() {
//            @Override
//            public void SearchAciton(String string) {
//                System.out.println("我收到了" + string);
//                newslist = getNewsFromDB(string);
//            }
//        });
//
//        // 5. 设置点击返回按键后的操作（通过回调接口）
//        searchView.setOnClickBack(new bCallBack() {
//            @Override
//            public void BackAction() {
//                finish();
//            }
//        });

    }

    private void initView() {
        searchlist = (ListView)findViewById(R.id.searchlist);
        adapter = new NewsAdapter(this,R.layout.news_item,newslist,this);
    }

    @Override
    public void click(View view) {
        int position = Integer.parseInt(view.getTag().toString());
//        Intent intent = new Intent(this, ShowNewsActivity.class);
//        intent.putExtra("title", newslist.get(position).getNews_title());
//        intent.putExtra("url", newslist.get(position).getNews_url());
//        intent.putExtra("date", newslist.get(position).getDate());
//        intent.putExtra("author", newslist.get(position).getAuthor_name());
//        intent.putExtra("pic_url", newslist.get(position).getNews_picurl());
//        startActivity(intent);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        db.execSQL("delete from Collection_News where news_title=?",
//                new String[]{newsList.get(position).getNews_title()});
//        db.close();
//        newsList.remove(position);
//        adapter.notifyDataSetChanged();
//        Toast.makeText(this, "该新闻已被移除收藏夹！", Toast.LENGTH_SHORT).show();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//        TextView _title = (TextView)itemView.findViewById(R.id.news_item_title);
//        ImageView _img = (ImageView)itemView.findViewById(R.id.news_item_img);
//        TextView _author = (TextView)itemView.findViewById(R.id.news_item_author);
//        TextView _date = (TextView)itemView.findViewById(R.id.news_item_date);
//    }
//
//    public class SearchAdapter extends RecyclerView.Adapter<ViewHolder> {
//        private List<News> newslist;
//        public SearchAdapter(List<News> list) {
//            this.newslist = list;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            News news = newslist.get(position);
//            holder._title.setText(news.getNews_title());
//            Glide.with(SearchActivity.this)
//                    .load(news.getNews_picurl())
//                    .centerCrop()
//                    .placeholder(R.drawable.mili_pic)
//                    .into(holder._img);
//            holder._author.setText(news.getAuthor_name());
//            holder._date.setText(news.getDate());
//        }
//
//        @Override
//        public int getItemCount() {
//            return newslist.size();
//        }
//    }

    public void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Dao mdao = new Dao();
//                newsList = mdao.query_collect();
                SQLiteDatabase db = helper.getReadableDatabase();
//                String[] con = {"%"+context+"%"};
                String current_sql_sel = "select * from See_News where news_title like '%" + text +"%'";
//                String current_sql_sel = "SELECT * FROM All_News where title like '"+text+"'";
//                Cursor cursor = db.query(true,"All_News",new String[]{"news_url","news_title",
//                "news_date","news_author","news_picurl"},"form",)
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
//                if (newsList.size() == 0){
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
//                mdao.close_db();
            }
        }).start();
    }

    public void getNewsFromDB(String context) {
//        List<News> list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Dao mdao = new Dao();
//                newsList = mdao.query_collect();
                newslist.clear();
                SQLiteDatabase db = helper.getReadableDatabase();
//                String[] con = {"%"+context+"%"};
                Cursor cursor = db.query("See_News",null,"news_title like '%" + text +"%' ",null,null,null,null);
//                String current_sql_sel = "SELECT * FROM All_News where title like '"+context+"'";
//                Cursor cursor = db.query(true,"All_News",new String[]{"news_url","news_title",
//                "news_date","news_author","news_picurl"},"form",)
//                Cursor cursor = db.rawQuery(current_sql_sel,null);
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
//                if (newsList.size() == 0){
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
//                mdao.close_db();
            }
        }).start();
//        return list;
    }

}