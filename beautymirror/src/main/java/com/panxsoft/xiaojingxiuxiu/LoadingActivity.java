package com.panxsoft.xiaojingxiuxiu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.app.MyApp;
import com.panxsoft.xiaojingxiuxiu.litemolder.PathwayMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;
import com.panxsoft.xiaojingxiuxiu.utils.BaseActivity;
import com.panxsoft.xiaojingxiuxiu.utils.Gmethod;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;


public class LoadingActivity extends Activity {
    private static String TAG = "Loading" + Gmethod.TAG_plus;
    private MyApp myApp;

    private static final int dispHandler_goto_menu = 0x100;
    private static final int dispHandler_goto_disp = 0x101;
    // 底部的提示语
    private TextView tvhintinfo;

    SQLiteDatabase db;
    ProductMolder productMolder;
    List<ProductMolder> Proall;
    //产品表
    ProductlibMolder molders;

    ProductMolder molderes;

    List<PathwayMolder> patall;

    private int image[] = new int[]{R.drawable.g093,R.drawable.g11020, R.drawable.g11021, R.drawable.g03042, R.drawable.g03044, R.drawable.g03051,
            R.drawable.g03060, R.drawable.g05222, R.drawable.g05223, R.drawable.g05224, R.drawable.g05226,R.drawable.go1};

    private String[] types = {"Eyebrow","Glasses","Glasses","Glasses","Glasses","Glasses","Glasses","Glasses","Glasses","Glasses","Glasses","Blush"};

    private String[] texts = {
            "http://gz.bcebos.com/beauty-mirror/BeautyMirrorAssets/c50/xml/Eyebrow/panx_makeupEyebrow_Dior_093.xml@3",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11020.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11021.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03042.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03044.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03051.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03060.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05222.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05223.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05224.panxbundle+1+",
            "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05226.panxbundle+1+",
            "https://gz.bcebos.com/beauty-mirror/BeautyMirrorAssets/c135/xml/Blush/panx_blush_CARSLAN-01_xml.xml@3"
    };

    String[] codelist = new String[]{"03021","03042","03044","03051","03060","05222","05223","05224","05226","11020","11021","11022"};

    private int[] inv = new int[]{11,14,17,21,24,27,31,34,37,41,44,47,51,54,57,61,64,67,71,74,77};

    PathwayMolder molder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d("ACTIVITY：",getClass().getSimpleName());
        myApp = (MyApp)getApplication();

        db = Connector.getDatabase();
        LitePal.deleteAll(ProductlibMolder.class);
        LitePal.deleteAll(PathwayMolder.class);

        for(int i=0;i<inv.length;i++){
            molder = new PathwayMolder();
            molder.setCode(inv[i]+"");
            molder.setErrorcode("1");
            molder.setMergecode("1");
            molder.setNummax(8);
            molder.setNumnow(0);
            molder.save();
        }
        patall = LitePal.findAll(PathwayMolder.class);
        for (int i = 0;i<texts.length;i++){
            molders = new ProductlibMolder();
            molders.setCode(codelist[i]);
            molders.setName(codelist[i]);
            molders.setImage(image[i]);
            molders.setPd(0);
            molders.setPrice("10");
            molders.setPost(i+"");
            molders.setTexts(texts[i]);
            molders.setType(types[i]);
            molders.setInventory("0");
            molders.save();
        }
        Proall = LitePal.findAll(ProductMolder.class);
        Log.d("zlc","Proall"+Proall.size());
        if(+Proall.size() == 0){
            for (int i = 0;i<patall.size();i++){

                molderes = new ProductMolder();
                molderes.setCode("");
                molderes.setName("");
                molderes.setImage(R.drawable.g093);
                molderes.setPd(0);
                molderes.setPrice("");
                molderes.setTexts("");
                molderes.setType("");
                molderes.setInventory(0);
                molderes.setPathwaynumber("");
                molderes.setUp("0");
                molderes.setPost(i+"");
                molderes.save();
            }

        }


        tvhintinfo = findViewById(R.id.hintinfo);
        tvhintinfo.setText("正在准备基础数据......");

