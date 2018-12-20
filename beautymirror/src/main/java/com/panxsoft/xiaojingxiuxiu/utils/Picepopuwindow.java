package com.panxsoft.xiaojingxiuxiu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/12/14.
 * 出货失败
 */

public class Picepopuwindow extends PopupWindow {


    Context context;
    View view;
    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.no)
    TextView no;
    @BindView(R.id.ok)
    TextView ok;

    Onclike onclike;

    public Picepopuwindow(Activity activity) {

        this.context = activity;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popu_pice, null);
        ButterKnife.bind(this,view);
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
       // this.setFocusable(false);
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


    }

    @OnClick({R.id.no, R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.no:
                dismiss();
                break;
            case R.id.ok:
                if(!TextUtils.isEmpty(edit.getText().toString())){
                    onclike.okclike(edit.getText().toString());
                }
                dismiss();
                break;
        }
    }
    public interface Onclike{
        void okclike(String s);
    }
    public void SetOnclike(Onclike onclike){
        this.onclike = onclike;
    }
}
