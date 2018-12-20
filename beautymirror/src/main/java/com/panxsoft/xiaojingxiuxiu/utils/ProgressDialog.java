package com.panxsoft.xiaojingxiuxiu.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.R;


/**
 * Created by Administrator on 2017/8/28.
 */

public class ProgressDialog {

    /**
     * 显示处理中的对话框
     * @param context 上下文
     * @param msg 文字tip
     * @return 对象
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.doing_dialog, null);// 得到加载view
        LinearLayout layout =  v.findViewById(R.id.doingdialog_id);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage =  v.findViewById(R.id.img);
        //TextView tipTextView =  v.findViewById(R.id.tipTextView);// 提示文字
        TextView mProcessTV =  v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.doing_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        mProcessTV.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.doing_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        return loadingDialog;

    }
}
