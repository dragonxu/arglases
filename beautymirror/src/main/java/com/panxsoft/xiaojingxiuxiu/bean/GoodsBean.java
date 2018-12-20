package com.panxsoft.xiaojingxiuxiu.bean;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GoodsBean {

    public String goodscode = "";
    public String goodsname = "";

    public int price = 1000;

    // 存有此商品的货道编号(11,14,17,21,24,27,31,34,37,41,44,47,51,54,57,61,64,67,71,74,77)
    public String mainsaleing = "";  // 有货的轨道编号（有错误的轨道，被当做无货）
    public String mainsaleout = ""; // 有这个商品的轨道编号

}
