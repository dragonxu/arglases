package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;
import com.ruffian.library.widget.RImageView;
import com.ruffian.library.widget.helper.RImageViewHelper;

import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

class YanRecyAdapter extends RecyclerView.Adapter<YanRecyAdapter.ViewHolder> {



    private Context context;
    List<ProductMolder> probean;
    int pos;
    Onclikes onclikes;
    RImageViewHelper helper;


    public YanRecyAdapter(Context context, List<ProductMolder> probean, int position) {
        this.context = context;
        this.probean = probean;
        this.pos = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_yanductes, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(probean.get(pos*10+position).getImage()).into(holder.image);

        helper = holder.image.getHelper();
        // holder.image.setImageResource(probean.getDate().getListdate().get(position).getImage());
        int w = 10;
        if(probean.size() < (pos+1)*10){

            w = probean.size()%10;
        }else {
            w = 10;
        }

        final int finalW = w;
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              for (int i = 0; i< finalW; i++){
                  if(i == position){
                      Log.d("zl","3"+position);
                      probean.get(i).setPd(1);

                  }else{
                      Log.d("zl","4"+position);
                      probean.get(i).setPd(0);
                  }
              }
              notifyDataSetChanged();

                onclikes.btonclikes(pos*10+position);
            }

        });

        if(probean.get(pos*10+position).getPd() == 1){
            holder.image.setBackgroundResource(R.drawable.textview_lanbianneibaikuang);
            holder.goumai.setVisibility(View.VISIBLE);
            helper.setBorderWidth(4);
            helper.setBorderColor(Color.parseColor("#AEABEC"));
            Log.d("zl","1"+position);
        }else{
            holder.image.setBackgroundResource(R.drawable.textview_neibaikuang);
            holder.goumai.setVisibility(View.GONE);
            helper.setBorderWidth(2);
            helper.setBorderColor(Color.parseColor("#E6E6E6"));
            Log.d("zl","2"+position);
        }

        holder.goumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onclikes.gmonclikes(probean.size()/10*10+position);
            }
        });
    }

    public interface Onclikes{
        void btonclikes(int position);
        void gmonclikes(int position);
    }
    public void SetOnclikes(Onclikes onclikes){
        this.onclikes = onclikes;
    }
    @Override
    public int getItemCount() {
        if(probean.size() < (pos+1)*10){

            return probean.size()%10;
        }else {
            return 10;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RImageView image;
        TextView text1;
        TextView text2;
        RelativeLayout goumai;
        View bt;
        public ViewHolder(View itemView) {
            super(itemView);
            bt = itemView;
            image = itemView.findViewById(R.id.image);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            goumai = itemView.findViewById(R.id.goumai);
        }
    }
}
