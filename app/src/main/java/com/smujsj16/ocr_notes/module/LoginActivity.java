package com.smujsj16.ocr_notes.module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.smujsj16.ocr_notes.Entity.User;
import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.Service.DBService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView signup;
    private  TextView forget_passwd;
    private RadioButton remeber_me;
    private EditText phone_num;
    private  EditText passwd;
    private  String moblie;
    private  String password;
    private Button login;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.signup);
        forget_passwd=findViewById(R.id.forget);
        remeber_me=findViewById(R.id.remember_me);
        phone_num=findViewById(R.id.phone_text_l);
        passwd=findViewById(R.id.passwd_text_l);
        login=findViewById(R.id.button_confirm);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.button_confirm:
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //进行对手机号和密码的判断 传入数据库中进行测试
                        String inputPhoneNum = phone_num.getText().toString();
                        String inputPwd = passwd.getText().toString();
                        int i = DBService.getDbService().checkPassword(inputPhoneNum, inputPwd);//检查手机号密码是否正确
                        //登录成功 跳转到首页
                        if (i == 1) {
                            Log.d("Login", "Success");
                            User user=new User(inputPhoneNum,inputPwd);
                            DBService.getDbService().getUserId(user);
                            SignupActivity.userid=user.getUser_id();//传入登录的userid
                            intent = new Intent(LoginActivity.this, IndexActivity.class);
                            startActivity(intent);
                        }
                        //登录失败显示账号密码有误
                        else {
                            Log.d("Login", "Failed");
                            AlertDialog.Builder error = new AlertDialog.Builder(LoginActivity.this);
                            error.setMessage("手机号或者密码输入有误");
                            error.show();
                        }
                        Looper.loop();
                    }

                }.start();

                break;
            case R.id.signup:
                intent=new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
                default:break;
            /*case R.id.button_login:
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
            }*/
        }

    }
}
