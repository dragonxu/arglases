package com.panxsoft.xiaojingxiuxiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.panxsoft.xiaojingxiuxiu.utils.BaseActivity;

public class MenuActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d("ACTIVITY：",getClass().getSimpleName());
        ((Button)findViewById(R.id.toTest)).setOnClickListener(this);

        ((Button)findViewById(R.id.toName)).setOnClickListener(this);
        ((Button)findViewById(R.id.toStock)).setOnClickListener(this);
        ((Button)findViewById(R.id.toBack)).setOnClickListener(this);

        ((Button)findViewById(R.id.toFinish)).setOnClickListener(this);

//        ((Button)findViewById(R.id.toVision11020)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision11021)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision03042)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision03044)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision03051)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision03060)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision05222)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision05223)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision05224)).setOnClickListener(this);
//        ((Button)findViewById(R.id.toVision05226)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.toFinish:

                MenuActivity.this.finish();
                System.exit(0);
                break;


            case R.id.toTest:

                Intent toMenu = new Intent(MenuActivity.this, TestMagexActivity.class);
                startActivity(toMenu);
                MenuActivity.this.finish();

                break;

            case R.id.toName:

                Intent toName = new Intent(MenuActivity.this, NameActivity.class);
                startActivity(toName);
                MenuActivity.this.finish();

                break;

            case R.id.toStock:

                Intent toStock = new Intent(MenuActivity.this, StockActivity.class);
                startActivity(toStock);
                MenuActivity.this.finish();

                break;

            case R.id.toBack:

                Intent toBack = new Intent(MenuActivity.this, LoadingActivity.class);
                startActivity(toBack);
                MenuActivity.this.finish();

                break;

//            case R.id.toVision11020:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11020.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision11021:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_parim_11021.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision03042:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03042.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision03044:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03044.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//
//            case R.id.toVision03051:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03051.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision03060:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls03060.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision05222:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05222.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision05223:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05223.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision05224:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05224.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;
//
//            case R.id.toVision05226:
//            {
//                Intent toMain = new Intent(MenuActivity.this, MainActivity.class);
//                Bundle dataLoading = new Bundle();
//                dataLoading.putString("param", "https://gz.bcebos.com/v1/benyamin/AX_glasses/model/android/panx_lws_ls05226.panxbundle+1+");
//                toMain.putExtras(dataLoading);
//                startActivity(toMain);
//                MenuActivity.this.finish();
//            }
//            break;

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
