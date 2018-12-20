package com.panxsoft.xiaojingxiuxiu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.adapter.CarouselPagerAdapter;
import com.panxsoft.xiaojingxiuxiu.adapter.ProPagerAdapter;
import com.panxsoft.xiaojingxiuxiu.app.MyApp;
import com.panxsoft.xiaojingxiuxiu.bean.DataBean;
import com.panxsoft.xiaojingxiuxiu.bean.ListDataBean;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.panxsoft.xiaojingxiuxiu.utils.CarouselViewPager;
import com.panxsoft.xiaojingxiuxiu.utils.Gmethod;
import com.panxsoft.xiaojingxiuxiu.utils.ProgressDialog;
import com.panxsoft.xiaojingxiuxiu.utils.ScreenShot;
import com.panxsoft.xiaojingxiuxiu.utils.ToastUtil;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android_serialport_api.ComTrackMagex;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DispActivity extends Activity implements View.OnClickListener {
    private static final int dispHandler_out_ng = 0x100;
    private static final int dispHandler_out_ok = 0x101;

    private static String TAG = "Disp" + Gmethod.TAG_plus;
    @BindView(R.id.banner)
    CarouselViewPager banner;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.pager)
    ViewPager pager;
    private MyApp myApp;

    //页码
    String pagenumber;
    private int size;

    ImagePagerAdapter adapter;

    String out;

    // 提示对话框
    private Dialog mProgressDialog;
    public static Bitmap bitmap;



  //  ProrecyBean prebean;

    DataBean images;

   // List<ListDataBean> listbean;

    ListDataBean beans;



    List<ProductMolder> Proall;

    List<ProductMolder> Proalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp);
        ButterKnife.bind(this);
        Log.d("ACTIVITY：", getClass().getSimpleName());

        myApp = (MyApp) getApplication();

        mProgressDialog = ProgressDialog.createLoadingDialog(DispActivity.this, "Delivering , Please wait...");

        findViewById(R.id.tomenu).setOnClickListener(this);

        Intent intent1 = this.getIntent();
        out = intent1.getStringExtra("out");
        Log.i(TAG, "收到的out参数：" + out);

        myApp.setSelectgoods(0);

        // 刷新页面
        refresh();

        //轮播图
        initBanner();


        if (!out.equals("0")) {
            // 需要出货啦
            mProgressDialog.show();

            // 根据out的数字，获取最佳的货道信息
            String code = myApp.getGoodsBeanArray()[Integer.parseInt(out) - 1].goodscode;

            String trackno = "11";
            int nowstock = 0;
            int outindex = 0;
            for (int i = 0; i < 21; i++) {
                if (code.equals(myApp.getTrackMainGeneral()[i].getGoodscode()) && myApp.getTrackMainGeneral()[i].getNumnow() > 0) {
                    if (myApp.getTrackMainGeneral()[i].getNumnow() > nowstock) {
                        trackno = myApp.getTrackMainGeneral()[i].getTrackno();
                        nowstock = myApp.getTrackMainGeneral()[i].getNumnow();

                        outindex = i;
                    }
                }
            }
            Log.i(TAG, "出货眼镜序号：" + out + ";轨道号：" + trackno + ";现在库存：" + nowstock);

            final String outTrackno = trackno;
            final int outIndex = outindex;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ComTrackMagex comTrack = new ComTrackMagex("");
                    comTrack.openSerialPort();

                    String[] rtn = comTrack.vend_out_ind(
                            Integer.parseInt(outTrackno.substring(0, 1)), Integer.parseInt(outTrackno.substring(1, 2)), 1,
                            "", "", "");

                    comTrack.closeSerialPort();

                    Log.i(TAG, "出货结果：" + rtn[0]);

                    if (rtn[0].length() > 0) {
                        myApp.getTrackMainGeneral()[outIndex].setNumnow(0);

                        String spstr = myApp.getTrackMainGeneral()[outIndex].getGoodscode() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getGoodsname() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getPrice() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getNummax() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getNumnow() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getErrorcode() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getErrortime();

                        //  goodscode,goodsname,price
                        SharedPreferences sp =
                                DispActivity.this.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();//获取编辑器
                        editor.putString("track" + outTrackno, spstr);  //  goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                        editor.apply();//提交修改

                        Message msg = dispHandler.obtainMessage(dispHandler_out_ng);
                        msg.obj = rtn;
                        dispHandler.sendMessage(msg);


                    } else {
                        myApp.getTrackMainGeneral()[outIndex].setNumnow(myApp.getTrackMainGeneral()[outIndex].getNumnow() - 1);

                        String spstr = myApp.getTrackMainGeneral()[outIndex].getGoodscode() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getGoodsname() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getPrice() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getNummax() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getNumnow() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getErrorcode() + ","
                                + myApp.getTrackMainGeneral()[outIndex].getErrortime();

                        //  goodscode,goodsname,price
                        SharedPreferences sp =
                                DispActivity.this.getSharedPreferences("vmsetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();//获取编辑器
                        editor.putString("track" + outTrackno, spstr);  //  goodscode,goodsname,price,nummax,numnow,errorcode,errortime
                        editor.apply();//提交修改

                        dispHandler.sendEmptyMessage(dispHandler_out_ok);
                    }

                }
            }).start();

        }


    }


    //轮播
    private void initBanner() {

        adapter = new ImagePagerAdapter(DispActivity.this, banner);
        banner.setOffscreenPageLimit(1);
        banner.setAdapter(adapter);
        // 设置轮播时间
        banner.setTimeOut(6);
        // 设置3d效果
        //  lun.setPageTransformer(true, new GalleryTransformer());
        // 设置已经有数据了，可以进行轮播，一般轮播的图片等数据是来源于网络，网络数据来了后才设置此值，此处因为是demo，所以直接赋值了
        banner.setHasData(true);
        // 开启轮播
        banner.startTimer();
    }

    @OnClick({R.id.left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                pagenumber = text.getText().toString();
                int number = Integer.parseInt(pagenumber);
                if (number > 1) {
                    number = number - 1;
                    Log.d("ceshis", number + "");
                    pager.setCurrentItem(number - 1);
                    text.setText(number + "");
                } else {
                    ToastUtil.showToast(DispActivity.this, myApp.getSelectgoods()+"");
                }
                break;
            case R.id.right:
                pagenumber = text.getText().toString();
                int number1 = Integer.parseInt(pagenumber);
                if (number1 < size) {
                    number1 = number1 + 1;
                    text.setText(number1 + "");
                    pager.setCurrentItem(number1 - 1);
                    Log.d("ceshis", number1 + "");
                } else {
                    ToastUtil.showToast(DispActivity.this, "没有了");
                }
                break;
        }
    }




    public class ImagePagerAdapter extends CarouselPagerAdapter<CarouselViewPager> {


        public ImagePagerAdapter(DispActivity context, CarouselViewPager viewPager) {
            super(viewPager);
        }


        @Override
        public Object instantiateRealItem(ViewGroup container, final int position) {
            View localView = View.inflate(DispActivity.this, R.layout.item_barner, null);
            ImageView banners = localView.findViewById(R.id.items_banner);
            container.addView(localView);
            return localView;
        }

        @Override
        public int getRealDataCount() {

            return 3;
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

                case dispHandler_out_ng:
                    mProgressDialog.dismiss();
                    String error = msg.obj.toString();

                    new AlertDialog.Builder(DispActivity.this).setTitle("系统提示")//设置对话框标题
                            .setMessage("抱歉，出错了！错误信息：" + error)//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // 刷新页面
                                    refresh();
                                }
                            })
                            .show();//在按键响应事件中显示此对话框
                    break;

                case dispHandler_out_ok:
                    mProgressDialog.dismiss();
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    private void refresh() {
        // -------------- 刷新页面 ----------------------------



        int j = -1;
      /*  for (int i = 0; i < texts.length; i++) {

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
        Proall = LitePal.findAll(ProductMolder.class);*/
       /* if (listbean != null) {
            size = listbean.size() / 9 + 1;
        }*/

       Proalle = LitePal.findAll(ProductMolder.class);
       Proall = new ArrayList<ProductMolder>();
       Proall.clear();
        for(int i=0;i<Proalle.size();i++){
            if(Proalle.get(i).getUp().equals("1") && Proalle.get(i).getInventory() > 0){
                Log.d("zlc","Proalle.get(i).getUp():"+Proalle.get(i).getUp()+i);
                Proall.add(Proalle.get(i));
            }
        }
        if (Proall != null) {
            size = Proall.size() / 9 + 1;
        }
        initView();
        // -------------- 刷新页面 ----------------------------
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("zlc","stop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("zlc","pause");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("zlc","start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("zlc","Restart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("zlc","Resume");
        myApp.setSelectgoods(0);
    }


    private void initView() {


        ProPagerAdapter proPagerAdapter = new ProPagerAdapter(DispActivity.this, Proall);
        pager.setAdapter(proPagerAdapter);
        proPagerAdapter.SetOnitemclikes(new ProPagerAdapter.Onitemclikes() {
            @Override
            public void onclikes(int position, int positions) {
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(1);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                   // Bundle dataLoading = new Bundle();

                    /**
                     * myApp.getSelectgoods()    ：   url
                     */

//                    dataLoading.putSerializable("softPro", prebean);

                    /**
                     * myApp.getSelectgoods()    ：   url
                     */
//                    dataLoading.putString("param",listbean.get(positions).getTexts()+";");

                    Log.d("zlc","点击了");
                    ToastUtil.showToast(DispActivity.this,"点击了");


                    bitmap = ScreenShot.takeScreenShot(DispActivity.this);

                    try {
                        saveMyBitmap("jietu", bitmap);
                        Log.d("zlc..","zz");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    toMain.putExtra("param", 1);
                    toMain.putExtra("post", positions);
                    // toMain.putExtra("bitmap",bitmapByte);

                   // toMain.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //  toMain.putExtras(dataLoading);
                    startActivityForResult(toMain, 1);

                 //   listbean.clear();
                    // DispActivity.this.finish();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            if (requestCode == 1) {
                out = data.getStringExtra("out");

              //  refresh();
            }
        }
    }

    public void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {
        File f = new File("/sdcard/Note/" + bitName);
        if (!f.exists())
            f.mkdirs();//如果没有这个文件夹的话，会报file not found错误
        f = new File("/sdcard/Note/" + bitName + ".png");
        f.createNewFile();
        try {
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.toString());
        }

    }


    private void setClick(int i) {
        switch (i) {
            case 0:
                findViewById(R.id.goods1).setOnClickListener(this);
                findViewById(R.id.goodss1).setVisibility(View.INVISIBLE);
                break;
            case 1:
                findViewById(R.id.goods2).setOnClickListener(this);
                findViewById(R.id.goodss2).setVisibility(View.INVISIBLE);
                break;
            case 2:
                findViewById(R.id.goods3).setOnClickListener(this);
                findViewById(R.id.goodss3).setVisibility(View.INVISIBLE);
                break;
            case 3:
                findViewById(R.id.goods4).setOnClickListener(this);
                findViewById(R.id.goodss4).setVisibility(View.INVISIBLE);
                break;
            case 4:
                findViewById(R.id.goods5).setOnClickListener(this);
                findViewById(R.id.goodss5).setVisibility(View.INVISIBLE);
                break;
            case 5:
                findViewById(R.id.goods6).setOnClickListener(this);
                findViewById(R.id.goodss6).setVisibility(View.INVISIBLE);
                break;
            case 6:
                findViewById(R.id.goods7).setOnClickListener(this);
                findViewById(R.id.goodss7).setVisibility(View.INVISIBLE);
                break;
            case 7:
                findViewById(R.id.goods8).setOnClickListener(this);
                findViewById(R.id.goodss8).setVisibility(View.INVISIBLE);
                break;
            case 8:
                findViewById(R.id.goods9).setOnClickListener(this);
                findViewById(R.id.goodss9).setVisibility(View.INVISIBLE);
                break;
            case 9:
                findViewById(R.id.goods10).setOnClickListener(this);
                findViewById(R.id.goodss10).setVisibility(View.INVISIBLE);
                break;
            default:
                break;

        }
    }

    private void setUnClick(int i) {
        switch (i) {
            case 0:
                findViewById(R.id.goods1).setOnClickListener(null);
                findViewById(R.id.goodss1).setVisibility(View.VISIBLE);
                break;
            case 1:
                findViewById(R.id.goods2).setOnClickListener(null);
                findViewById(R.id.goodss2).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.goods3).setOnClickListener(null);
                findViewById(R.id.goodss3).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.goods4).setOnClickListener(null);
                findViewById(R.id.goodss4).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.goods5).setOnClickListener(null);
                findViewById(R.id.goodss5).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.goods6).setOnClickListener(null);
                findViewById(R.id.goodss6).setVisibility(View.VISIBLE);
                break;
            case 6:
                findViewById(R.id.goods7).setOnClickListener(null);
                findViewById(R.id.goodss7).setVisibility(View.VISIBLE);
                break;
            case 7:
                findViewById(R.id.goods8).setOnClickListener(null);
                findViewById(R.id.goodss8).setVisibility(View.VISIBLE);
                break;
            case 8:
                findViewById(R.id.goods9).setOnClickListener(null);
                findViewById(R.id.goodss9).setVisibility(View.VISIBLE);
                break;
            case 9:
                findViewById(R.id.goods10).setOnClickListener(null);
                findViewById(R.id.goodss10).setVisibility(View.VISIBLE);
                break;
            default:
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods1:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(1);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();

                    /**
                     * myApp.getSelectgoods()    ：   url
                     */
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11020.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[0].price + ";" + myApp.getGoodsBeanArray()[0].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods2:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(2);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11021.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[1].price + ";" + myApp.getGoodsBeanArray()[1].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods3:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(3);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03042.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[2].price + ";" + myApp.getGoodsBeanArray()[2].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods4:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(4);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03044.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[3].price + ";" + myApp.getGoodsBeanArray()[3].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;


            case R.id.goods5:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(5);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03051.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[4].price + ";" + myApp.getGoodsBeanArray()[4].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods6:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(6);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03060.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[5].price + ";" + myApp.getGoodsBeanArray()[6].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods7:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(7);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05222.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[6].price + ";" + myApp.getGoodsBeanArray()[6].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods8:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(8);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05223.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[7].price + ";" + myApp.getGoodsBeanArray()[7].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods9:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(9);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05224.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[8].price + ";" + myApp.getGoodsBeanArray()[8].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.goods10:
                if (myApp.getSelectgoods() == 0) {
                    myApp.setSelectgoods(10);
                    Intent toMain = new Intent(DispActivity.this, MainActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05226.panxbundle+1+" + ";" + myApp.getSelectgoods() + ";" + myApp.getGoodsBeanArray()[9].price + ";" + myApp.getGoodsBeanArray()[9].goodscode);
                    toMain.putExtras(dataLoading);
                    startActivity(toMain);
                    DispActivity.this.finish();
                }
                break;

            case R.id.tomenu:
                if (myApp.getSelectgoods() == 0) {
                    Intent toStock = new Intent(DispActivity.this, MenuActivity.class);
                    startActivity(toStock);
                    DispActivity.this.finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

        // 返回按钮的处理，不让程序退出的话，可以注解下面这行代码
        //super.onBackPressed();

    }
}
