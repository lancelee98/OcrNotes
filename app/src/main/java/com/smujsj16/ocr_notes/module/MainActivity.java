package com.smujsj16.ocr_notes.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.smujsj16.ocr_notes.R;

/**
 * @author smujsj16
 * @Description : 主界面Activity,处理View部分
 * @class : MainActivity
 * @time Create at 6/4/2018 4:24 PM
 */


public class MainActivity extends AppCompatActivity implements MainContract.View{

    private Context mContext;

    public static ImageView cameraphoto;
    public static EditText ocrResult;
    private Button upload;


    public static MainPresenter mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        mContext = this;
        cameraphoto = findViewById(R.id.photo);
        ocrResult = findViewById(R.id.ocr_content);
        ocrResult.setText("OCR识别中...");
        upload = findViewById(R.id.upload);
        mPresenter = new MainPresenter(this);
        //上传操作
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Resources r = mContext.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.test);
                mPresenter.getRecognitionResultByImage(bmp);*/
            }
        });
        showOcrResult();
    }

    @Override
    public void updateUI(String s) {
        ocrResult.setText(s);
    }
    void showOcrResult(){
        int camera = getIntent().getIntExtra("request", -1); //接受IndexActivity传过来的请求值 判断是拍照 Or 图库
        String s =Integer.toString(camera);
        Log.d("request",s);
        //拍照
        if(camera==2){
            Bitmap photo = BitmapFactory.decodeFile(IndexActivity.mTmpFile.getAbsolutePath());
            mPresenter.getRecognitionResultByImage(photo);
            cameraphoto.setImageBitmap(photo);
        }
        //图库
        if(camera==3){
            Intent intent=getIntent();
            if(intent!=null){
                Bitmap rebitmap=intent.getParcelableExtra("bitmap");
                cameraphoto.setImageBitmap(rebitmap);
                mPresenter.getRecognitionResultByImage(rebitmap); //OCR解析
                cameraphoto.setImageBitmap(rebitmap);    //展示图片
            }

        }
    }

}
