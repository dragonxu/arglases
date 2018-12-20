package com.panxsoft.xiaojingxiuxiu.litemolder;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Administrator on 2018/12/15.、
 * 轨道
 */

public class PathwayMolder extends LitePalSupport {

    private String code;//轨道号

    private int nummax = 0;//最大存放数
    private int numnow = 0;//现在存放数量

    private String errorcode = "1";  // 1表示没有故障，0表示有故障

    private String mergecode = "1";  //1表示合并后可用的，0表示被合并的

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getMergecode() {
        return mergecode;
    }

    public void setMergecode(String mergecode) {
        this.mergecode = mergecode;
    }
}
