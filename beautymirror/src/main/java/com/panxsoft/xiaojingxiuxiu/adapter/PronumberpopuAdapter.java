package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductlibMolder;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19.
 */

public class PronumberpopuAdapter extends RecyclerView.Adapter<PronumberpopuAdapter.ViewHolder>{

    private Context context;

    Onckile onckile;

    private List<ProductlibMolder> parliball;
    int[] numbers;
    public PronumberpopuAdapter(Context context, List<ProductlibMolder> parliball, int[] numbers) {

        this.context = context;
        this.parliball = parliball;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_pronumberpopu, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ProductlibMolder molder = parliball.get(position);
        holder.text.setText(numbers[position]+"");
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onckile.onclike(numbers[position]);
            }
        });
    }

    public interface Onckile{
        void onclike(int position);
    }
    public void SetOnclike(Onckile onckile){
        this.onckile = onckile;
    }
    @Override
    public int getItemCount() {
        return numbers.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        View bt;
        public ViewHolder(View itemView) {
            super(itemView);
            bt = itemView;
            text = itemView.findViewById(R.id.text);
        }
    }
}
