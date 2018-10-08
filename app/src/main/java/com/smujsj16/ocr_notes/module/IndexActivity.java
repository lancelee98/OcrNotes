package com.smujsj16.ocr_notes.module;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smujsj16.ocr_notes.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private Button menu_button;
    private TextView search_textview;
    private Button take_photo;
    private Button search_button;
    File mTmpFile;
    Uri imageUri;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int LOCAL_CROP = 3;// 本地图库
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
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
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
                break;*///拍照的按钮一直在的问题
               // take_photo.setVisibility(View.VISIBLE);
            default:break;
        }
    }
    private void takePhoto(){
        CharSequence[] items = {"拍照","图库"};
        new AlertDialog.Builder(IndexActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which){
                            // 选择了拍照
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img"; //图片保存地址
                                if (new File(path).exists()) {
                                    try {
                                        new File(path).createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                mTmpFile = new File(path, filename + ".jpg");
                                mTmpFile.getParentFile().mkdirs();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    String authority = getPackageName() + ".provider";
                                    imageUri = FileProvider.getUriForFile(IndexActivity.this, authority, mTmpFile);
                                } else {
                                    imageUri = Uri.fromFile(mTmpFile);
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                break;
                            // 调用系统图库
                            case 1:

                                // 创建Intent，用于打开手机本地图库选择图片
                                Intent intent1 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 启动intent打开本地图库
                                startActivityForResult(intent1,LOCAL_CROP);
                                break;

                        }

                    }
                }).show();


        if (!hasPermission()) {
            return;
        }
    }
    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
            return false;
        }else {
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        return;
                    }
                }
                takePhoto();
            }
        }
    }
}
