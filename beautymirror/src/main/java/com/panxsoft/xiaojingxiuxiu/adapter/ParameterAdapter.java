package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.MainActivity;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;

import java.util.List;

/**
 * Created by Administrator on 2018/12/14.
 */

public class ParameterAdapter extends RecyclerView.Adapter<ParameterAdapter.ViewHolder>{

    private Context context;
    private List<ProductMolder> probean;
    public ParameterAdapter(Context mainActivity, List<ProductMolder> probean) {
        this.context = mainActivity;
        this.probean = probean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_par, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
