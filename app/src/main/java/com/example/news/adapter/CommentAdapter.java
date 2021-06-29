package com.example.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.entity.Comment;

import java.util.List;

/**
 * 评论适配器
 */

public class CommentAdapter extends ArrayAdapter<Comment> implements View.OnClickListener{

    private int resourceId;
    private CallBack mCallBack;

    public CommentAdapter(Context context, int textViewResourceId, List<Comment> objects, CommentAdapter.CallBack callBack) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mCallBack = callBack;
    }


    public interface CallBack {
        public void click(View view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        View view;
        CommentAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new CommentAdapter.ViewHolder();
            viewHolder.newsImg = view.findViewById(R.id.news_item_img_c);
            viewHolder.newsTitle = view.findViewById(R.id.news_item_title_c);
            viewHolder.newsDelete = view.findViewById(R.id.delete_item_c);
            viewHolder.newsAuthor = view.findViewById(R.id.news_item_author_c);
            viewHolder.newsDate = view.findViewById(R.id.news_item_date_c);
            viewHolder.comment = view.findViewById(R.id.context);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (CommentAdapter.ViewHolder) view.getTag();
        }

        viewHolder.newsImg.setImageBitmap(comment.getNews_img());
        viewHolder.newsTitle.setText(comment.getNews_title());
        viewHolder.newsAuthor.setText(comment.getAuthor_name());
        viewHolder.newsDate.setText(comment.getDate());
        viewHolder.comment.setText(comment.getContext());

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
        TextView comment;
    }

    @Override
    public void onClick(View view) {
        mCallBack.click(view);
    }
}
