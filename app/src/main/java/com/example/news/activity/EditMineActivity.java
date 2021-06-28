package com.example.news.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.fragment.MineFragment;
import com.example.news.util.ApplicationUtil;
import com.example.news.util.MyDatabaseHelper;
import com.example.news.util.SharedPreUtil;

/**
 * 修改密码
 */
public class EditMineActivity extends AppCompatActivity {

    private EditText update_username, update_password, update_repassword;

    private TextView update_user;
    private ImageView icReturn;

    private MyDatabaseHelper dbHelper;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mine);
        initView();

        dbHelper = new MyDatabaseHelper(this, "UserDB.db", null, 6);

        username = (String) SharedPreUtil.getParam(EditMineActivity.this, SharedPreUtil.LOGIN_DATA, "");
        update_username.setText(username);


        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();

            }
        });
        ApplicationUtil.getInstance().addActivity(this);

        icReturn = findViewById(R.id.ic_return);
        icReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EditMineActivity.this, MineFragment.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        update_user =findViewById(R.id.update_user);
        update_username = findViewById(R.id.update_username);
        update_password = findViewById(R.id.update_password);
        update_repassword = findViewById(R.id.update_repassword);

    }

    private void updateUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String update_username_str = update_username.getText().toString();
        String update_password_str = update_password.getText().toString();
        String update_repassword_str = update_repassword.getText().toString();

        if (update_password_str.equals(update_repassword_str)) {
            db.execSQL("update User set name = ?,password = ? where name = ?",
                    new String[]{update_username_str, update_password_str, username});
            Toast.makeText(EditMineActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(EditMineActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }
}