package com.smujsj16.ocr_notes.module;

import android.Manifest;
import android.app.AlertDialog;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smujsj16.ocr_notes.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public static File mTmpFile;
    public static Uri imageUri;
    public static String savePath;
    public static String filename;
    public static final int PERMISSIONS_REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    public static final int LOCAL_CROP = 3;// 本地图库
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

                Log.d("test",SignupActivity.userid);//测试是否传过来userid
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
                                savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img"; //图片保存地址
                                if (new File(savePath).exists()) {
                                    try {
                                        new File(savePath).createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                mTmpFile = new File(savePath, filename + ".jpg");
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IndexActivity.CAMERA_REQUEST_CODE) {
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtra("request",CAMERA_REQUEST_CODE);
            startActivity(intent);
          /*  ocrResult.setText("OCR识别中...");
            Bitmap photo = BitmapFactory.decodeFile(IndexActivity.mTmpFile.getAbsolutePath());
            mPresenter.getRecognitionResultByImage(photo);
            cameraphoto.setImageBitmap(photo);*/
        }
        try {
            if (requestCode == LOCAL_CROP) {

                Uri uri = data.getData();
                BitmapFactory.Options option = new BitmapFactory.Options();
                // 属性设置，用于压缩bitmap对象
                option.inSampleSize = 2;
                option.inPreferredConfig = Bitmap.Config.RGB_565;
                // 根据文件流解析生成Bitmap对象
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, option);
                bitmap=ratio(bitmap,480f,240f);
                Intent intent=new Intent(this,MainActivity.class);
                intent.putExtra("request",LOCAL_CROP);
                intent.putExtra("bitmap",bitmap);
                startActivity(intent);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, os);//这里压缩80%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }





}
