package com.panxsoft.xiaojingxiuxiu.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/12/18.
 */

public class LoadMakeUpBean {


    /**
     * target : Eyebrow
     * drawings : [{"index":0,"id":"296","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_028_img.jpg"},{"index":1,"id":"291","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_023_img.jpg"},{"index":2,"id":"289","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_021_img.jpg"},{"index":3,"id":"287","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_019_img.jpg"},{"index":4,"id":"286","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_018_img.jpg"},{"index":5,"id":"319","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_033_img.jpg"},{"index":6,"id":"317","texture":"https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_031_img.jpg"}]
     */

    private String target;
    private List<DrawingsBean> drawings;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<DrawingsBean> getDrawings() {
        return drawings;
    }

    public void setDrawings(List<DrawingsBean> drawings) {
        this.drawings = drawings;
    }

    public static class DrawingsBean {
        /**
         * index : 0
         * id : 296
         * texture : https://gz.bcebos.com/v1/beauty-mirror/MeiZhuangGetShaderTex/eyebrow/eyebrowdefult_Tex/panx_eyebrow2_028_img.jpg
         */

        private int index;
        private String id;
        private String texture;
        private int pd = 0;

        public int getPd() {
            return pd;
        }

        public void setPd(int pd) {
            this.pd = pd;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTexture() {
            return texture;
        }

        public void setTexture(String texture) {
            this.texture = texture;
        }
    }
}
