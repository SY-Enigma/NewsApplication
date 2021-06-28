package com.example.news.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    //创建用户表
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "password text,"
            + "describe text,"
            + "gender text,"
            + "birth text,"
            + "area text,"
            + "job text)";


    //创建新闻表
    public static final String CREATE_ALL_NEWS = "create table All_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";

    //创建IT表
    public static final String CREATE_IT_NEWS = "create table IT_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";


    //创建足球表
    public static final String CREATE_Football_NEWS = "create table football_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";

    //创建国际表
    public static final String CREATE_GJ_NEWS = "create table GJ_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    //创建cba表
    public static final String CREATE_CBA_NEWS = "create table cba_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";

    //创建NBA表
    public static final String CREATE_NBA_NEWS = "create table NBA_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";

    //创建体育表
    public static final String CREATE_SPORT_NEWS = "create table Sport_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    //创建VR表
    public static final String CREATE_VR_NEWS = "create table VR_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";

    //创建视频列表
    public static final String CREATE_VIDEO = "create table Video ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "url text,"
            + "coverurl text,"
            + "author text,"
            + "avatar text)";

    //创建收藏新闻列表
    public static final String CREATE_COLLECTION_NEWS = "create table Collection_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    //创建历史列表
    public static final String CREATE_SEE_NEWS = "create table See_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    //创建点赞新闻表
    public static final String CREATE_GOOD_NEWS = "create table Good_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    //创建用户评论表
    public static final String CREATE_COMMENT = "create table Comment ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text,"
            + "context text)";

    //创建搜索列表
    public static final String CREATE_SEARCH = "create table Search ("
            + "id integer primary key autoincrement,"
            + "name text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        sqLiteDatabase.execSQL(CREATE_ALL_NEWS);

        sqLiteDatabase.execSQL(CREATE_GJ_NEWS);
        sqLiteDatabase.execSQL(CREATE_VR_NEWS);
        sqLiteDatabase.execSQL(CREATE_SPORT_NEWS);
        sqLiteDatabase.execSQL(CREATE_NBA_NEWS);
        sqLiteDatabase.execSQL(CREATE_CBA_NEWS);
        sqLiteDatabase.execSQL(CREATE_IT_NEWS);
        sqLiteDatabase.execSQL(CREATE_Football_NEWS);


        sqLiteDatabase.execSQL(CREATE_VIDEO);
        sqLiteDatabase.execSQL(CREATE_COLLECTION_NEWS);
        sqLiteDatabase.execSQL(CREATE_SEE_NEWS);
        sqLiteDatabase.execSQL(CREATE_GOOD_NEWS);
        sqLiteDatabase.execSQL(CREATE_COMMENT);
        sqLiteDatabase.execSQL(CREATE_SEARCH);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists User");
        sqLiteDatabase.execSQL("drop table if exists All_News");

        sqLiteDatabase.execSQL("drop table if exists GJ_News");
        sqLiteDatabase.execSQL("drop table if exists VR_News");
        sqLiteDatabase.execSQL("drop table if exists Sport_News");
        sqLiteDatabase.execSQL("drop table if exists NBA_News");
        sqLiteDatabase.execSQL("drop table if exists cba_News");
        sqLiteDatabase.execSQL("drop table if exists IT_News");
        sqLiteDatabase.execSQL("drop table if exists football_News");

        sqLiteDatabase.execSQL("drop table if exists Video");
        sqLiteDatabase.execSQL("drop table if exists Collection_News");
        sqLiteDatabase.execSQL("drop table if exists See_News");
        sqLiteDatabase.execSQL("drop table if exists Good_News");
        sqLiteDatabase.execSQL("drop table if exists Comment");
        sqLiteDatabase.execSQL("drop table if exists Search");


        onCreate(sqLiteDatabase);
    }
}
