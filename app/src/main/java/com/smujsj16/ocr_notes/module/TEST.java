package com.smujsj16.ocr_notes.module;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.Service.DBService;
import com.smujsj16.ocr_notes.utils.SFTPUtils;

import static com.smujsj16.ocr_notes.utils.PermissionUtils.verifyStoragePermissions;

public class TEST extends Activity implements View.OnClickListener {
    private  final String TAG="TEST";
    private Button buttonUpLoad = null;
    private Button buttonDownLoad = null;
    private SFTPUtils sftp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    public void init(){
        //获取控件对象
        buttonUpLoad = (Button) findViewById(R.id.button_upload);
        buttonDownLoad = (Button) findViewById(R.id.button_download);
        //设置控件对应相应函数
        buttonUpLoad.setOnClickListener(this);
        buttonDownLoad.setOnClickListener(this);
        sftp = new SFTPUtils();
        verifyStoragePermissions(this);//sd卡权限
    }
    public void onClick(final View v) {
        // TODO Auto-generated method stub

        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                switch (v.getId()) {
                    case R.id.button_upload: {
                        //上传文件
                        Log.d(TAG,"上传文件");
                        String localPath = "sdcard/DCIM/";
                        sftp.connect();
                        Log.d(TAG,"连接成功");
                        sftp.uploadFile("16:30.jpg", localPath, "1536484127788.jpg");
                        Log.d(TAG,"上传成功");
                        sftp.disconnect();
                        Log.d(TAG,"断开连接");
                    }
                    break;

                    case R.id.button_download: {

                        int i=DBService.getDbService().createNewUser("13561542957","19980624lc");
                        if(i==-1)
                            Log.d(TAG,"失败");
                        if(i==1)
                            Log.d(TAG,"成功");

                    }
                    break;
                    default:
                        break;
                }
            }
        }.start();
    };
}
