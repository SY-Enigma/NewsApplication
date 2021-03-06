package com.example.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.entity.News;

import java.util.List;

/**
 * 新闻适配器
 */
public class NewsAdapter extends ArrayAdapter<News> implements View.OnClickListener{

    private int resourceId;
    private CallBack mCallBack;

    public NewsAdapter(Context context, int textViewResourceId, List<News> objects, CallBack callBack) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mCallBack = callBack;
    }


    public interface CallBack {
        public void click(View view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.newsImg = view.findViewById(R.id.news_item_img);
            viewHolder.newsTitle = view.findViewById(R.id.news_item_title);
            viewHolder.newsDelete = view.findViewById(R.id.delete_item);
            viewHolder.newsAuthor = view.findViewById(R.id.news_item_author);
            viewHolder.newsDate = view.findViewById(R.id.news_item_date);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        Glide.with(getContext())
                .load(news.getNews_picurl())
                .placeholder(R.drawable.onenews)
                .into(viewHolder.newsImg);
        viewHolder.newsTitle.setText(news.getNews_title());
        viewHolder.newsAuthor.setText(news.getAuthor_name());
        viewHolder.newsDate.setText(news.getDate());

        viewHolder.newsDelete.setTag(position);
        viewHolder.newsDelete.setOnClickListener(this);

        return view;
    }


    static class ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsAuthor;
        TextView newsDate;
        ImageView newsDelete;
    }

    @Override
    public void onClick(View view) {
        mCallBack.click(view);
    }
}
