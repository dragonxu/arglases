package com.panxsoft.xiaojingxiuxiu.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

public class ProrecyBean implements Parcelable{

    private DataBean date;

    protected ProrecyBean(Parcel in) {
        date = in.readParcelable(DataBean.class.getClassLoader());
    }

    public ProrecyBean() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(date, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProrecyBean> CREATOR = new Creator<ProrecyBean>() {
        @Override
        public ProrecyBean createFromParcel(Parcel in) {
            return new ProrecyBean(in);
        }

        @Override
        public ProrecyBean[] newArray(int size) {
            return new ProrecyBean[size];
        }
    };

    public DataBean getDate() {
        return date;
    }

    public void setDate(DataBean date) {
        this.date = date;
    }

}
