package com.visagetechnologies.visagetrackerunitydemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author:LPK
 * @time:2018/7/3 14:33
 * @Package:com.visagetechnologies.visagetrackerunitydemo
 * @Description: 单妆的实体类，放在集合里可以组成整体妆容
 */
public class SingleMakeup {
    private String modelUrl;
    private int modelVersion;
    private int drawingIndex;
    private ArrayList<String> drawingUrls = new ArrayList<>();

    public SingleMakeup() {
    }

    public SingleMakeup(String modelUrl, int modelVersion, int drawingIndex) {
        this.modelUrl = modelUrl;
        this.modelVersion = modelVersion;
        this.drawingIndex = drawingIndex;
    }

    public SingleMakeup(String modelUrl, int modelVersion, int drawingIndex, ArrayList<String> drawingUrls) {
        this.modelUrl = modelUrl;
        this.modelVersion = modelVersion;
        this.drawingIndex = drawingIndex;
        this.drawingUrls = drawingUrls;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public int getDrawingIndex() {
        return drawingIndex;
    }

    public void setDrawingIndex(int drawingIndex) {
        this.drawingIndex = drawingIndex;
    }

    public int getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(int modelVersion) {
        this.modelVersion = modelVersion;
    }

    public ArrayList<String> getDrawingUrls() {
        return drawingUrls;
    }

    public void setDrawingUrls(ArrayList<String> drawingUrls) {
        this.drawingUrls = drawingUrls;
    }

    public void addDrawingUrls(String drawingUrl) {
        this.drawingUrls.add(drawingUrl);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("xml_url", this.modelUrl);
            jsonObject.put("draw_index", this.drawingIndex);
            jsonObject.put("version", this.modelVersion);
            JSONArray drawingUrls = new JSONArray();
            for (int j = 0; j < this.drawingUrls.size(); j++) {
                drawingUrls.put(this.drawingUrls.get(j));
            }
            jsonObject.put("resource_images_url", drawingUrls);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
