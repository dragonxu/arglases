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
import com.panxsoft.xiaojingxiuxiu.StockActivity;
import com.panxsoft.xiaojingxiuxiu.litemolder.PathwayMolder;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19.
 */

public class ProselectAdapter extends RecyclerView.Adapter<ProselectAdapter.ViewHolder> {


    private Context context;
    List<ProductMolder> parliball;
    ItemOnclike itemOnclike;
    List<PathwayMolder> patall;

    public ProselectAdapter(StockActivity stockActivity, List<ProductMolder> parliball, List<PathwayMolder> patall) {
        this.context = stockActivity;
        this.parliball = parliball;
        this.patall = patall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_proselect, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Glide.with(context).load(parliball.get(position).getImage()).into(holder.image);

        holder.code.setText(parliball.get(position).getCode());
        holder.name.setText(parliball.get(position).getName());
        holder.libs.setText(parliball.get(position).getInventory()+"");


        holder.guidao.setText(patall.get(position).getCode());
        holder.huaninv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zlc","holder.libs.getText().toString():"+holder.libs.getText().toString());

                    itemOnclike.invclike(position,holder.libs.getText().toString());


            }
        });
        holder.huanpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zlc","holder.name.getText().toString():"+holder.name.getText().toString());
                itemOnclike.proclike(position,holder.name.getText().toString());
            }
        });

    }

    public interface ItemOnclike{
        /**
         * 换商品
         * @param position
         * @param text
         */
        void proclike(int position, String text);

        /**
         * 换库存
         * @param position
         * @param text
         */
        void invclike(int position, String text);

    }

    public void update(List<ProductMolder> productMolders, int positions){
        this.parliball = productMolders;
        notifyItemChanged(positions);
    }
    public void SetItemOnclike(ItemOnclike itemOnclike){
        this.itemOnclike = itemOnclike;
    }

    @Override
    public int getItemCount() {
        return patall.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView guidao;
        TextView code;
        TextView libs;
        TextView name;
        TextView huanpro;
        TextView huaninv;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            guidao = itemView.findViewById(R.id.guidao);
            code = itemView.findViewById(R.id.code);
            libs = itemView.findViewById(R.id.libs);
            name = itemView.findViewById(R.id.name);
            huanpro = itemView.findViewById(R.id.huanpro);
            huaninv = itemView.findViewById(R.id.huaninv);
        }
    }
}
