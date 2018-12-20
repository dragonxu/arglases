package com.panxsoft.xiaojingxiuxiu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.panxsoft.xiaojingxiuxiu.adapter.DetailsfigureAdapter;
import com.panxsoft.xiaojingxiuxiu.adapter.ParameterAdapter;
import com.panxsoft.xiaojingxiuxiu.adapter.YanjingRecyAdapter;
import com.panxsoft.xiaojingxiuxiu.adapter.YanpagerAdapter;
import com.panxsoft.xiaojingxiuxiu.app.MyApp;
import com.panxsoft.xiaojingxiuxiu.bean.LoadMakeUpBean;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.bean.YanBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.panxsoft.xiaojingxiuxiu.utils.ApplyOkpopuwindow;
import com.panxsoft.xiaojingxiuxiu.utils.Create2DCodeUtil;
import com.panxsoft.xiaojingxiuxiu.utils.Gmethod;
import com.panxsoft.xiaojingxiuxiu.utils.GoumaiPopuwindow;
import com.panxsoft.xiaojingxiuxiu.utils.MD5;
import com.panxsoft.xiaojingxiuxiu.utils.MyHttp;
import com.panxsoft.xiaojingxiuxiu.utils.ProductPopuwindow;
import com.panxsoft.xiaojingxiuxiu.utils.ToastUtil;
import com.panxsoft.xiaojingxiuxiu.utils.VerticalSeekBar;
import com.ruffian.library.widget.RImageView;
import com.unity3d.player.UnityPlayer;
import com.visagetechnologies.visagetrackerunitydemo.CameraActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CameraActivity {
    private static String TAG = "Main" + Gmethod.TAG_plus;
    @BindView(R.id.seekbar)
    VerticalSeekBar seekbar;
    @BindView(R.id.popu)
    ImageView popu;
    @BindView(R.id.particulars)
    RelativeLayout particulars;
    @BindView(R.id.recy)
    RecyclerView recy;
    @BindView(R.id.rel1)
    RelativeLayout rel1;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.right)
    ImageView right;
  /*  @BindView(R.id.shou)
    ImageView shou;
    @BindView(R.id.image)
    RImageView image;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.recy1)
    RecyclerView recy1;
    @BindView(R.id.recy2)
    RecyclerView recy2;
    @BindView(R.id.re2)
    RelativeLayout re2;*/
    @BindView(R.id.loading_iv_top)
    ImageView loadingIvTop;
    @BindView(R.id.id_linear_capture_top)
    LinearLayout idLinearCaptureTop;
    @BindView(R.id.loading_iv_bottm)
    ImageView loadingIvBottm;
    @BindView(R.id.id_linear_capture_under)
    LinearLayout idLinearCaptureUnder;
    @BindView(R.id.rela1)
    RelativeLayout rela1;
    @BindView(R.id.zhezhao)
    ImageView zhezhao;

    private String pagenumber;
    private SimpleDateFormat formathms = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    private RelativeLayout payrl;

    private boolean alipaythreadisrunning = false;
    private boolean weixinthreadisrunning = false;
    private boolean isAlreadyPayOrCancel = false;

    private String AlipayCheckStr = "";
    private String WxpayCheckStr = "";

    private TextView pricetv;

    protected static final int dispHandler_alipaycode_readyng_nonet = 0x450;   // Handle的消息标志：alipaycodeReady联网失败
    protected static final int dispHandler_alipaycode_readyok = 0x452;
    protected static final int dispHandler_alipaycode_readcount = 0x453;
    protected static final int dispHandler_alipaycode_payok = 0x454;   // Handle的消息标志：weixin支付成功了
    private TextView alipaycodetishi;
    public ImageView qrcodealipay;
    private Bitmap qrBitMapalipay;

    protected static final int dispHandler_weixin_readyng_nonet = 0x460;   // Handle的消息标志：weixinReady联网失败
    protected static final int dispHandler_weixin_readyok = 0x462;   // Handle的消息标志：weixinReady联网成功了，显示二维码
    protected static final int dispHandler_weixin_readcount = 0x463; // Handle的消息标志：weixinReady联网轮询用户支付结果
    protected static final int dispHandler_weixin_payok = 0x464;   // Handle的消息标志：weixin支付成功了
    private TextView weixintishi;
    public ImageView qrcodeweixin;
    private Bitmap qrBitMapweixin;

    LoadMakeUpBean loadMakeUpBean;


    private int selectgoods;
   // ProrecyBean probean;
    //页码
    int size;
    //选中商品下标
    int post = 0;

    public Bitmap bitmap;

    private List<YanBean> yanlist = new ArrayList<>();
    //产品集合
    List<ProductMolder> proall;
    List<ProductMolder> proalle;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case 1:

                    loadMakeUpBean.getDrawings().get(0).setPd(1);
                    recy.setVisibility(View.VISIBLE);
                    LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, OrientationHelper.HORIZONTAL, false);
                    YanjingRecyAdapter adapter = new YanjingRecyAdapter(MainActivity.this, loadMakeUpBean);
                    recy.setLayoutManager(manager);
                    recy.setAdapter(adapter);

                    adapter.SetOnclikes(new YanjingRecyAdapter.Onclike() {
                        @Override
                        public void onclike(int position) {
                            UnityPlayer.UnitySendMessage("Scripts", "Makeup_change_drawing", loadMakeUpBean.getTarget()+"|"+loadMakeUpBean.getDrawings().get(position).getIndex());

                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startAnima();
        Log.d("zlc：", getClass().getSimpleName());
        //myApp = (MyApp)getApplication();
        dispHandler.sendEmptyMessageDelayed(20,5000);
        recy.setVisibility(View.GONE);
        payrl = findViewById(R.id.salerl);
        payrl.setVisibility(View.INVISIBLE);

        alipaycodetishi = findViewById(R.id.alipaytishi);
        alipaycodetishi.setText("正在连接服务器...");
        qrcodealipay = findViewById(R.id.gd_qrcode_alipay);
        qrcodealipay.setVisibility(View.INVISIBLE);
        qrBitMapalipay = null;

        weixintishi = findViewById(R.id.weixintishi);
        weixintishi.setText("正在连接服务器...");
        qrcodeweixin = findViewById(R.id.gd_qrcode_weixin);
        qrcodeweixin.setVisibility(View.INVISIBLE);
        qrBitMapweixin = null;

        Intent intent1 = this.getIntent();
       /* final String flag = intent1.getStringExtra("param").split(";")[0];
        Log.d("ceshi1", flag);*/
        selectgoods = intent1.getIntExtra("param",1);
       /* final int price = Integer.parseInt(intent1.getStringExtra("param").split(";")[2]);
        final String goodscode = intent1.getStringExtra("param").split(";")[3];*/
        //  byte[] bis = intent1.getByteArrayExtra("bitmap");
        // bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        post = intent1.getIntExtra("post", 0);

       // probean = (ProrecyBean) intent1.getParcelableExtra("softPro");

        proalle = LitePal.findAll(ProductMolder.class);
        proall = new ArrayList<ProductMolder>();
        proall.clear();
        for(int i=0;i<proalle.size();i++){
            if(proalle.get(i).getUp().equals("1") && proalle.get(i).getInventory() > 0){
                proall.add(proalle.get(i));
            }
        }

        seekbarjudge(proall.get(post).getType(),proall.get(post).getTexts());

        Glide.with(MainActivity.this).load(proall.get(post).getImage()).into(zhezhao);

             for (int i = 0; i <proall .size(); i++) {
                        if (i == post) {
                            proall.get(i).setPd(1);

                        } else {
                           proall.get(i).setPd(0);
                        }
                    }
       /* ProductMolder productMolder = new ProductMolder();
        productMolder.setPd(0);
        productMolder.updateAll("texts = ?",intent1.getStringExtra("param"));*/
        //Log.i(TAG,"selectgoods:"+myApp.getSelectgoods()+";"+myApp.toString());

       /* pricetv = findViewById(R.id.pricetv);
        pricetv.setText(Gmethod.tranFenToSimple(price));*/

        initUnityView();
        initView();

      /*  new Handler().postDelayed(new Runnable() {
            public void run() {
                Log.d("zlcflag",flag);
                UnityPlayer.UnitySendMessage("Scripts", "TryGlasses", flag);
                //UnityPlayer.UnitySendMessage("Scripts", "TryGlasses", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11020.panxbundle+1+");
                //execute the task                                               https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11020.panxbundle

            }

        }, 1000);*/

        findViewById(R.id.toBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePayRL();

                Intent toDisp = new Intent();
                Bundle dataLoading = new Bundle();
                dataLoading.putString("out", "" + 0);
                toDisp.putExtras(dataLoading);
                setResult(1, toDisp);

                MainActivity.this.finish();
            }
        });

        findViewById(R.id.toDelivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toDisp = new Intent(MainActivity.this, DispActivity.class);
                Bundle dataLoading = new Bundle();
                dataLoading.putString("out", "" + selectgoods);
                toDisp.putExtras(dataLoading);
                startActivity(toDisp);

                MainActivity.this.finish();
            }
        });


    /*    findViewById(R.id.toPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"Pay rl",Toast.LENGTH_LONG).show();

                payrl.setVisibility(View.VISIBLE);

                Thread alipayCodeThread = new AlipayCodeThread_Self("10", goodscode,
                        price, true);
                alipayCodeThread.setPriority(Thread.MIN_PRIORITY);
                alipayCodeThread.setName("支付宝二维码支付线程：" + formathms.format(new Date(System.currentTimeMillis())));
                alipayCodeThread.start();


                Thread weixinCodeThread = new WeixinThread_Self("10", goodscode,
                        price, true);
                weixinCodeThread.setPriority(Thread.MIN_PRIORITY);
                weixinCodeThread.setName("微信支付线程：" + formathms.format(new Date(System.currentTimeMillis())));
                weixinCodeThread.start();

            }
        });*/
    }



    /**
     * 播放动画
     */
    private void startAnima() {

        cutting();

        //设置上下播放拉伸图片
        loadingIvTop.setImageBitmap(this.bitmapTop);
        loadingIvBottm.setImageBitmap(this.bitmapBottom);

        rela1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                //设置了图片所以在这里获取他们两个的高,就是执行动画的距离
                int topHeight = loadingIvTop.getHeight();   //id_linear_capture_top   id_linear_capture_under
                int bottonHeight = loadingIvBottm.getHeight()+100;
                ObjectAnimator animator = ObjectAnimator.ofFloat(idLinearCaptureTop, "translationY", 0, -topHeight);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(idLinearCaptureUnder, "translationY", 0, bottonHeight);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(animator).with(animator1);
                animSet.setDuration(4000);
                animSet.start();
                rela1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    Bitmap bitmapTop;
    Bitmap bitmapBottom;

    private void cutting() {

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/sdcard/Note/jietu.png");
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 切割第一个图
        bitmapTop = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 710);
        //且第二个图
        bitmapBottom = Bitmap.createBitmap(bitmap, 0, 710, bitmap.getWidth(), bitmap.getHeight() - 710);
    }

    private void initView() {
   /*     seekbar.setVertical_color(getResources().getColor(R.color.background));//可以设置滑竿的颜色，单一颜色
        seekbar.setDragable(true);//设置是否可以拖动
        seekbar.setCircle_color(getResources().getColor(R.color.background));//设置圆形滑块颜色*/
        seekbar.setMax(1000);
        seekbar.setProgress(0);




       /* //眼妆

        for (int i = 0; i < 5; i++) {
            YanBean bean = new YanBean();
            bean.setPd(0);
            yanlist.add(bean);
        }
        yanlist.get(0).setPd(1);*/



//产品
        YanpagerAdapter adapter1 = new YanpagerAdapter(MainActivity.this, proall);
        pager.setAdapter(adapter1);
        Log.d("zlc$",proall.get(post).getPd()+"");
        if(post<10){
            pager.setCurrentItem(0);
            text.setText("1");
        }else{
            pager.setCurrentItem(post/10);
            text.setText(post/10+1+"");
        }

        adapter1.SteItemOnclikes(new YanpagerAdapter.Onitemclikes() {
            @Override
            public void btitemoclikes(int positions) {
                String s = proall.get(positions).getTexts();
                Log.d("zlc","proall.get(positions).getType():"+proall.get(positions).getType());
                seekbarjudge(proall.get(positions).getType(),s);

            }

            @Override
            public void gmitemoclikes(int positions) {
                final GoumaiPopuwindow popuwindow = new GoumaiPopuwindow(MainActivity.this, proall.get(positions));
                popuwindow.showAsDropDown(pager, 230, 388);
                popuwindow.SetOnclikes(new GoumaiPopuwindow.Onclikes() {
                    @Override
                    public void poonclike() {
                        ApplyOkpopuwindow popu = new ApplyOkpopuwindow(MainActivity.this);
                        popu.showAsDropDown(pager, 230, 388);
                        popuwindow.dismiss();
                    }
                });
            }
        });

        size = proall.size() / 10 + 1;
    }

    //判断是否显示seekbar
    private int pd = 0;

    @OnClick({R.id.popu, R.id.left, R.id.right, R.id.particulars})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.popu:
               /* if (pd == 0) {
                    seekbar.setVisibility(View.VISIBLE);
                    pd = 1;
                } else {
                    seekbar.setVisibility(View.GONE);
                    pd = 0;
                }*/
                UnityPlayer.UnitySendMessage("Scripts", "Makeup_Revoke", "All");
               // UnityPlayer.UnitySendMessage();
                break;
            case R.id.left:
                pagenumber = text.getText().toString();
                int number = Integer.parseInt(pagenumber);
                if (number > 1) {
                    number = number - 1;
                    Log.d("ceshis", number + "");
                    pager.setCurrentItem(number - 1);
                    text.setText(number + "");
                } else {
                    ToastUtil.showToast(MainActivity.this, "没有了");
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
                    ToastUtil.showToast(MainActivity.this, "没有了");
                }
                break;
            case R.id.particulars:

                ObjectAnimator anim = ObjectAnimator.ofFloat(particulars, "alpha", 1f, 0f);
                anim.setDuration(1000);// 动画持续时间
                anim.start();

                particulars.setVisibility(View.GONE);
                ObjectAnimator translationX = new ObjectAnimator().ofFloat(rel1, "translationX", 0, -479f);
                ObjectAnimator translationY = new ObjectAnimator().ofFloat(rel1, "translationY", 0, 0);

                AnimatorSet animatorSet = new AnimatorSet();  //组合动画
                animatorSet.playTogether(translationX, translationY); //设置动画
                animatorSet.setDuration(1000);  //设置动画时间
                animatorSet.start(); //启动

               /* ObjectAnimator translationX1 = new ObjectAnimator().ofFloat(re2, "translationX", 0, -479f);
                ObjectAnimator translationY1 = new ObjectAnimator().ofFloat(re2, "translationY", 0, 0);

                AnimatorSet animatorSet1 = new AnimatorSet();  //组合动画
                animatorSet1.playTogether(translationX1, translationY1); //设置动画
                animatorSet1.setDuration(1000);  //设置动画时间
                animatorSet1.start(); //启动*/

                final ProductPopuwindow popuwindow = new ProductPopuwindow(MainActivity.this,proall,post);
               // popuwindow.showAsDropDown(pager,602,200);
                popuwindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null),Gravity.RIGHT|Gravity.TOP,-602,22);
                popuwindow.setOnDismissListener(mDismissListener);
                popuwindow.SetOnclike(new ProductPopuwindow.Onclikes() {
                    @Override
                    public void onclike() {
                        popuwindow.dismiss();
                    }
                });


                ObjectAnimator translationX4 = new ObjectAnimator().ofFloat(recy, "translationX", 0, -241f);
                ObjectAnimator translationY4 = new ObjectAnimator().ofFloat(recy, "translationY", 0, 0);

                AnimatorSet animatorSet4 = new AnimatorSet();  //组合动画
                animatorSet4.playTogether(translationX4, translationY4); //设置动画
                animatorSet4.setDuration(1000);  //设置动画时间
                animatorSet4.start(); //启动


                break;

        }
    }

    private PopupWindow.OnDismissListener mDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            Log.d("zlc","消失了");
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(particulars, "alpha", 0f, 1f);
            anim1.setDuration(1000);// 动画持续时间
            anim1.start();


            ObjectAnimator translationX2 = new ObjectAnimator().ofFloat(rel1, "translationX", -479f, 0);
            ObjectAnimator translationY2 = new ObjectAnimator().ofFloat(rel1, "translationY", 0, 0);

            AnimatorSet animatorSet2 = new AnimatorSet();  //组合动画
            animatorSet2.playTogether(translationX2, translationY2); //设置动画
            animatorSet2.setDuration(1000);  //设置动画时间
            animatorSet2.start(); //启动

         /*      *//**//* ObjectAnimator translationX3 = new ObjectAnimator().ofFloat(re2, "translationX", -479f, -20f);
            ObjectAnimator translationY3 = new ObjectAnimator().ofFloat(re2, "translationY", 0, 0);

            AnimatorSet animatorSet3 = new AnimatorSet();  //组合动画
            animatorSet3.playTogether(translationX3, translationY3); //设置动画
            animatorSet3.setDuration(1000);  //设置动画时间
            animatorSet3.start(); //启动*//**//**/

            ObjectAnimator translationX5 = new ObjectAnimator().ofFloat(recy, "translationX", -241f, 0);
            ObjectAnimator translationY5 = new ObjectAnimator().ofFloat(recy, "translationY", 0, 0);

            AnimatorSet animatorSet5 = new AnimatorSet();  //组合动画
            animatorSet5.playTogether(translationX5, translationY5); //设置动画
            animatorSet5.setDuration(1000);  //设置动画时间
            animatorSet5.start(); //启动
            particulars.setVisibility(View.VISIBLE);
        }
    };




    // 支付宝二维码支付的线程
    private class AlipayCodeThread_Self extends Thread {
        // 用户选择的轨道编号（10，，，A0，A1，，，K0，，，）
        private String t_selectedTrackNo = "";
        private String t_goodscode = "";
        private int t_pricefen = 1;

        private AlipayCodeThread_Self(String selectedTrackNo, String goodscode, int price, boolean ismain) {
            this.t_selectedTrackNo = (ismain ? "0" : "1") + (selectedTrackNo.length() == 1 ? "0" + selectedTrackNo : selectedTrackNo);
            this.t_goodscode = goodscode;
            this.t_pricefen = price;
        }

        public void run() {
            alipaythreadisrunning = true;

            String qrcodestr = "联网失败";  // 返回二维码字符串
            //String checkstre = ""; // 商户版的支付
            boolean alipayCodeReady = false;

            long timestamp = System.currentTimeMillis();

            String str = "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                    "&timestamp=" + timestamp + "&accesskey=00000000";
            String md5 = MD5.GetMD5Code(str);

            String rtnstr = (new MyHttp()).post(((MyApp) getApplication()).getServerurl() + "/alipayqrcode",
                    "goodscode=" + t_goodscode + "&macid=831AVM000201" + "&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                            "&timestamp=" + timestamp + "&md5=" + md5);

            Log.i(TAG, "连接服务后台第一次，支付宝二维码准备，返回结果：" + rtnstr);

            //{"msg":"","twocode":"https://qr.alipay.com/bax00822jurjljgvdwg0806e",
            // "checkstr":"_input_charset=utf-8&alipay_ca_request=2&out_trade_no=000000011111A0620170223190738&partner=2088511624563106&service=alipay.acquire.query&sign=b7c2e77cf8e3ece3724f2c95bbb1375d&sign_type=MD5",
            // "code":"1",
            // "checkstre":""}

            if (isAlreadyPayOrCancel) {
                alipaythreadisrunning = false;
                return;  // 当用户主动切换支付方式时，则结束线程
            }

            if (rtnstr.length() > 0) {
                // 说明和后台联网正常的
                try {
                    JSONObject soapJson = new JSONObject(rtnstr);
                    if (soapJson.getString("code").equals("0")) {
                        // 取到了正确的结果啦
                        alipayCodeReady = true;
                        qrcodestr = soapJson.getString("qrcode");

                        AlipayCheckStr = soapJson.getString("checkstre");
                    } else {
                        qrcodestr = soapJson.getString("msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                timestamp = System.currentTimeMillis();

                str = "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                        "&timestamp=" + timestamp + "&accesskey=00000000";
                md5 = MD5.GetMD5Code(str);

                rtnstr = (new MyHttp()).post(((MyApp) getApplication()).getServerurl() + "/alipayqrcode",
                        "goodscode=" + t_goodscode + "&macid=831AVM000201" + "&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                                "&timestamp=" + timestamp + "&md5=" + md5);

                Log.i(TAG, "连接服务后台第二次，支付宝二维码准备，返回结果：" + rtnstr);

                if (isAlreadyPayOrCancel) {
                    alipaythreadisrunning = false;
                    return;  // 当用户主动切换支付方式时，则结束线程
                }

                if (rtnstr.length() > 0) {
                    // 说明和后台联网正常的
                    try {
                        JSONObject soapJson = new JSONObject(rtnstr);
                        if (soapJson.getString("code").equals("0")) {
                            // 取到了正确的结果啦
                            alipayCodeReady = true;
                            qrcodestr = soapJson.getString("qrcode");
                            AlipayCheckStr = soapJson.getString("checkstre");
                        } else {
                            qrcodestr = soapJson.getString("msg");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (alipayCodeReady) {
                //LogUtils.v( "alipaypaystr:[" + qrcodestr + "]");


                // 要显示该二维码
                //LogUtils.i( " 支付宝扫码时，取得二维码内容了");
                Message msg = dispHandler.obtainMessage();
                msg.what = dispHandler_alipaycode_readyok;
                msg.obj = qrcodestr;
                dispHandler.sendMessage(msg);


                String AlipayCodeCustomerOpenID = "";
                boolean ispayOK = false;

                if (AlipayCheckStr.length() > 0) {
                    //LogUtils.i("------支付宝V4------isAlreadyPayOrCancel=" + isAlreadyPayOrCancel);
                    while (!isAlreadyPayOrCancel && !ispayOK) {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isAlreadyPayOrCancel) {
                            //LogUtils.i("------支付宝V4--2----isAlreadyPayOrCancel=" + isAlreadyPayOrCancel);
                            alipaythreadisrunning = false;
                            return;  // 当用户主动切换支付方式时，则结束线程
                        }

                        String soaprtn = (new MyHttp()).post("https://openapi.alipay.com/gateway.do", AlipayCheckStr);
                        //LogUtils.i( "alipay2=" + soaprtn);
                        if (isAlreadyPayOrCancel) {
                            alipaythreadisrunning = false;
                            return;  // 当用户主动切换支付方式时，则结束线程
                        }

                        if (soaprtn.length() > 0) {  // 表示有返回的啦
                            try {
                                JSONObject soapJson = new JSONObject(soaprtn);
                                String alipay_trade_query_response = soapJson.getString("alipay_trade_query_response");
                                JSONObject responseJson = new JSONObject(alipay_trade_query_response);
                                String msg2 = responseJson.getString("msg");
                                //LogUtils.i( "支付宝轮询结果msg2：" + msg2);
                                if (msg2.equals("Success")) {
                                    String end = responseJson.getString("trade_status");
                                    Log.i(TAG, "轮询结果end：" + end);
                                    if (end.equals("TRADE_SUCCESS")) {
                                        // {"alipay_trade_query_response":
                                        // {"code":"10000","msg":"Success","buyer_logon_id":"ten***@139.com","buyer_pay_amount":"0.01","buyer_user_id":"2088002093332363",
                                        // "fund_bill_list":[{"amount":"0.01","fund_channel":"ALIPAYACCOUNT"}],
                                        // "invoice_amount":"0.01","out_trade_no":"0test201708000120171019154304003","point_amount":"0.00",
                                        // "receipt_amount":"0.01","send_pay_date":"2017-10-19 15:44:29","total_amount":"0.01",
                                        // "trade_no":"2017101921001004360294517606","trade_status":"TRADE_SUCCESS"},
                                        // "sign":"Gg41Um7DFAighrOYPJZ1jR4YJijUkLxNeEzwVZW7tAAkLQROBrKaBTme965GHmgAsMHiA+dYWwTqD9d8p50PhQ0SodcE3xj6vRHJe0izq/r+79SyQaUad+ODx8ljxt6PTF4onl3+dI8seHcd7ewl1Q7A6BlpLCpty6bKgqDl4Tnho207Si/A7iWQzDSLd6vKreniOrhXPITbZOTeucarnVMFk1vhkSL/fr5T+6Z642NglMyTt2CCL/mXX+p58Xlx7HhxNwVMR4qlmetuqX06AxUgSxTQdsq2xNJOVbpbKxYXcjYs6se8TR1LOLumylYJ3poEB08sJdEWNBkitM5T9Q=="}
                                        ispayOK = true;
                                        // 商户交易号,支付宝交易号,支付者buyer_logon_id,分
                                        AlipayCodeCustomerOpenID = responseJson.getString("out_trade_no") + "," + responseJson.getString("trade_no") + ","
                                                + responseJson.getString("buyer_logon_id") + "," + Gmethod.tranYuanToFen(responseJson.getString("total_amount"));

                                        //LogUtils.i( "------支付宝V4------" + AlipayCodeCustomerOpenID);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        Message msg1 = dispHandler.obtainMessage();
//                        msg1.what = dispHandler_alipaycode_readcount;
//                        if(soaprtn.length() > 0)
//                            msg1.obj = "" ;
//                        else
//                            msg1.obj = "" +"(网络异常)";
//                        dispHandler.sendMessage(msg1);
                    }
                }

                if (ispayOK) {
                    AlipayCheckStr = "";
                    //  支付宝扫码支付成功了，要开始出货啦
                    Message msg2 = dispHandler.obtainMessage();
                    msg2.what = dispHandler_alipaycode_payok;
                    msg2.obj = AlipayCodeCustomerOpenID;
                    dispHandler.sendMessage(msg2);

                }

            } else {

                // 联网失败，提示客户用现金付款
                Log.i(TAG, " 支付宝扫码准备时联网失败");
                Message msg = dispHandler.obtainMessage();
                msg.what = dispHandler_alipaycode_readyng_nonet;
                msg.obj = qrcodestr;
                dispHandler.sendMessage(msg);

            }

            alipaythreadisrunning = false;
        }
    }

    // 微信处理的线程
    private class WeixinThread_Self extends Thread {
        // 用户选择的轨道编号（10，，，A0，A1，，，K0，，，）
        private String t_selectedTrackNo = "";
        private String t_goodscode = "";
        private int t_pricefen = 1;

        /**
         * @param selectedTrackNo 用户选择的轨道编号（10，，，A0，A1，，，K0，，，）
         */
        private WeixinThread_Self(String selectedTrackNo, String goodscode, int price, boolean ismain) {
            this.t_selectedTrackNo = (ismain ? "0" : "1") + (selectedTrackNo.length() == 1 ? "0" + selectedTrackNo : selectedTrackNo);
            ;
            this.t_goodscode = goodscode;
            this.t_pricefen = price;
        }

        public void run() {
            weixinthreadisrunning = true;

            String qrcodestr = "联网失败";  // 返回二维码字符串
            //String checkstr = "";
            boolean weixinCodeReady = false;

            long timestamp = System.currentTimeMillis();

            String str = "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                    "&timestamp=" + timestamp + "&accesskey=00000000";
            String md5 = MD5.GetMD5Code(str);

            String rtnstr = (new MyHttp()).post(((MyApp) getApplication()).getServerurl() + "/wxpayqrcode",
                    "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                            "&timestamp=" + timestamp + "&md5=" + md5);

            Log.i(TAG, "连接服务后台第一次，微信准备，返回结果：" + rtnstr);
            //{"msg":"","checkstr":"<xml><appid>wx6d37f5d05a8d301f<\/appid><mch_id>1219661201<\/mch_id><nonce_str>PQaIr3yc37pFoVHp<\/nonce_str><out_trade_no>000000011111A0720170223193353<\/out_trade_no><sign>B8FC93D8717B61D9F8618FBF3592FB5A<\/sign><\/xml>","code":"1","twocode":"weixin://wxpay/bizpayurl?pr=OJuQQz3"}

            if (isAlreadyPayOrCancel) {
                weixinthreadisrunning = false;
                return;  // 当用户主动切换支付方式时，则结束线程
            }

            if (rtnstr.length() >= 0) {
                try {
                    JSONObject soapJson = new JSONObject(rtnstr);
                    if (soapJson.getString("code").equals("0")) {
                        // 取到了正确的结果啦
                        weixinCodeReady = true;
                        qrcodestr = soapJson.getString("qrcode");

                        WxpayCheckStr = soapJson.getString("selfcheck");
                    } else {
                        qrcodestr = soapJson.getString("msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                // 再试一次
                timestamp = System.currentTimeMillis();

                str = "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                        "&timestamp=" + timestamp + "&accesskey=00000000";
                md5 = MD5.GetMD5Code(str);

                rtnstr = (new MyHttp()).post(((MyApp) getApplication()).getServerurl() + "/wxpayqrcode",
                        "goodscode=" + t_goodscode + "&macid=831AVM000201&price=" + t_pricefen + "&track=" + t_selectedTrackNo +
                                "&timestamp=" + timestamp + "&md5=" + md5);

                Log.i(TAG, "连接服务后台第二次，微信准备，返回结果：" + rtnstr);

                if (isAlreadyPayOrCancel) {
                    weixinthreadisrunning = false;
                    return;  // 当用户主动切换支付方式时，则结束线程
                }

                if (rtnstr.length() >= 0) {
                    try {
                        JSONObject soapJson = new JSONObject(rtnstr);
                        if (soapJson.getString("code").equals("0")) {
                            // 取到了正确的结果啦
                            weixinCodeReady = true;
                            qrcodestr = soapJson.getString("qrcode");
                            WxpayCheckStr = soapJson.getString("selfcheck");
                        } else {
                            qrcodestr = soapJson.getString("msg");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (weixinCodeReady) {

                //LogUtils.v( "weixinpaystr:[" + qrcodestr + "]");

                // 要显示该二维码
                //LogUtils.i( " 微信准备时联网成功");
                Message msg = dispHandler.obtainMessage();
                msg.what = dispHandler_weixin_readyok;
                msg.obj = qrcodestr;
                dispHandler.sendMessage(msg);

                //SystemClock.sleep(10000);

                String WeixinCustomerOpenID = "";
                boolean ispayOK = false;

                if (WxpayCheckStr.length() > 0) {
                    //LogUtils.i("------微信self------isAlreadyPayOrCancel=" + isAlreadyPayOrCancel);
                    while (!isAlreadyPayOrCancel && !ispayOK) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isAlreadyPayOrCancel) {
                            //LogUtils.i("------微信self--2----isAlreadyPayOrCancel=" + isAlreadyPayOrCancel);
                            weixinthreadisrunning = false;
                            return;  // 当用户主动切换支付方式时，则结束线程
                        }

                        String soaprtn = (new MyHttp()).post("https://api.mch.weixin.qq.com/pay/orderquery", WxpayCheckStr);

                        if (isAlreadyPayOrCancel) {
                            weixinthreadisrunning = false;
                            return;  // 当用户主动切换支付方式时，则结束线程
                        }

                        if (soaprtn.length() > 0) {  // 表示有返回的啦
                            //LogUtils.i( "微信轮询结果msg2：" + soaprtn);

                            String return_code = Gmethod.xuan_zhi(soaprtn, "<return_code><![CDATA[", "]]></return_code>");

                            if (return_code.equals("SUCCESS")) {
                                String result_code = Gmethod.xuan_zhi(soaprtn, "<result_code><![CDATA[", "]]></result_code>");
                                if (result_code.equals("SUCCESS")) {
                                    String endresult = Gmethod.xuan_zhi(soaprtn, "<trade_state><![CDATA[", "]]></trade_state>");
                                    Log.i(TAG, "weixin轮询结果1：" + endresult);
                                    if (endresult.equals("SUCCESS")) {
                                        //<xml>
                                        // <return_code><![CDATA[SUCCESS]]></return_code>
                                        // <return_msg><![CDATA[OK]]></return_msg>
                                        // <appid><![CDATA[wx09587b46d66adac6]]></appid>
                                        // <mch_id><![CDATA[1366646502]]></mch_id>
                                        // <nonce_str><![CDATA[XhNXwFTVQXlgiWHg]]></nonce_str>
                                        // <sign><![CDATA[B77054EC5515B277932EDE86551503CE]]></sign>
                                        // <result_code><![CDATA[SUCCESS]]></result_code>
                                        // <openid><![CDATA[oDyIJwmLIFVZrrWXO7WekwYBkdCs]]></openid>
                                        // <is_subscribe><![CDATA[Y]]></is_subscribe>
                                        // <trade_type><![CDATA[NATIVE]]></trade_type>
                                        // <bank_type><![CDATA[CFT]]></bank_type>
                                        // <total_fee>1</total_fee>
                                        // <fee_type><![CDATA[CNY]]></fee_type>
                                        // <transaction_id><![CDATA[4200000002201710199011315664]]></transaction_id>
                                        // <out_trade_no><![CDATA[0test201708000120171019153643003]]></out_trade_no>
                                        // <attach><![CDATA[test2017080001_003_11004]]></attach>
                                        // <time_end><![CDATA[20171019153801]]></time_end>
                                        // <trade_state><![CDATA[SUCCESS]]></trade_state>
                                        // <cash_fee>1</cash_fee>
                                        // </xml>

                                        //LogUtils.i( "Weixin---------------OK--------------" + xuan_zhi(soaprtn, "<transaction_id><![CDATA[", "]]></transaction_id>"));
                                        ispayOK = true;
                                        // 商户交易号,微信交易号,支付者openid,收单的微信appid,是否关注(1为关注着,0为未关注),fen
                                        WeixinCustomerOpenID = Gmethod.xuan_zhi(soaprtn, "<out_trade_no><![CDATA[", "]]></out_trade_no>") + "," +
                                                Gmethod.xuan_zhi(soaprtn, "<transaction_id><![CDATA[", "]]></transaction_id>") + "," +
                                                Gmethod.xuan_zhi(soaprtn, "<openid><![CDATA[", "]]></openid>") + "," +
                                                Gmethod.xuan_zhi(soaprtn, "<appid><![CDATA[", "]]></appid>") + "," +
                                                (Gmethod.xuan_zhi(soaprtn, "<is_subscribe><![CDATA[", "]]></is_subscribe>").equals("Y") ? "1" : "0")
                                                + "," + Gmethod.xuan_zhi(soaprtn, "<cash_fee>", "</cash_fee>");
                                        //LogUtils.i("------微信self------"+WeixinCustomerOpenID);
                                    }

                                }
                                //else {
                                //LogUtils.i( "weixin轮询结果2：" + "err_code:" + xuan_zhi(soaprtn, "<err_code><![CDATA[", "]]></err_code>"));
                                //}
                            }
                            //else {
                            //LogUtils.i( "weixin轮询结果3：" + xuan_zhi(soaprtn, "<return_msg><![CDATA[", "]]></return_msg>"));
                            //}

                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        Message msg1 = dispHandler.obtainMessage();
//                        msg1.what = dispHandler_weixin_readcount;
//                        if (soaprtn.length() > 0)
//                            msg1.obj = "";
//                        else
//                            msg1.obj = "" + "(网络异常)";
//                        dispHandler.sendMessage(msg1);
                    }
                }

                if (ispayOK) {
                    WxpayCheckStr = "";
                    //  微信支付成功了，要开始出货啦
                    Message msg2 = dispHandler.obtainMessage();
                    msg2.what = dispHandler_weixin_payok;
                    msg2.obj = WeixinCustomerOpenID;
                    dispHandler.sendMessage(msg2);

                }

            } else {
                // 联网失败，提示客户用现金付款
                Log.i(TAG, " 微信准备时联网失败");
                Message msg = dispHandler.obtainMessage();
                msg.what = dispHandler_weixin_readyng_nonet;
                msg.obj = qrcodestr;
                dispHandler.sendMessage(msg);
            }

            weixinthreadisrunning = false;
        }
    }

    private void closePayRL() {

//        payrl.setVisibility(View.GONE);

        isAlreadyPayOrCancel = true;

        qrcodealipay.setImageBitmap(null);
        qrcodeweixin.setImageBitmap(null);
        if (qrBitMapalipay != null) {
            if (!qrBitMapalipay.isRecycled()) {
                qrBitMapalipay.recycle();
                qrBitMapalipay = null;
            }
        }
        if (qrBitMapweixin != null) {
            if (!qrBitMapweixin.isRecycled()) {
                qrBitMapweixin.recycle();
                qrBitMapweixin = null;
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


                case dispHandler_alipaycode_readyok:

                    try {
                        qrBitMapalipay = Create2DCodeUtil.Create2DCode(msg.obj.toString(), 300, 300);
                        qrcodealipay.setImageBitmap(qrBitMapalipay);
                        qrcodealipay.setVisibility(View.VISIBLE);
                        alipaycodetishi.setText("请用支付宝的扫一扫支付");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case dispHandler_alipaycode_readyng_nonet:
                    // 支付宝的提示语
                    alipaycodetishi.setText(msg.obj.toString());
                    break;

                case dispHandler_alipaycode_payok:

                    closePayRL();
                {
                    Intent toDisp = new Intent(MainActivity.this, DispActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("out", "" + selectgoods);
                    toDisp.putExtras(dataLoading);
                    startActivity(toDisp);

                    MainActivity.this.finish();
                }

                break;

                case dispHandler_weixin_readyok:
                    try {
                        qrBitMapweixin = Create2DCodeUtil.Create2DCode(msg.obj.toString(), 300, 300);
                        qrcodeweixin.setImageBitmap(qrBitMapweixin);
                        qrcodeweixin.setVisibility(View.VISIBLE);
                        weixintishi.setText("请用微信的扫一扫支付");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case dispHandler_weixin_readyng_nonet:
                    // 微信的提示语
                    weixintishi.setText(msg.obj.toString());

                    break;

                case dispHandler_weixin_payok:
                    closePayRL();
                {
                    Intent toDisp = new Intent(MainActivity.this, DispActivity.class);
                    Bundle dataLoading = new Bundle();
                    dataLoading.putString("out", "" + selectgoods);
                    toDisp.putExtras(dataLoading);
                    startActivity(toDisp);

                    MainActivity.this.finish();
                }

                break;

                case 20:
                    zhezhao.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onLoadMessage(String msg) {
        super.onLoadMessage(msg);

    }

    @Override
    protected void onMakeupSDKInitialized() {
        super.onMakeupSDKInitialized();

    }

    /**
     * 美妆返回数据
     * @param msg
     */
    private void PassJson(String msg) {
        Log.d("zlc","jiexi");
        Gson gson = new Gson();
        loadMakeUpBean = gson.fromJson(msg, LoadMakeUpBean.class);
        handler.sendEmptyMessage(1);
    }



    /**
     *判断seekbar的显示及调用AR的sdk
     * @param type 需要展示的类别
     * @param flag 需要传输给sdk的数据
     */
    private void seekbarjudge(String type, String flag) {
        if(type.equals("Glasses")){
            UnityPlayer.UnitySendMessage("Scripts", "Makeup_Revoke", "All");
            seekbar.setVisibility(View.GONE);
            //眼镜
            UnityPlayer.UnitySendMessage("Scripts", "TryGlasses", flag);

        }else{
            UnityPlayer.UnitySendMessage("Scripts", "Makeup_Revoke", "All");


            //除眼镜外的一切美妆
            UnityPlayer.UnitySendMessage("Scripts", "LoadMakeUp", flag);
        }
    }

    //画法返回
    public void Makeup_Drawings(String str){

        Log.d("zlcz",str);
        if(str != null || !str.equals("")){
            PassJson(str);
        }
    }

    //眼镜返回
    public void SDK2NativeLoadedStateForModel(String msg){
        Log.d("zlcy",msg);

    }


    /**
     * SDK初始化完成，此方法将会被执行
     */
    public void OnTrackerStart() {
        Log.d("zlc","执行了");
    }
    /**
     * SDK追踪状态
     * @param state 的值为：0、1、2、3
     * 0：追踪关闭，即未打开摄像头
     * 1：追踪正常
     * 2：追踪恢复中，即追踪不到人的时候正在尝试检测人脸（此值一般不用于判断处理）
     * 3：追踪正在初始化，即丢失人脸的时长大于1秒（此值一般不用于判断处理）
     */
    public void OnTrackState(int state){
     //   Log.d("zlc",state+"");
    }

    /**
     * 美妆加载的状态回调
     * @param msg 的值有：
     *            （create_material_successful_makeup、change_material_successful_makeup）、
     *            （load_error_makeup、load_finished_makeup、create_material_failed_makeup、change_material_failed_makeup）
     * 以上6个状态的回调，分别是请求加载妆容与切换画法的状态。
     * create_material_successful_makeup为加载妆容成功的状态
     * create_material_failed_makeup为加载妆容失败的状态
     * change_material_successful_makeup为切换画法成功的状态
     * change_material_failed_makeup为切换画法失败的状态
     * load_error_makeup为加载妆容失败的的状态
     * load_finished_makeup为xml解析完成的状态，此状态非加载失败也非加载成功，后续还有create_material_successful_makeup、load_error_makeup、create_material_failed_makeup等状态
     */
    public void Load_Message(String msg) {
        Log.d("zlcok",msg);
    }

    /**
     * 妆容加载状态的log信息回调
     * @param msg 此方法只是用于开发、测试输出妆容加载的情况，为了快速寻找妆容加载失败的问题所在，不用实现什么逻辑代码
     */
    public void Log_Message(String msg) {
        Log.d("zlcno",msg);
    }

    /**
     * 妆容浓淡度的回调
     * @param target ：Eyebrow、Blush、Mouth、Eyeline、Eyeshadow、Eyelash、Powdery
     * @param value ：当前target妆容的当前浓淡度的值
     * 此方法只有“Eyebrow[眉毛]、Blush[腮红]、Mouth[口红]、Eyeline[眼线]、Eyeshadow[眼影]、Eyelash[眼睫毛]、Powdery[底妆/粉底]”这七种类型的妆容才会回调执行；
     * 属性target必须存储处理，用于执行调用“Makeup_AlphaLevel”的方法的参数来改变浓淡度的值
     */
    public  void Makeup_Alpha(final String target, final float value){
        Log.d("zlccode",Math.round(value*1000)+"");

        seekbar.setVisibility(View.VISIBLE);
        seekbar.post(new Runnable(){
            @Override
            public void run() {
               seekbar.setProgress(Math.round(value*1000));
            }
        });

        //seekBar设置监听
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * seekbar改变时的事件监听处理
             * @param seekBar
             * @param progress
             * @param fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ToastUtil.showToast(MainActivity.this, "水平当前进度：" + progress + "%");
                String pro = Math.round(value*1000)+"";
                if(progress == 1000){
                    pro = "1";
                }else if(progress < 100){
                    pro = "0.0"+ progress;
                }else{
                    pro ="0."+ progress;
                }
                UnityPlayer.UnitySendMessage("Scripts", "Makeup_AlphaLevel", target+"|"+pro);

            }

            /**
             * 按住seekbar时的事件监听处理
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("tag", "按住seekbar");
            }

            /**
             * 放开seekbar时的时间监听处理
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("tag", "放开seekbar");
            }
        });
    }

}
