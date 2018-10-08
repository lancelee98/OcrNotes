package com.smujsj16.ocr_notes.module;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.smujsj16.ocr_notes.Entity.Info;
import com.smujsj16.ocr_notes.Entity.User;
import com.smujsj16.ocr_notes.R;
import com.smujsj16.ocr_notes.Service.DBService;
import com.smujsj16.ocr_notes.utils.SFTPUtils;

import java.util.Date;
import java.util.List;

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

                        User user=new User("18916131518","19980624lc");
                        Info info=new Info( "2","ocrnotes", "ocrcontent",
                                "sdcard/DCIM/","1536484127788.jpg");
                        //上传笔记信息，倒数第二个参数为图片在本地的位置，
                        // 倒数第一个参数为，图片的名称

                       // int i=DBService.getDbService().getUserId(user);//获得id
                       // int i=DBService.getDbService().createNewUser(user);//创建用户
                       // int i=DBService.getDbService().checkPassword("18916139519","19980628lc");//检查密码是否正确
                       // int i=DBService.getDbService().createNewNotes(info);//新建笔记
                        List<Info> infoList=DBService.getDbService().selectNotes("null","2");//查询相关笔记
                        for(Info i : infoList)
                        {
                            System.out.println(i.toString());
                        }
                        /*if(i==-1)
                            Log.d(TAG,"失败");
                        if(i==1)
                            Log.d(TAG,"成功");*/

                    }
                    break;
                    default:
                        break;
                }
            }
        }.start();
    };
}
