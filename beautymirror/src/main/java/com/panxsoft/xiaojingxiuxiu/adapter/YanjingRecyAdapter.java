package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.panxsoft.xiaojingxiuxiu.MainActivity;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.LoadMakeUpBean;
import com.panxsoft.xiaojingxiuxiu.bean.YanBean;
import com.panxsoft.xiaojingxiuxiu.utils.ToastUtil;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.helper.RImageViewHelper;

import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

public class YanjingRecyAdapter extends RecyclerView.Adapter<YanjingRecyAdapter.ViewHander>{

    private Context context;

    private LoadMakeUpBean yanlist;

    Onclike onclike;

    private int pd = 1;
    public YanjingRecyAdapter(MainActivity mainActivity, LoadMakeUpBean yanlist) {
        this.context = mainActivity;
        this.yanlist = yanlist;
    }

    @NonNull
    @Override
    public ViewHander onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_yan, parent, false);
        ViewHander viewHander = new ViewHander(inflate);
        return viewHander;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHander holder, final int position) {
        Log.d("22222333",position+"");
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0;i<yanlist.getDrawings().size();i++){
                    if(i == position){
                        yanlist.getDrawings().get(i).setPd(1);
                    }else{
                        yanlist.getDrawings().get(i).setPd(0);
                    }
                }
                onclike.onclike(position);
                notifyDataSetChanged();

            }
        });
        Glide.with(context).load(yanlist.getDrawings().get(position).getTexture()).into(holder.image);
        RImageViewHelper helper = holder.image.getHelper();
        if(yanlist.getDrawings().get(position).getPd() == 1){
            Log.d("22222333",position+"2");
            helper.setBorderColor(Color.parseColor("#AEABEC"));
            helper.setBorderWidth(6);
        }else {
            Log.d("22222333",position+"1");
            holder.image.setBackgroundResource(R.drawable.textview_wubianbianneibaikuang);
            helper.setBorderColor(Color.parseColor("#ffffff"));
            helper.setBorderWidth(1);
        }
    }

    public interface Onclike{
        void onclike(int position);
    }

    public void SetOnclikes(Onclike onclike){
        this.onclike = onclike;
    }

    @Override
    public int getItemCount() {
        return yanlist.getDrawings().size();
    }

    public class ViewHander extends RecyclerView.ViewHolder {
       // RelativeLayout re1;
        RImageView image;
        public ViewHander(View itemView) {
            super(itemView);
           // re1 = itemView.findViewById(R.id.re1);
            image = itemView.findViewById(R.id.image);
        }
    }
}
