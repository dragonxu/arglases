package com.panxsoft.xiaojingxiuxiu.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.panxsoft.xiaojingxiuxiu.bean.GoodsBean;
import com.panxsoft.xiaojingxiuxiu.bean.TrackBean;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017/8/22.
 */

public class MyApp extends Application {

    //public static MyApp mApplication;

    private final String serverurl = "http://106.14.165.246/macservice";
    //private final String serverurl = "http://192.168.0.141:8080";

    private String mainMacID = "";
    private String mainMacType = "general";  //   "drink" or "general"            “饮料机”，“综合机”
    private String subMacID = "";
    private String accessKey = "";

    private int selectgoods = 0;



    // 7*3 = 21个轨道
    private TrackBean[] trackMainGeneral = new TrackBean[21];
    // 10种商品（眼镜）
    private GoodsBean[] goodsBeanArray = new GoodsBean[12];

//    /**
//     * 本身实例
//     */
//    public static MyApp getInstance() {
//        if (mApplication == null) {
//            mApplication = (MyApp)getInstance()getApplication();
//        }
//        return mApplication;
//    }


    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(getApplicationContext());
//        mApplication = this;

        for(int i=0;i<21;i++){
            trackMainGeneral[i] = new TrackBean();
        }

        for(int i=0;i<12;i++){
            goodsBeanArray[i] = new GoodsBean();
        }

    }



    public TrackBean[] getTrackMainGeneral() {
        return trackMainGeneral;
    }

    public void setTrackMainGeneral(TrackBean[] trackMainGeneral) {
        this.trackMainGeneral = trackMainGeneral;
    }

    public GoodsBean[] getGoodsBeanArray() {
        return goodsBeanArray;
}

    public void setGoodsBeanArray(GoodsBean[] goodsBean) {
        this.goodsBeanArray = goodsBean;
    }


    public String getServerurl() {
        return serverurl;
    }

    public String getMainMacID() {
        return mainMacID;
    }

    public void setMainMacID(String mainMacID) {
        this.mainMacID = mainMacID;
    }

    public String getMainMacType() {
        return mainMacType;
    }

    public void setMainMacType(String mainMacType) {
        this.mainMacType = mainMacType;
    }

    public String getSubMacID() {
        return subMacID;
    }

    public void setSubMacID(String subMacID) {
        this.subMacID = subMacID;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public int getSelectgoods() {
        return selectgoods;
    }

    public void setSelectgoods(int selectgoodsindex) {
        this.selectgoods = selectgoodsindex;
    }
}
