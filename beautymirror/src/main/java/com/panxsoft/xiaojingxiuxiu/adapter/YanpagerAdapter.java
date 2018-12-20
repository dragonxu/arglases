package com.panxsoft.xiaojingxiuxiu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.panxsoft.xiaojingxiuxiu.MainActivity;
import com.panxsoft.xiaojingxiuxiu.R;
import com.panxsoft.xiaojingxiuxiu.bean.ProrecyBean;
import com.panxsoft.xiaojingxiuxiu.litemolder.ProductMolder;

import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

public class YanpagerAdapter extends PagerAdapter{
    private Context context;
    List<ProductMolder> probean;
    Onitemclikes onitemclikes;

    public YanpagerAdapter(MainActivity mainActivity, List<ProductMolder> probean) {
        this.context = mainActivity;
        this.probean = probean;
    }

    @Override
    public int getCount() {

        return probean.size()/10+1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_yanduct, null);
        RecyclerView recy = view.findViewById(R.id.recy);
        GridLayoutManager manager = new GridLayoutManager(context,5);
        YanRecyAdapter adapter = new YanRecyAdapter(context,probean,position);
        recy.setLayoutManager(manager);
        recy.setAdapter(adapter);
        adapter.SetOnclikes(new YanRecyAdapter.Onclikes() {
            @Override
            public void btonclikes(int positions) {
                onitemclikes.btitemoclikes(positions);
            }

            @Override
            public void gmonclikes(int positions) {
                onitemclikes.gmitemoclikes(positions);
            }
        });
        container.addView(view);
        return view;
    }

    public interface Onitemclikes{
        void btitemoclikes(int positions);
        void gmitemoclikes(int positions);
    }

    public void SteItemOnclikes(Onitemclikes onitemclikes){
        this.onitemclikes = onitemclikes;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
