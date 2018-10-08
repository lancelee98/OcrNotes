package com.smujsj16.ocr_notes.module;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smujsj16.ocr_notes.R;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private Button menu_button;
    private TextView search_textview;
    private Button take_photo;
    private Button search_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        menu_button =(Button) findViewById(R.id.menu_button);
        menu_button.setOnClickListener(this);
        search_textview=(TextView) findViewById(R.id.search_input);
        search_textview.setOnClickListener(this);
        take_photo=(Button)findViewById(R.id.take_photo);
        search_button=(Button)findViewById(R.id.search_button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.menu_button:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            /*case R.id.search_input:
                take_photo.setVisibility(View.INVISIBLE);
                break;
            case R.id.search_button:
                take_photo.setVisibility(View.VISIBLE);
                break;*///拍照的按钮一直在的问题
            default:break;
        }
    }
}
