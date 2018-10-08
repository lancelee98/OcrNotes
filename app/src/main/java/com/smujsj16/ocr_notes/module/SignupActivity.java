package com.smujsj16.ocr_notes.module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smujsj16.ocr_notes.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private Button back;
    private  Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        back=findViewById(R.id.back_button);
        back.setOnClickListener(this);
        confirm=findViewById(R.id.button_confirm);
        confirm.setOnClickListener(this);
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
                intent=new Intent(this, IndexActivity.class);
                startActivity(intent);
                break;
        }
    }
}
