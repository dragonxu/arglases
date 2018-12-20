package com.visagetechnologies.visagetrackerunitydemo;

/**
 * Created by Huyuzengjian on 2018/1/26.
 */

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.panxsoft.xiaojingxiuxiu.utils.ToastUtil;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends UnityPlayerActivity {
    public final String TAG = CameraActivity.this.getClass().getSimpleName();
    Camera cam;
    int ImageWidth = -1;
    int ImageHeight = -1;
    SurfaceTexture tex;
    public float cameraFps;
    int pickCam = -1;
    int camId = -1;
    int orientation;
    boolean openCam = false;

    public CameraActivity() {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CameraInfo camInfo = new CameraInfo();

        try {
            Camera.getCameraInfo(this.camId, camInfo);
        } catch (Exception var4) {
        }

        Display display = ((WindowManager) this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        this.orientation = camInfo.orientation;
        if (camInfo.facing == 1) {
            setParameters((display.getRotation() * 90 + this.orientation) % 360, -1, -1, -1);
        } else if (camInfo.facing == 0) {
            setParameters((this.orientation - display.getRotation() * 90 + 360) % 360, -1, -1, -1);
        }

    }

    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
    }

    //TODO initMakeupView
    protected void initUnityView() {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        LinearLayout unityContainer = new LinearLayout(this);
        unityContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        unityContainer.addView(mUnityPlayer.getView());
        rootView.addView(unityContainer, 0);
    }

    //TODO initMakeupView
    protected void initUnityView(ViewGroup unityContainer) {
        unityContainer.addView(mUnityPlayer.getView());
    }

    @Override
    public void onPause() {
        this.closeCamera();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (this.openCam) {
            this.GrabFromCamera(this.ImageWidth, this.ImageHeight, this.pickCam);
        }
        super.onResume();
    }

    private void setPreviewSize(Parameters parameters, int width, int height) {
        int idx = 0;
        double dist1 = 100000.0D;
        double dist2 = 100000.0D;
        List sizes = parameters.getSupportedPreviewSizes();

        for (int i = 0; i < sizes.size(); ++i) {
            Log.i(TAG, "w:" + ((Size) sizes.get(i)).width + "|h:" + ((Size) sizes.get(i)).height);
            dist2 = Math.abs(Math.sqrt(Math.pow((double) (((Size) sizes.get(i)).width - width), 2.0D) + Math.pow((double) (((Size) sizes.get(i)).height - height), 2.0D)));
            if (dist2 < dist1) {
                idx = i;
                dist1 = dist2;
            }
        }

        parameters.setPreviewSize(((Size) sizes.get(idx)).width, ((Size) sizes.get(idx)).height);
    }

    public void GrabFromCamera(int imWidth, int imHeight, int pickCamera) {
        if (imWidth == -1 || imHeight == -1) {
            imWidth = 320;
            imHeight = 240;
        }

//        String resolutionStr = SystemUtil.getPreferenceString(getApplicationContext(), "resolutionConfig", "1280x960");

//        imWidth = Integer.valueOf(resolutionStr.split("x")[0]);
//        imHeight = Integer.valueOf(resolutionStr.split("x")[1]);

        this.pickCam = pickCamera;
        this.camId = this.getCameraId(this.pickCam);

        try {
            this.cam = Camera.open(this.camId);
        } catch (Exception var12) {
            Log.e(TAG, "Unable to open camera");
            Toast.makeText(this.getBaseContext(), "Unable to open camera", Toast.LENGTH_SHORT).show();
            return;
        }

        this.ImageWidth = imWidth;
        this.ImageHeight = imHeight;
        Parameters parameters = this.cam.getParameters();
        this.setPreviewSize(parameters, this.ImageWidth, this.ImageHeight);
        parameters.setPreviewFormat(ImageFormat.NV21);

        if (parameters.getMaxNumMeteringAreas() > 0) {
            Log.i(TAG, "ENABLE METERING");
            List<Camera.Area> mMeteringList = new ArrayList<>();
            mMeteringList.add(new Camera.Area(new Rect(-500, -500, 500, 500), 1000));
            parameters.setMeteringAreas(mMeteringList);
        }

        this.cam.setParameters(parameters);

        Display display = ((WindowManager) this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int screenOrientation = display.getRotation();
        Size previewSize = this.cam.getParameters().getPreviewSize();
        Log.i(TAG, "Current preview size is " + previewSize.width + ", " + previewSize.height);
        int dataBufferSize = (int) ((double) (previewSize.height * previewSize.width) * ((double) ImageFormat.getBitsPerPixel(this.cam.getParameters().getPreviewFormat()) / 8.0D));

        for (int cameraInfo = 0; cameraInfo < 10; ++cameraInfo) {
            this.cam.addCallbackBuffer(new byte[dataBufferSize]);
        }

        this.tex = new SurfaceTexture(0);

        try {
            this.cam.setPreviewTexture(this.tex);
        } catch (IOException var11) {
            var11.printStackTrace();
        }

        CameraInfo var13 = new CameraInfo();
        Camera.getCameraInfo(this.camId, var13);
        this.orientation = var13.orientation;
        byte flip = 0;
        if (var13.facing == 1) {
            flip = 1;
        }

//        if (SystemUtil.getPreferenceBoolean(getApplicationContext(), "isCameraMirrored")) {
//            if (flip == 1) {
//                flip = 0;
//            } else {
//                flip = 1;
//            }
//        }

        this.ImageWidth = previewSize.width;
        this.ImageHeight = previewSize.height;
        if (var13.facing == 1) {
            setParameters((screenOrientation * 90 + this.orientation + 360) % 360, this.ImageWidth, this.ImageHeight, flip);
        } else {
            setParameters((this.orientation - screenOrientation * 90 + 360) % 360, this.ImageWidth, this.ImageHeight, flip);
        }

        this.cam.setPreviewCallbackWithBuffer(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                CameraActivity.WriteFrame(data);
                camera.addCallbackBuffer(data);
            }
        });
        this.cam.startPreview();
        this.openCam = true;
    }

    protected void setWhiteBalance(String whiteBalance) {
        Parameters parameters = this.cam.getParameters();
        parameters.setWhiteBalance(whiteBalance);
        this.cam.setParameters(parameters);
    }

    protected List<String> getWhiteBalanceList() {
        return this.cam.getParameters().getSupportedWhiteBalance();
    }

    protected String getCurrentWhiteBalance() {
        return this.cam.getParameters().getWhiteBalance();
    }

    protected void setSceneMode(String sceneMode) {
        Parameters parameters = this.cam.getParameters();
        parameters.setSceneMode(sceneMode);
        this.cam.setParameters(parameters);
    }

    protected List<String> getSceneModeList() {
        return this.cam.getParameters().getSupportedSceneModes();
    }

    protected String getCurrentSceneMode() {
        return this.cam.getParameters().getSceneMode();
    }

    protected void setExposure(String exposure) {
        Parameters parameters = this.cam.getParameters();
        parameters.setExposureCompensation(Integer.valueOf(exposure));
        this.cam.setParameters(parameters);
    }

    protected void setMeteringArea(int l, int t, int r, int b) {
        Parameters parameters = this.cam.getParameters();
        if (parameters.getMaxNumMeteringAreas() > 0) {
            Log.i(TAG, "ENABLE METERING");
            List<Camera.Area> mMeteringList = new ArrayList<>();
            mMeteringList.add(new Camera.Area(new Rect(l, t, r, b), 1000));
            parameters.setMeteringAreas(mMeteringList);
        }

        this.cam.setParameters(parameters);
    }

    protected void handleMetering(MotionEvent event) {
        Parameters params = this.cam.getParameters();
        Size previewSize = params.getPreviewSize();

        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, previewSize);

        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 800));
            params.setMeteringAreas(meteringAreas);
        } else {
            Log.i(TAG, "metering areas not supported");
        }
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / previewSize.width - 1000);
        int centerY = (int) (y / previewSize.height - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    protected int getMaxExposure() {
        return this.cam.getParameters().getMaxExposureCompensation();
    }

    protected int getMinExposure() {
        return this.cam.getParameters().getMinExposureCompensation();
    }

    protected String getCurrentExposure() {
        return "" + this.cam.getParameters().getExposureCompensation();
    }

    public void closeCamera() {
        if (this.cam != null) {
            this.cam.stopPreview();
            this.cam.release();
            this.cam = null;
        }
    }

    int getCameraId(int pickCamera) {
        boolean cameraId = true;
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == 1 && pickCamera != 1) {
                return i;
            }

            if (info.facing == 0 && pickCamera == 1) {
                return i;
            }
        }

        if (numberOfCameras > 0) {
            return 0;
        }

        return -1;
    }

    protected int getCameraCount() {
        return Camera.getNumberOfCameras();
    }

    public void AlertDialogFunction(final String message) {
        this.runOnUiThread(new Runnable() {
            private String licenseMessage = message;

            @Override
            public void run() {
                TextView title = new TextView(UnityPlayer.currentActivity);
                title.setText("License warning");
                title.setGravity(17);
                title.setTextSize(2, 30.0F);
                TextView msg = new TextView(UnityPlayer.currentActivity);
                msg.setText(message);
                msg.setGravity(17);
                msg.setTextSize(2, 20.0F);
            }
        });
    }

    @Override
    public void onDestroy() {
        this.closeCamera();
        super.onDestroy();
    }

    public static native void WriteFrame(byte[] var0);

    public static native void setParameters(int var0, int var1, int var2, int var3);

    public static native int getImageWidth();

    static {
        System.loadLibrary("VisageVision");
        System.loadLibrary("VisageTrackerUnityPlugin");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    /**
     * 获取Tracker配置
     *
     * @return
     */
    private int ConfigForTracker() {
        return 0;
    }


    /**
     * 设置渲染分辨率
     *
     * @return
     */
    protected void setRenderResolution(int w, int h) {
        //TODO
        UnityPlayer.UnitySendMessage("Scripts", "ScreenResolution", w + "x" + h);
    }

    /**
     * 加载单妆
     *
     * @param model
     */
    protected void loadMakeUp(SingleMakeup model) {
        //TODO 重做 传入实体类 JSON
        UnityPlayer.UnitySendMessage("Scripts", "LoadMakeUp", model.toJson().toString());
    }

    /**
     * 加载眼镜
     *
     * @param
     */
    protected void loadGlasses(String param) {
        UnityPlayer.UnitySendMessage("Scripts", "TryGlasses", param);
    }

    /**
     * 加载单妆
     *
     * @param operation
     */
    protected void loadMakeUp(String operation) {
        //TODO 内部使用 传入操作String即可
        UnityPlayer.UnitySendMessage("Scripts", "LoadMakeUp", operation);
    }

    /**
     * 加载整妆
     */
    protected void loadOverallMakeUp(ArrayList<SingleMakeup> list) {
        //TODO 火坤说要换JSON传过去
        //                operationStr = operationStr + assets.getJSONObject(j).getString("model_xml") +
//                        "+" + assets.getJSONObject(j).getJSONObject("pivot").getString("index") +
//                        "@" + assets.getJSONObject(j).optInt("version");
//        StringBuilder sb = new StringBuilder();
        JSONObject makeup_data = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < list.size(); i++) {
//                if (i > 0) {
//                    sb.append("|");
//                }
//                sb.append(singleMakeup.getModelUrl());
//                sb.append("+");
//                sb.append(singleMakeup.getDrawingIndex());
//                sb.append("@");
//                sb.append(singleMakeup.getModelVersion());
                jsonArray.put(list.get(i).toJson());
            }
            makeup_data.put("makeup_data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "loadOverallMakeUp: " + makeup_data.toString());
//        Log.i(TAG, "loadOverallMakeUp: " + sb.toString());
        UnityPlayer.UnitySendMessage("Scripts", "OverallMakeUp", makeup_data.toString());
    }

    /**
     * 加载整妆
     *
     * @param operation
     */
    protected void loadOverallMakeUp(String operation) {
        //TODO 内部使用 传入操作String即可
        UnityPlayer.UnitySendMessage("Scripts", "OverallMakeUp", operation);
    }

    /**
     * unity屏幕截图
     */
    protected void captureScreenshot() {
        UnityPlayer.UnitySendMessage("Scripts", "CaptureScreenshot", "");
    }

    /**
     * unity清除缓存
     */
    protected void clearCache() {
        //TODO
        UnityPlayer.UnitySendMessage("Scripts", "ClearCache", "");
    }

    /**
     * @param isFrontCam
     */
    protected void switchCamera(boolean isFrontCam) {
        //TODO 传bool值选择前后，避免字符串
        UnityPlayer.UnitySendMessage("Scripts", "SwitchCamera", isFrontCam ? "FrontFacing" : "BackFacing");
    }

    /**
     * 清除妆容
     *
     * @param makeupType 妆容类型{@link Const#TYPE_LIPSTICK}
     */
    protected void clearMakeup(String makeupType) {
        //TODO 提示传入Const下的类型
        UnityPlayer.UnitySendMessage("Scripts", "Makeup_Revoke", makeupType);
    }

    /**
     * 清除所有妆容
     */
    protected void clearAllMakeup() {
        clearMakeup("All");
    }

    /**
     * 对比试戴妆容
     *
     * @param bool
     */
    protected void hideMakeup(boolean bool) {
        //TODO 更名以便理解
        UnityPlayer.UnitySendMessage("Scripts", bool ? "Makeup_Hide" : "Makeup_Show", "All");
    }

    /**
     * 设置试装参数
     *
     * @param attr  {@link Const#ATTR_EYELASH_TOP_BOTTOM}
     * @param value
     */
    protected void setMakeupConfig(String attr, float value) {
        //TODO
        UnityPlayer.UnitySendMessage("Scripts", "MakeupSetConfigure", attr + "=" + value);
    }

    /**
     * 重置试装参数
     *
     * @param
     */
    protected void resetMakeupConfig() {
        //TODO
        UnityPlayer.UnitySendMessage("Scripts", "MakeupSetConfigure", "reset");
    }


    /**
     * 调整妆容浓淡度
     *
     * @param makeupType {@link Const#TYPE_LIPSTICK}
     * @param level      {0-1f}
     */
    protected void adjustMakeupAlpha(String makeupType, float level) {
        //TODO 提示传入Const下的类型
        UnityPlayer.UnitySendMessage("Scripts", "Makeup_AlphaLevel", makeupType + "|" + level);
    }

    /**
     * 停止追踪
     */
    protected void stopTracker() {
        UnityPlayer.UnitySendMessage("Scripts", "StopTracker", "");
    }

    /**
     * 开始追踪
     */
    protected void startTracker() {
        //TODO 增加
        UnityPlayer.UnitySendMessage("Scripts", "startTracker", "");
    }

    /**
     * 切换妆容画法
     *
     * @param makeupType
     * @param drawingIndex
     */
    protected void changeMakeupDrawing(String makeupType, int drawingIndex) {
        //TODO 提示传入Const下的类型
        UnityPlayer.UnitySendMessage("Scripts", "Makeup_change_drawing", makeupType + "|" + drawingIndex);
    }

    /**
     * Unity妆容试戴参数返回
     *
     * @param str
     */
    private void Makeup_Drawings(String str) {
        try {
            ArrayList<WearWay> wearWayList = new ArrayList<>();
            JSONObject json = new JSONObject(str);
            String target = json.getString("target");
            JSONArray drawings = json.getJSONArray("drawings");
            for (int i = 0; i < drawings.length(); i++) {
                WearWay wearWay = new WearWay(target, drawings.getJSONObject(i).getInt("index"));
                if (drawings.getJSONObject(i).has("texture")) {
                    if (!drawings.getJSONObject(i).getString("texture").equals("")) {
                        wearWay.setPic(drawings.getJSONObject(i).getString("texture"));
                    }
                }
                wearWayList.add(wearWay);
            }
            onReceiveWearList(wearWayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unity 截屏回调
     *
     * @param path
     */
    private void CaptureScreenshot_callback(String path) {
        onCaptureFinish(path);
    }

    /**
     * Unity初始化完成
     */
    public void OnTrackerStart() {
        onMakeupSDKInitialized();
    }

    /**
     * Unity
     *
     * @param msg
     */
    private void Load_Message(String msg) {
        onLoadMessage(msg);
    }

    /**
     * Unity
     *
     * @param msg
     */
    private void Log_Message(String msg) {
    }

    /**
     * NULL
     *
     * @param islosed
     */
    private void OnTrackerLosed(final boolean islosed) {

    }

    /**
     * NULL
     *
     * @param isdo
     */
    private void OnlineADs(boolean isdo) {

    }

    /**
     * Unity妆容浓淡程度返回
     *
     * @param target
     * @param value
     */
    private void Makeup_Alpha(String target, float value) {
        onMakeupAlpha(target, value);
    }

    /**
     * Unity返回追踪状态
     *
     * @param state
     */
    private void trackerStatus(int state) {
        onTrackerState(state);
    }

//-------------------------------------------------------以下为需要重写的方法-----------------------------------------------------------------------------------------

    /**
     * 获取当前单妆的画法列表
     *
     * @param wearList
     */
    protected void onReceiveWearList(ArrayList<WearWay> wearList) {
    }

    /**
     * Unity 截屏回调
     *
     * @param picPath
     */
    protected void onCaptureFinish(String picPath) {
    }

    /**
     * Unity初始化完成
     */
    protected void onMakeupSDKInitialized() {
    }

    /**
     * 妆容加载状况
     */
    protected void onLoadMessage(String msg) {
        //TODO
    }

    /**
     * Unity妆容浓淡程度返回
     */
    protected void onMakeupAlpha(String target, float value) {
        //TODO
    }

    /**
     * Unity返回追踪状态
     */
    protected void onTrackerState(int state) {
        //TODO
    }
}