        new Thread(new getSPIfoThread()).start();
    }

    private void dpDate() {
        for (int i = 0; i < texts.length; i++) {

            myApp.getGoodsBeanArray()[i].mainsaleing = "";
            myApp.getGoodsBeanArray()[i].mainsaleout = "";
        }

        for (int i = 0; i < 21; i++) {
/*
            Log.d("zlc*",myApp.getTrackMainGeneral()[i].getNumnow()+"");
            Log.d("zlc#",myApp.getTrackMainGeneral()[i].getGoodscode().length()+"");*/

            if (myApp.getTrackMainGeneral()[i].getNumnow() > 0 && myApp.getTrackMainGeneral()[i].getGoodscode().length() > 0) {
                // 有货
                String code = myApp.getTrackMainGeneral()[i].getGoodscode();
                Log.d("zlc?",code+"");

                for (int k = 0; k < texts.length; k++) {
                    Log.d("zlc=",myApp.getGoodsBeanArray()[k].goodscode+"");
                    if (myApp.getGoodsBeanArray()[k].goodscode.equals(code)) {
                        // 相同的啦
                        if (myApp.getGoodsBeanArray()[k].mainsaleing.length() == 0) {
                            myApp.getGoodsBeanArray()[k].mainsaleing = myApp.getTrackMainGeneral()[i].getTrackno();
                        } else {
                            myApp.getGoodsBeanArray()[k].mainsaleing += "," + myApp.getTrackMainGeneral()[i].getTrackno();
                        }
                    }
                }
            }
        }
        for (int i = 0; i < texts.length; i++) {

            if (myApp.getGoodsBeanArray()[i].mainsaleing.length() > 0) {
                //setClick(i);

                productMolder = new ProductMolder();
                productMolder.setImage(image[i]);
                productMolder.setTexts(texts[i] + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[i].price + ";" + myApp.getGoodsBeanArray()[i].goodscode);
                productMolder.setPd(0);
                productMolder.setType(types[i]);
                productMolder.save();
            }

        }
      //  Proall = LitePal.findAll(ProductMolder.class);
    }


    // 显示上部图片轮询的线程
    private class getSPIfoThread extends Thread{
        @Override
        public void run() {
            /*try {
                SharedPreferences sp =  myApp.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);

                myApp.setMainMacID(sp.getString("mainMacID", "831AVM000201"));
                Log.d("ssssss",sp.getString("mainMacID", "831AVM000201"));
                myApp.setMainMacType(sp.getString("mainMacType", "general"));
                myApp.setSubMacID(sp.getString("subMacID", ""));
                myApp.setAccessKey(sp.getString("accessKey", "00000000"));

                String track11 = sp.getString("track11","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Log.d("ssssss1",track11);
                Gmethod.setTrackInfo(myApp,track11,0,"11");

                String track14 = sp.getString("track14","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track14,1,"14");

                String track17 = sp.getString("track17","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track17,2,"17");

                String track21 = sp.getString("track21","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track21,3,"21");

                String track24 = sp.getString("track24","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track24,4,"24");

                String track27 = sp.getString("track27","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track27,5,"27");

                String track31 = sp.getString("track31","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track31,6,"31");

                String track34 = sp.getString("track34","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track34,7,"34");

                String track37 = sp.getString("track37","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track37,8,"37");

                String track41 = sp.getString("track41","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track41,9,"41");

                String track44 = sp.getString("track44","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track44,10,"44");

                String track47 = sp.getString("track47","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track47,11,"47");

                String track51 = sp.getString("track51","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track51,12,"51");

                String track54 = sp.getString("track54","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track54,13,"54");

                String track57 = sp.getString("track57","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track57,14,"57");

                String track61 = sp.getString("track61","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track61,15,"61");

                String track64 = sp.getString("track64","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track64,16,"64");

                String track67 = sp.getString("track67","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track67,17,"67");

                String track71 = sp.getString("track71","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track71,18,"71");

                String track74 = sp.getString("track74","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track74,19,"74");

                String track77 = sp.getString("track77","");// goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                Gmethod.setTrackInfo(myApp,track77,20,"77");


                for(int i=0;i<texts.length;i++){
                    String goodsinfo = sp.getString("goods"+i,"");// goodscode,goodsname,price
                    Log.i(TAG,"goods"+i + ":"+goodsinfo);
                    if(goodsinfo.length()==0){
                        myApp.getGoodsBeanArray()[i].goodscode = codelist[i];
                        myApp.getGoodsBeanArray()[i].goodsname = codelist[i];
                    } else if(goodsinfo.split(",").length == 3){
                        myApp.getGoodsBeanArray()[i].goodscode = goodsinfo.split(",")[0];
                        myApp.getGoodsBeanArray()[i].goodsname = goodsinfo.split(",")[1];
                        myApp.getGoodsBeanArray()[i].price = Integer.parseInt(goodsinfo.split(",")[2]);
                    }
                }

            } catch (Exception ex){
                ex.printStackTrace();
            }*/

            boolean isSetted = false;
            for(int i=0;i<Proall.size();i++){
                if(Proall.get(i).getUp().equals("1") && Proall.get(i).getInventory() > 0){
                    isSetted = true;
                    break;
                }
            }


            // 暂停2秒
            SystemClock.sleep(1000);

            if(isSetted){
                dispHandler.sendEmptyMessage(dispHandler_goto_disp);

            } else {
                dispHandler.sendEmptyMessage(dispHandler_goto_menu);
            }

        }

    }

    /**
     * Handle线程，显示处理结果
     */
    Handler dispHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            // 处理消息
            switch (msg.what) {

                case dispHandler_goto_disp:
                    Intent toDisp = new Intent(LoadingActivity.this, DispActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("out", "" + myApp.getSelectgoods());
                    toDisp.putExtras(dataLoading);
                    startActivity(toDisp);

                    LoadingActivity.this.finish();
                    break;

                case dispHandler_goto_menu:
                    Intent toMenu = new Intent(LoadingActivity.this, MenuActivity.class);
                    startActivity(toMenu);
                    LoadingActivity.this.finish();
                    break;

                default:
                    break;
            }
            return  false;
        }
    });


}
