package com.bmobapp.bmobchatai;

import android.app.Application;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.ai.BmobAI;

public class BmobApp extends Application {
    public static BmobAI bmobAI;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        Bmob.initialize(this,"e1a1a71e3df8d0da3e65c47379b1f7b4");

        bmobAI = new BmobAI();
    }
}
