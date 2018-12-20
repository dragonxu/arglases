package com.visagetechnologies.visagetrackerunitydemo;

/**
 * 穿戴方式选项
 * Created by Huyuzengjian on 2018/1/19.
 */

public class WearWay {
    /**
     * 目标（睫毛、眉毛、眼影、腮红）
     */
    public String target;
    /**
     * 方式序数
     */
    public int way_index;
    /**
     * 方式示意图url
     */
    public String pic_url;
    /**
     * 描述文字
     */
    public String txt;

    /**
     * 构造
     *
     * @param _target 目标
     * @param _index  序数
     */
    public WearWay(String _target, int _index) {
        this.target = _target;
        this.way_index = _index;
    }

    /**
     * 设置示意图url
     *
     * @param _url 示意图url
     * @return
     */
    public WearWay setPic(String _url) {
        this.pic_url = _url;
        return this;
    }

    /**
     * 设置描述文字
     *
     * @param _txt 描述文字
     * @return
     */
    public WearWay setTxt(String _txt) {
        this.txt = _txt;
        return this;
    }

}
