package com.panxsoft.xiaojingxiuxiu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.adapter.ProselectAdapter;
import com.panxsoft.xiaojingxiuxiu.app.MyApp;
import com.panxsoft.xiaojingxiuxiu.litemolder.PathwayMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;
import com.panxsoft.xiaojingxiuxiu.utils.Gmethod;
import com.panxsoft.xiaojingxiuxiu.utils.PronumberPopuwindow;
import com.panxsoft.xiaojingxiuxiu.utils.ProselectPopuwindow;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockActivity extends Activity {
    private static String TAG = "Stock" + Gmethod.TAG_plus;
    @BindView(R.id.recy)
    RecyclerView recy;
    private MyApp myApp;

    /**
     * 返回
     */
    private Button btnReturn;

    // private ListView listView;

    // 显示的列表
    ArrayList<Map<String, String>> list = null;



    //产品库
    List<ProductlibMolder> parliball;
    //合并完整库
    List<ProductMolder> proall;
    //轨道库
    List<PathwayMolder> patall;

    ProductMolder molders;

    ProselectAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        myApp = (MyApp) getApplication();

        btnReturn = (Button) findViewById(R.id.btn_back);
        // 返回到主菜单
        btnReturn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Intent toMenu = new Intent(StockActivity.this, MenuActivity.class);
                startActivity(toMenu);

                StockActivity.this.finish();
            }
        });


     initDate();
    }

    private void initDate() {
        patall = LitePal.findAll(PathwayMolder.class);

        parliball = LitePal.findAll(ProductlibMolder.class);

        proall = LitePal.findAll(ProductMolder.class);



        LinearLayoutManager manager = new LinearLayoutManager(StockActivity.this);
        recy.setLayoutManager(manager);
        adapter = new ProselectAdapter(StockActivity.this,proall,patall);
        recy.setAdapter(adapter);

        adapter.SetItemOnclike(new ProselectAdapter.ItemOnclike() {
            @Override
            public void proclike(final int positions, String text) {
                final ProselectPopuwindow popuwindow = new ProselectPopuwindow(StockActivity.this,parliball,text);
                popuwindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_stock, null),Gravity.CENTER,0,240);
                popuwindow.SetOnclike(new ProselectPopuwindow.Onckile() {
                    @Override
                    public void onclike(int position) {
                        Log.d("zlc","positions:"+positions);
                        molders = new ProductMolder();
                        molders.setCode(parliball.get(position).getCode());
                        molders.setName(parliball.get(position).getName());
                        molders.setImage(parliball.get(position).getImage());
                        molders.setPd(0);
                        molders.setPrice(parliball.get(position).getPrice());
                        molders.setTexts(parliball.get(position).getTexts());
                        molders.setType(parliball.get(position).getType());
                        molders.setPathwaynumber(patall.get(position).getCode());
                        molders.setUp("1");
                        molders.updateAll("post = ?",positions+"");
                        proall = LitePal.findAll(ProductMolder.class);
                        adapter.update(proall,positions);


                      /*  adapter = new ProselectAdapter(StockActivity.this,proall,patall);
                        recy.setAdapter(adapter);*/
                        popuwindow.dismiss();
                    }
                });

            }

            @Override
            public void invclike(final int positions, String text) {
                final PronumberPopuwindow popuwindows = new PronumberPopuwindow(StockActivity.this,parliball,text);
                popuwindows.showAtLocation(getLayoutInflater().inflate(R.layout.activity_stock, null),Gravity.START|Gravity.TOP,140,160);
                popuwindows.SetOnclike(new PronumberPopuwindow.Onckile() {
                    @Override
                    public void onclike(int position) {
                        molders = new ProductMolder();
                        molders.setInventory(position);
                        molders.updateAll("post = ?",positions+"");
                        proall = LitePal.findAll(ProductMolder.class);
                        adapter.update(proall, positions);
                        popuwindows.dismiss();
                    }
                });
            }
        });
    }




    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed()");

        // 返回按钮的处理，不让程序退出的话，可以注解下面这行代码
        //super.onBackPressed();

    }

}
