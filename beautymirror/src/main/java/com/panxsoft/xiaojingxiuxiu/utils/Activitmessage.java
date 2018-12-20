package com.panxsoft.xiaojingxiuxiu.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-06.
 */

public class Activitmessage {
    private static List<Activity> activitys = new ArrayList<>();

    public static void AddActivity(Activity activity){
        activitys.add(activity);
    }
    public static void RemoveActivity(Activity activity){
        activitys.remove(activity);
    }
    public static void AllActivityFinsh(){
        for (Activity activity:activitys){
            activity.finish();
        }
    }
    public static Boolean isActivity(){
        Log.d("ssssssss",activitys.size()+"");
     if(activitys.size() == 0){
         return true;
     }else{
         return false;
     }

    }
}
