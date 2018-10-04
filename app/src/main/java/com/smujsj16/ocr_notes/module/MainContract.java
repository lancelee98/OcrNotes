package com.smujsj16.ocr_notes.module;

import android.graphics.Bitmap;

/**
 * @author smujsj16
 * @Description : MainContract
 * @class : MainContract
 * @time Create at 6/4/2018 4:21 PM
 */


public interface MainContract {

    interface View{
        void updateUI(String s);
    }

    interface Presenter{
        void getAccessToken();
        void getRecognitionResultByImage(Bitmap bitmap);
    }

}
