package com.example.news.activity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.example.news.util.ApplicationUtil
import com.example.news.util.SharedPreUtil
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {
    val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === 1) {
                //判断用户是否登录
                val userIsLogin = SharedPreUtil.getParam(
                    this@WelcomeActivity,
                    SharedPreUtil.IS_LOGIN, false
                ) as Boolean
                if (userIsLogin) {
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@WelcomeActivity, LoginOrRegisterActivity::class.java)
                    startActivity(intent)
                }
                finish()
            } else if (msg.what === 0) {
                thread.interrupt()
            }
        }
    }

    val message: Message = Message()
    val thread = Thread {
        try {
            Thread.sleep(3000)
            message.what = 1
            handler.sendMessage(message)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        thread.start()

        btn_jump.setOnClickListener {
//                flag = true;
                message.what = 0
                handler.sendMessage(message)

                //判断用户是否登录
                val userIsLogin = SharedPreUtil.getParam(
                    this@WelcomeActivity,
                    SharedPreUtil.IS_LOGIN, false
                ) as Boolean
                if (userIsLogin) {
                    val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@WelcomeActivity, LoginOrRegisterActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        ApplicationUtil.getInstance().addActivity(this)
    }
}
