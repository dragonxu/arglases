package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.ListDataBean;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.ruffian.library.widget.RImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/12/12.
 */

class ProRecyAdapter extends RecyclerView.Adapter<ProRecyAdapter.ViewHolder> {


    private Onitemclike onitemclike;
    private Context context;
    private List<ProductMolder> images;
    private int pos;

    public ProRecyAdapter(Context mContext, List<ProductMolder> images, int position) {
        this.context = mContext;
        this.images = images;
        this.pos = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_productes, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d("zlc/",9/9+"");

        Log.d("sssss",pos*9+position+"");

            Glide.with(context).load(images.get(pos*9+position).getImage()).into(holder.image);


        holder.Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    onitemclike.Btonclike(pos*9+position);


            }
        });
    }
    interface Onitemclike{
        void Btonclike(int position);
    }

    public void SetOnitemClike(Onitemclike onitemclike){
        this.onitemclike = onitemclike;
    };
    @Override
    public int getItemCount() {

        if(images.size() < (pos+1)*9){

            return images.size()%9;
        }else {
            return 9;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RImageView image;
        TextView text1;
        TextView text2;
        View Bt;
        public ViewHolder(View itemView) {
            super(itemView);
            Bt = itemView;
            image = itemView.findViewById(R.id.image);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);

        }
    }
}
