package com.smujsj16.ocr_notes.module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.utils.CheckUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView signup;
    private  TextView forget_passwd;
    private RadioButton remeber_me;
    private EditText phone_num;
    private  EditText passwd;
    private  String moblie;
    private  String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.signup);
        forget_passwd=findViewById(R.id.forget);
        remeber_me=findViewById(R.id.remember_me);
        phone_num=findViewById(R.id.phone_text_l);
        passwd=findViewById(R.id.passwd_text_l);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_login:
                {
                    moblie=phone_num.getText().toString();
                    password=passwd.getText().toString();
                    if(!CheckUtils.isMobile(moblie))
                    {
                        Toast.makeText(this,"手机号码格式错误",Toast.LENGTH_SHORT);
                        break;
                    }
                    if(!CheckUtils.isPassword(password))
                    {
                        Toast.makeText(this,"密码格式错误",Toast.LENGTH_SHORT);
                        break;
                    }

                    new Thread() {
                        @Override
                        public void run() {
                            //这里写入子线程需要做的工作
                        }
                    }.start();
                Intent intent=new Intent(this, TEST.class);
                startActivity(intent);
                }
            case R.id.signup:{
                Intent intent=new Intent(this, SignupActivity.class);
                startActivity(intent);
            }
        }

    }
}
