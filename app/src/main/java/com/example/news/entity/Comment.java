package com.example.news.entity;

import android.graphics.Bitmap;

public class Comment {
    private Bitmap news_img;
    private String news_title;
    private String news_url;
    private String news_picurl;
    private String context;

    private String uniquekey;
    private String date;
    private String author_name;

    public Comment(){}

    public Comment(String con,String news_title, String news_url, String news_picurl, String date, String author_name) {
        this.context = con;
        this.news_title = news_title;
        this.news_picurl = news_picurl;
        this.date = date;
        this.author_name = author_name;
    }

    public Comment(String con,Bitmap news_img, String news_title, String news_url, String news_picurl, String date, String author_name) {
        this.news_img = news_img;
        this.context = con;
        this.news_title = news_title;
        this.news_url = news_url;
        this.news_picurl = news_picurl;
        this.date = date;
        this.author_name = author_name;
    }

    public Comment(String con,Bitmap news_img, String news_title, String news_url, String news_picurl, String uniquekey, String date, String author_name) {
        this.news_img = news_img;
        this.context = con;
        this.news_title = news_title;
        this.news_url = news_url;
        this.news_picurl = news_picurl;
        this.uniquekey = uniquekey;
        this.date = date;
        this.author_name = author_name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getNews_picurl() {
        return news_picurl;
    }

    public void setNews_picurl(String news_picurl) {
        this.news_picurl = news_picurl;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Bitmap getNews_img() {
        return news_img;
    }

    public void setNews_img(Bitmap news_img) {
        this.news_img = news_img;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

}
