package com.example.news.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "password text,"
            + "describe text,"
            + "gender text,"
            + "birth text,"
            + "area text,"
            + "job text)";
    public static final String CREATE_ALL_NEWS = "create table All_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_ANIME_NEWS = "create table Anime_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_ECON_NEWS = "create table Econ_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_ENTE_NEWS = "create table Ente_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_MILI_NEWS = "create table Mili_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_SOCIAL_NEWS = "create table Social_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_SPORT_NEWS = "create table Sport_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_TECH_NEWS = "create table Tech_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_VIDEO = "create table Video ("
            + "id integer primary key autoincrement,"
            + "title text,"
            + "url text,"
            + "coverurl text,"
            + "author text,"
            + "avatar text)";
    public static final String CREATE_COLLECTION_NEWS = "create table Collection_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_SEE_NEWS = "create table See_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_GOOD_NEWS = "create table Good_News ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text)";
    public static final String CREATE_COMMENT = "create table Comment ("
            + "id integer primary key autoincrement,"
            + "news_title text,"
            + "news_date text,"
            + "news_author text,"
            + "news_picurl text,"
            + "news_url text,"
            + "context text)";
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
        sqLiteDatabase.execSQL(CREATE_SEARCH);
        sqLiteDatabase.execSQL(CREATE_COLLECTION_NEWS);
        sqLiteDatabase.execSQL(CREATE_GOOD_NEWS);
        sqLiteDatabase.execSQL(CREATE_COMMENT);
        sqLiteDatabase.execSQL(CREATE_ANIME_NEWS);
        sqLiteDatabase.execSQL(CREATE_ECON_NEWS);
        sqLiteDatabase.execSQL(CREATE_ENTE_NEWS);
        sqLiteDatabase.execSQL(CREATE_MILI_NEWS);
        sqLiteDatabase.execSQL(CREATE_SOCIAL_NEWS);
        sqLiteDatabase.execSQL(CREATE_SPORT_NEWS);
        sqLiteDatabase.execSQL(CREATE_TECH_NEWS);
        sqLiteDatabase.execSQL(CREATE_VIDEO);
        sqLiteDatabase.execSQL(CREATE_SEE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists User");
        sqLiteDatabase.execSQL("drop table if exists All_News");
        sqLiteDatabase.execSQL("drop table if exists Collection_News");
        sqLiteDatabase.execSQL("drop table if exists Good_News");
        sqLiteDatabase.execSQL("drop table if exists Comment");
        sqLiteDatabase.execSQL("drop table if exists Anime_News");
        sqLiteDatabase.execSQL("drop table if exists Econ_News");
        sqLiteDatabase.execSQL("drop table if exists Ente_News");
        sqLiteDatabase.execSQL("drop table if exists Mili_News");
        sqLiteDatabase.execSQL("drop table if exists Social_News");
        sqLiteDatabase.execSQL("drop table if exists Sport_News");
        sqLiteDatabase.execSQL("drop table if exists Tech_News");
        sqLiteDatabase.execSQL("drop table if exists Video");
        sqLiteDatabase.execSQL("drop table if exists Search");
        sqLiteDatabase.execSQL("drop table if exists See_News");
        onCreate(sqLiteDatabase);
    }
}
