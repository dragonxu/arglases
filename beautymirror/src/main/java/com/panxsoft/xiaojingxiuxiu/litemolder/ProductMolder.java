package com.panxsoft.xiaojingxiuxiu.litemolder;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2018/12/15.
 * 商品轨道合并后完整的数据
 */

public class ProductMolder extends LitePalSupport {

    private int id;
    //商品图片
    private int image;
    //商品传给sdk的值
    private String texts;
    //商品选择
    private int pd ;
    //商品类别
    private String type;
    //商品状态
    private String code;
    //商品名称
    private String name;
    //商品价格
    private String price;
    //对应的轨道编号
    private String pathwaynumber;
    //库存
    private int inventory;
    //是否进入列表
    private String up = "0";//0未放入列表，1放入列表
    //列表中的位置
    private String post;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getPathwaynumber() {
        return pathwaynumber;
    }

    public void setPathwaynumber(String pathwaynumber) {
        this.pathwaynumber = pathwaynumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    public int getPd() {
        return pd;
    }

    public void setPd(int pd) {
        this.pd = pd;
    }
}
