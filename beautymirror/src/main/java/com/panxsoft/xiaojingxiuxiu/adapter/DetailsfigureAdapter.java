package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.panxsoft.xiaojingxiuxiu.MainActivity;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;

import java.util.List;

/**
 * Created by Administrator on 2018/12/14.
 */

public class DetailsfigureAdapter extends RecyclerView.Adapter<DetailsfigureAdapter.ViewHolder>{

    private Context context;
    private List<ProductMolder> probean;
    int width = 0;// 得到图片宽
    int height = 0;// 得到图片高
    public DetailsfigureAdapter(Context mainActivity, List<ProductMolder> probean) {
        this.context = mainActivity;
        this.probean = probean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_detailsfigure, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("zl",position+"");

        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(479,
                600);
        holder.image.setLayoutParams(params);*/
        Glide.with(context).load(R.drawable.g03051).into(holder.image);
        holder.image.setBackgroundResource(R.drawable.g03042);
        Log.d("zld",width+"");
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
