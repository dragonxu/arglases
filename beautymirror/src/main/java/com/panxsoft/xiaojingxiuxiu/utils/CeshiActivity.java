package com.panxsoft.xiaojingxiuxiu.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.panxsoft.xiaojingxiuxiu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/12/17.
 */

public class CeshiActivity extends Activity {
    @BindView(R.id.back)
    Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("zlc", getClass().getSimpleName());
        setContentView(R.layout.activity_ceshi);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
