package com.example.news.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.entity.Code;
import com.example.news.util.AlbumUtil;
import com.example.news.util.ApplicationUtil;
import com.example.news.util.MyDatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    private String realCode;
    private TextView save_user;
    private ImageView shangchuan_head,fh;
    private EditText username, userpassword, repassword;
    private CheckBox checkBox;
    private EditText et_PhoneCodes;

    private ImageView iv_ShowCode;
    private static final int CHOSSE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 6);

        save_user =(TextView) findViewById(R.id.save_user);
        shangchuan_head =(ImageView) findViewById(R.id.shangchuan_head);
        fh = findViewById(R.id.fh);
        username =(EditText) findViewById(R.id.register_username);
        userpassword =(EditText) findViewById(R.id.register_password);
        repassword =(EditText) findViewById(R.id.register_repassword);
        checkBox =(CheckBox) findViewById(R.id.checkbox_tiaokuan);

        et_PhoneCodes = findViewById(R.id.et_phoneCodes);
        iv_ShowCode = findViewById(R.id.iv_showCode);

        //将验证码用图片的形式显示出来
        iv_ShowCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();

        iv_ShowCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变随机验证码的生成
                iv_ShowCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
            }
        });

        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
        shangchuan_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });

        save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    String username_str = username.getText().toString();
                    String userpassword_str = userpassword.getText().toString();
                    String repassword_str = repassword.getText().toString();
                    String phoneCode = et_PhoneCodes.getText().toString().toLowerCase();

                    if (!TextUtils.isEmpty(username_str) && !TextUtils.isEmpty(repassword_str) && !TextUtils.isEmpty(phoneCode)) {
                        if (phoneCode.equals(realCode)){
                            ContentValues values = new ContentValues();
                            //将用户和密码存入User表中
                            values.put("name", username_str);
                            values.put("password", userpassword_str);
                            db.insert("User", null, values);

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                            Toast.makeText(RegisterActivity.this,"验证通过，注册成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, "验证码错误,注册失败", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }else {
                    Toast.makeText(RegisterActivity.this, "请勾选同意使用条款", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        save_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkBox.isChecked()){
//                    SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//                    String username_str = username.getText().toString();
//                    String userpassword_str = userpassword.getText().toString();
//                    String repassword_str = repassword.getText().toString();
//
//                    if (userpassword_str.equals(repassword_str)) {
//                        ContentValues values = new ContentValues();
//                        //组装数据
//                        values.put("name", username_str);
//                        values.put("password", userpassword_str);
//
//                        db.insert("User", null, values);
//
//                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                        finish();
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
//                    }
//                    db.close();
//                }else {
//                    Toast.makeText(RegisterActivity.this, "请勾选同意使用条款", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });



        ApplicationUtil.getInstance().addActivity(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOSSE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOSSE_PHOTO:
                if (resultCode == -1) {
                    String imgPath = AlbumUtil.handleImageOnKitKat(this, data);
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
                String path = getCacheDir().getPath();
                File file = new File(path,"user_head");
                round.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            shangchuan_head.setImageBitmap(round);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}