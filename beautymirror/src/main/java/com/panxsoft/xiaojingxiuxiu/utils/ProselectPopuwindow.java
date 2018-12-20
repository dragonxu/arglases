package com.panxsoft.xiaojingxiuxiu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.adapter.ProselectpopuAdapter;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/12/14.
 * 出货成功
 */

public class ProselectPopuwindow extends PopupWindow {


    Context context;
    View view;
    @BindView(R.id.recy)
    RecyclerView recy;

    List<ProductlibMolder> parliball;
    @BindView(R.id.name)
    TextView name;
    String text;
    Onckile onckile;

    public ProselectPopuwindow(Activity activity, List<ProductlibMolder> parliball, String text) {

        this.context = activity;
        this.parliball = parliball;
        this.text = text;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popu_proselect, null);
        ButterKnife.bind(this, view);
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        /**
         * 设置点击外边可以消失
         */
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        /**
         *设置可以触摸
         */
        // setTouchable(true);


        /**
         * 设置点击外部可以消失
         */


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        name.setText("请选择新的商品（原来的商品名称："+text+"）");
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recy.setLayoutManager(manager);
        ProselectpopuAdapter adapter = new ProselectpopuAdapter(context, parliball);
        recy.setAdapter(adapter);
        adapter.SetOnclike(new ProselectpopuAdapter.Onckile() {
            @Override
            public void onclike(int position) {
                onckile.onclike(position);
            }
        });
    }

    public interface Onckile{
        void onclike(int position);
    }
    public void SetOnclike(Onckile onckile){
        this.onckile = onckile;
    }
}
