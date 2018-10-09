package com.smujsj16.ocr_notes.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.smujsj16.ocr_notes.Entity.Info;
import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.Service.DBService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.smujsj16.ocr_notes.module.IndexActivity.filename;

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
    private  EditText title;


    public static MainPresenter mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        mContext = this;
        cameraphoto = findViewById(R.id.photo);
        ocrResult = findViewById(R.id.ocr_content);
        title=findViewById(R.id.title_input);
        ocrResult.setText("OCR识别中...");
        upload = findViewById(R.id.upload);
        mPresenter = new MainPresenter(this);

        //上传操作
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new Thread() {
                        @Override
                        public void run() {
                            Info info = new Info(SignupActivity.userid, title.getText().toString(), ocrResult.getText().toString(),
                                    IndexActivity.savePath + "/", filename + ".jpg");
                            DBService.getDbService().createNewNotes(info);//新建笔记
                        }
                    }.start();
                    AlertDialog.Builder success = new AlertDialog.Builder(MainActivity.this);
                    success.setMessage("上传成功");
                    success.show();


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
                saveBitmap(rebitmap);//保存选择的图片
            }

        }
    }

    public void saveBitmap(Bitmap bm) {
        filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/img", filename+".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("Test", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
