package com.example.news.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.R;
import com.example.news.activity.SeeActivity;
import com.example.news.activity.CollectionActivity;
import com.example.news.activity.CommentActivity;
import com.example.news.activity.EditMineActivity;
import com.example.news.activity.GoodActivity;
import com.example.news.activity.LoginOrRegisterActivity;
import com.example.news.util.AlbumUtil;
import com.example.news.util.MyDatabaseHelper;
import com.example.news.util.SharedPreUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;

    private TextView edit_mine, exit_login, collection, good,comment, see,mine_user_name,request_authentication, jinriyuedu;

    private ImageView my_head;

    private static final int CHOSSE_PHOTO = 1;

    private MyDatabaseHelper helper;

    private ImageView top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();

        //        高斯模糊
        Glide.with(this)
                .load(R.drawable.ic_con)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(90)))
                .into(top);

        helper = new MyDatabaseHelper(getContext(), "UserDB.db", null, 5);
        String username = (String) SharedPreUtil.getParam(getContext(), SharedPreUtil.LOGIN_DATA, "");
        mine_user_name.setText(username);

        edit_mine.setOnClickListener(this);

        exit_login.setOnClickListener(this);

        collection.setOnClickListener(this);

        comment.setOnClickListener(this);
        see.setOnClickListener(this);

        my_head.setOnClickListener(this);
        good.setOnClickListener(this);

        try {
            String path = getContext().getCacheDir().getPath();
            String fileName = "user_head";
            File file = new File(path, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
                my_head.setImageBitmap(round);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getContext(), "拒绝许可", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        mine_user_name = view.findViewById(R.id.mine_user_name);
        edit_mine = view.findViewById(R.id.edit_mine);
        exit_login = view.findViewById(R.id.exit_login);
        collection = view.findViewById(R.id.collection);
        my_head = view.findViewById(R.id.my_head);
        top = view.findViewById(R.id.back_top);
        comment = view.findViewById(R.id.commenttext);
        good = view.findViewById(R.id.diazan);
        see = view.findViewById(R.id.mysee);
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOSSE_PHOTO:
                if (resultCode == -1) {
                    String imgPath = AlbumUtil.handleImageOnKitKat(getContext(), data);
                    setHead(imgPath);
                }
                break;
            default:
                break;
        }
    }


    private void setHead(String imgPath) {
        if (imgPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            Bitmap round = AlbumUtil.toRoundBitmap(bitmap);
            try {
                String path = getContext().getCacheDir().getPath();
                File file = new File(path, "user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            my_head.setImageBitmap(round);
        } else {
            Toast.makeText(getContext(), "无法获取图像", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_mine: {
                Intent intent = new Intent(getContext(), EditMineActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.mysee: {
                Intent intent = new Intent(getContext(), SeeActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.diazan: {
                Intent intent = new Intent(getContext(), GoodActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.exit_login: {
                SharedPreUtil.setParam(getContext(), SharedPreUtil.IS_LOGIN, false);
                SharedPreUtil.removeParam(getContext(), SharedPreUtil.LOGIN_DATA);

                //重新跳转到登录页面
                Intent intent = new Intent(getContext(), LoginOrRegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            break;
            case R.id.collection: {
                Intent intent = new Intent(getContext(), CollectionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.commenttext: {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.my_head: {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
            break;
            default:
                break;

        }
    }
}
