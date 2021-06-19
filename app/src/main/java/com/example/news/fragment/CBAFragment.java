package com.example.news.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.news.entity.LoadListView;
import com.example.news.R;
import com.example.news.activity.ShowNewsActivity;
import com.example.news.adapter.NewsAdapter;
import com.example.news.entity.News;

import com.example.news.util.HttpUtils;
import com.example.news.util.MyBitmapUtils;
import com.example.news.util.MyDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CBAFragment extends Fragment
{

}
