package com.smujsj16.ocr_notes.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smujsj16.ocr_notes.Entity.User;
import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.Service.DBService;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private Button back;
    private  Button confirm;
    private EditText phone_num;
    private  EditText passwd;
    private  EditText passwdConfirm;
    public static String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        back=findViewById(R.id.back_button);
        back.setOnClickListener(this);
        confirm=findViewById(R.id.button_confirm);
        confirm.setOnClickListener(this);
        phone_num=findViewById(R.id.phone_text_l);
        passwd=findViewById(R.id.passwd_text_l);
        passwdConfirm=findViewById(R.id.passwd_confirm);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_button:
                intent=new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case  R.id.button_confirm:
                Log.d("Sign","PressButton");
                new Thread() {
                    @Override
                    public void run() {
                        //todo 添加新手机号和密码到数据库中

                        String signPhoneNum = phone_num.getText().toString();
                        String signPwd = passwd.getText().toString();
                        String signPwdConfirm = passwdConfirm.getText().toString();
                        Log.d("Sign",signPhoneNum);
                        Log.d("Sign",signPwd);
                        Log.d("Sign",signPwdConfirm);
                   /* if(!signPwd.equals(signPwdConfirm)){
                        AlertDialog.Builder error = new AlertDialog.Builder(SignupActivity.this);
                        error.setMessage("两次密码输入不同");
                        error.show();
                    }*/
                        if (signPhoneNum != "" && signPwd.equals(signPwdConfirm)  && signPwd != "") {
                            Log.d("Sign","Enter");
                            User user = new User(signPhoneNum, signPwd);
                            int i = DBService.getDbService().createNewUser(user);
                            if (i == 1) {
                                Log.d("Sign", "Success");
                                DBService.getDbService().getUserId(user);
                                userid=user.getUser_id();
                                intent = new Intent(SignupActivity.this, IndexActivity.class);
                                startActivity(intent);
                            }
                            if (i == -1) {
                                Log.d("Sign", "Failed");
                            }
                        }
                    }
                }.start();
                break;
        }

    }
}
