package com.tron.huanxindemo.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Tron on 2017/2/15.
 */

public class ShowToast {

    public static void show(Activity activity, String content){
        Toast.makeText(activity, content, Toast.LENGTH_SHORT).show();
    }

    public static void showUI(final Activity activity, final String content){

        // 在分线程中activity有可能为空
        if(activity == null) {
            return;
        }

        // 在主线中执行
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(activity, content);
            }
        });

    }

}
