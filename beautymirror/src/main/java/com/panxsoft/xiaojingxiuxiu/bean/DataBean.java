package com.panxsoft.xiaojingxiuxiu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/12/13.
 */

public class DataBean implements Parcelable {
    private List<ListDataBean> listdate;;

    private int pd = 0;

    protected DataBean(Parcel in) {
        listdate = in.createTypedArrayList(ListDataBean.CREATOR);
        pd = in.readInt();
    }

    public DataBean(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listdate);
        dest.writeInt(pd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel in) {
            return new DataBean(in);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };

    public List<ListDataBean> getListdate() {
        return listdate;
    }

    public void setListdate(List<ListDataBean> listdate) {
        this.listdate = listdate;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }
}
