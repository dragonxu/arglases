package com.panxsoft.xiaojingxiuxiu.bean;

/**
 * Created by Administrator on 2017/8/22.
 */

public class TrackBean {

    private String trackno = "";  // AR眼镜，固定为11,14,17,21,24,27,31,34,37,41,44,47,51,54,57,61,64,67,71,74,77

    private String goodscode = "";
    private String goodsname = "";

    private int price = 1000;

    private int nummax = 0;
    private int numnow = 0;

    private String errorcode = "";  // 空值表示没有故障
    private String errortime = "";  // yyyymmddhhmmss

    public String getTrackno() {
        return trackno;
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }

    public String getGoodscode() {
        return goodscode;
    }

    public void setGoodscode(String goodscode) {
        this.goodscode = goodscode;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNummax() {
        return nummax;
    }

    public void setNummax(int nummax) {
        this.nummax = nummax;
    }

    public int getNumnow() {
        return numnow;
    }

    public void setNumnow(int numnow) {
        this.numnow = numnow;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrortime() {
        return errortime;
    }

    public void setErrortime(String errortime) {
        this.errortime = errortime;
    }
}
