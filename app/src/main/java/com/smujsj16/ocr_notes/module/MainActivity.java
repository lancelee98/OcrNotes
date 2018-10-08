package com.smujsj16.ocr_notes.module;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smujsj16.ocr_notes.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author smujsj16
 * @Description : 主界面Activity,处理View部分
 * @class : MainActivity
 * @time Create at 6/4/2018 4:24 PM
 */


public class MainActivity extends AppCompatActivity implements MainContract.View{

    private Context mContext;

    private ImageView imageView;
    private TextView textView;
    private Button button;
    private  Button button_test;

    private MainPresenter mPresenter;
    File mTmpFile;
    Uri imageUri;

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int LOCAL_CROP = 3;// 本地图库


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button_test=findViewById(R.id.button_test);
        mPresenter = new MainPresenter(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                /*Resources r = mContext.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.test);
                mPresenter.getRecognitionResultByImage(bmp);*/
            }
        });
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TEST.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void updateUI(String s) {
        textView.setText(s);
    }

    private void takePhoto(){
        CharSequence[] items = {"拍照","图库"};
        new AlertDialog.Builder(MainActivity.this)
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
                                    imageUri = FileProvider.getUriForFile(MainActivity.this, authority, mTmpFile);
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

    @Override
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
                textView.setText("OCR识别中...");
                Bitmap photo = BitmapFactory.decodeFile(mTmpFile.getAbsolutePath());
                mPresenter.getRecognitionResultByImage(photo);
                imageView.setImageBitmap(photo);
        }
        try {
            if (requestCode == LOCAL_CROP) {
                textView.setText("OCR识别中...");
                Uri uri = data.getData();
                BitmapFactory.Options option = new BitmapFactory.Options();
                // 属性设置，用于压缩bitmap对象
                option.inSampleSize = 2;
                option.inPreferredConfig = Bitmap.Config.RGB_565;
                // 根据文件流解析生成Bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
                mPresenter.getRecognitionResultByImage(bitmap); //OCR解析
                imageView.setImageBitmap(bitmap);    //展示图片
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
