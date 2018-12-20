package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.panxsoft.xiaojingxiuxiu.NameActivity;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/12/19.
 */

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {




    private Context context;

    private List<ProductlibMolder> proall;

    Onclike onclike;

    public NameAdapter(NameActivity nameActivity, List<ProductlibMolder> proall) {
        this.context = nameActivity;
        this.proall = proall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_name, parent,false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(proall.get(position).getImage()).into(holder.image);
        holder.name.setText(proall.get(position).getName());
        holder.code.setText(proall.get(position).getCode());
        holder.pice.setText(proall.get(position).getPrice());
        holder.piceg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclike.pricclike(position);
            }
        });
        holder.nameg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclike.nameonclike(position);
            }
        });
    }

    public interface Onclike{
        void nameonclike(int position);
        void pricclike(int position);
    }

    public void SetItemclike(Onclike onclike){
        this.onclike = onclike;
    }

    @Override
    public int getItemCount() {
        return proall.size();
    }

    public void update(List<ProductlibMolder> proall, int position){
        this.proall = proall;
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView code;
        TextView pice;
        TextView piceg;
        TextView nameg;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            code = itemView.findViewById(R.id.code);
            pice = itemView.findViewById(R.id.pice);
            piceg = itemView.findViewById(R.id.piceg);
            nameg = itemView.findViewById(R.id.nameg);

        }
    }
}
