package com.panxsoft.xiaojingxiuxiu.utils;

import android.content.Context;

import com.panxsoft.xiaojingxiuxiu.app.MyApp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/1.
 */

public class Gmethod {
    public static String TAG_plus = "~~~";


    /**
     * 根据sp文件的信息，设置到变量中去
     * @param myApp
     * @param sptrackstr
     * @param trackindex
     */
    public static void setTrackInfo(MyApp myApp,String sptrackstr, int trackindex,String trackno){

        String[] trackstr11 = sptrackstr.split(",");
        if(sptrackstr.length()==0){
            myApp.getTrackMainGeneral()[trackindex].setTrackno(trackno);
        }else if(trackstr11.length == 5){
            myApp.getTrackMainGeneral()[trackindex].setTrackno(trackno);
            myApp.getTrackMainGeneral()[trackindex].setGoodscode(trackstr11[0]);
            myApp.getTrackMainGeneral()[trackindex].setGoodsname(trackstr11[1]);
            myApp.getTrackMainGeneral()[trackindex].setPrice(Integer.parseInt(trackstr11[2]));
            myApp.getTrackMainGeneral()[trackindex].setNummax(Integer.parseInt(trackstr11[3]));
            myApp.getTrackMainGeneral()[trackindex].setNumnow(Integer.parseInt(trackstr11[4]));
        } else if(trackstr11.length == 7){
            myApp.getTrackMainGeneral()[trackindex].setTrackno(trackno);
            myApp.getTrackMainGeneral()[trackindex].setGoodscode(trackstr11[0]);
            myApp.getTrackMainGeneral()[trackindex].setGoodsname(trackstr11[1]);
            myApp.getTrackMainGeneral()[trackindex].setPrice(Integer.parseInt(trackstr11[2]));
            myApp.getTrackMainGeneral()[trackindex].setNummax(Integer.parseInt(trackstr11[3]));
            myApp.getTrackMainGeneral()[trackindex].setNumnow(Integer.parseInt(trackstr11[4]));
            myApp.getTrackMainGeneral()[trackindex].setErrorcode(trackstr11[5]);
            myApp.getTrackMainGeneral()[trackindex].setErrortime(trackstr11[6]);
        }
    }

    /**
     * 必须有完整2位小数的元
     * @param fen
     * @return 元，必须有2位小数的
     */
    public static String tranFenToFloat2(int fen) {
        if(fen < 10) {
            return "0.0" + fen;
        } else if(fen < 100) {
            return "0." + fen;
        } else {
            String fenstr = "" + fen;
            return fenstr.substring(0, fenstr.length()-2) + "." + fenstr.substring(fenstr.length()-2,fenstr.length());
        }
    }

    /**
     * 必须有完整2位小数的元
     * @param fenlong
     * @return 元，必须有2位小数的,0时返回0
     */
    public static String tranFenToFloat2(long fenlong) {
        if(fenlong == 0l) return "0";

        if(fenlong < 10l) {
            return "0.0" + fenlong;
        } else if(fenlong < 100l) {
            return "0." + fenlong;
        } else {
            String fenstr = "" + fenlong;
            return fenstr.substring(0, fenstr.length()-2) + "." + fenstr.substring(fenstr.length()-2,fenstr.length());
        }
    }

    public static String tranYuanToSimple(String yuan){
        if(yuan.equals("0")) return yuan;
        if(yuan.substring(yuan.length()-3,yuan.length()).equals(".00")) return yuan.substring(0,yuan.length()-3);
        if(yuan.substring(yuan.length()-1,yuan.length()).equals("0")) return yuan.substring(0,yuan.length()-1);
        else return yuan;
    }

    public static String tranFenToSimple(int fen){
        String yuan = tranFenToFloat2(fen);
        if(yuan.equals("0")) return yuan;
        if(yuan.substring(yuan.length()-3,yuan.length()).equals(".00")) return yuan.substring(0,yuan.length()-3);
        if(yuan.substring(yuan.length()-1,yuan.length()).equals("0")) return yuan.substring(0,yuan.length()-1);
        else return yuan;
    }

    public static String tranFenToFloat1(int fee20){
        String fee20str = String.valueOf(fee20);
        if(fee20str.length() == 2){
            fee20str = "0" + fee20str;
        } else if(fee20str.length() == 1){
            fee20str = "00" + fee20str;
        }
        fee20str = fee20str.substring(0,fee20str.length()-2) + "." + fee20str.substring(fee20str.length()-2,fee20str.length());
        return fee20str.substring(0,fee20str.length()-1);
    }

    // 取得这个文件的名称（2_20170807094902.jpg）
    public static String getFileName(String url) {
        int i = url.indexOf("/");
        if (i == -1) {
            return "";
        }

        String extend = url.substring(i + 1);

        while (extend.contains("/")) {
            extend = extend.substring(extend.indexOf("/") + 1);
        }

        return extend;
    }

    public static void doHm(ArrayList<Map<String,String>> newList, String trackno, String code, String name, String batch, String endday, int change){
        boolean ishave = false;
        for(int i=0;i<newList.size();i++){
            Map<String,String> hm = newList.get(i);

            if(hm.get("trackgoodscode").equals(code) &&
                    hm.get("trackgoodsname").equals(name) &&
                    hm.get("batch").equals(batch) &&
                    hm.get("endday").equals(endday)){
                ishave = true;

                int changetotal = Integer.parseInt(hm.get("change").replace("+",""));
                changetotal = changetotal + change;

                if(changetotal > 0){
                    hm.put("change","+"+changetotal);
                } else if(changetotal < 0){
                    hm.put("change",""+changetotal);
                } else {
                    hm.put("change","0");
                }

                String detail = hm.get("detail");
                if(change > 0){
                    detail = detail + "_" + trackno+ "+" + change;
                } else {
                    detail = detail + "_" + trackno+ "" + change;
                }
                hm.put("detail",detail);

                break;
            }

        }

        if(!ishave){
            Map<String,String> hm = new HashMap<>();
            hm.put("trackgoodscode",code);
            hm.put("trackgoodsname", name);
            hm.put("batch",batch);
            hm.put("endday",endday);

            if(change > 0){
                hm.put("change","+"+change);
                hm.put("detail",trackno+"+"+change);
            } else{
                hm.put("change",""+change);
                hm.put("detail",trackno+""+change);
            }
            newList.add(hm);
        }
    }

    // 取得本VM程序的版本号 : 1.0.0
    public static String getAppVersion(Context context)
    {
        try
        {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;

            return versionName;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    // 取得本VM程序的包名 : com.freshtribes.seller
    public static String getPackageName(Context context)
    {
        try
        {
            String pkName = context.getPackageName();

            return pkName;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    /**
     * 两个时间之间相差距离多少天
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差分钟
     */
    public static long getDistanceMin(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(str1);
        System.out.println(str2);
        Date one;
        Date two;
        long min = 0l;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            min = diff / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return min;
    }

    /**
     * 判断一个字符串是否为合法的时间
     * @param str 时间字符串
     * @param timeformat 时间格式，也可以不设定（ yyyy-MM-dd HH:mm:ss）
     * @return 是否为正确的时间格式
     */
    public static boolean isValidDate(String str, String timeformat) {
        SimpleDateFormat format;
        if (timeformat.length() == 0) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            format = new SimpleDateFormat(timeformat);
        }
        boolean convertSuccess = true;

        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 取得固定长度的字符串
     * @param str 原字符串
     * @param len 长度
     * @return 扩展到指定长度的字符串，前面用0补足
     */
    public static String getLenStrBeforeZero(String str,int len) {
        if(str.length() < len) {
            String zero = "";
            for(int i=0;i<len-str.length();i++) {
                zero = zero + "0";
            }
            return zero + str;
        } else {
            return str.substring(0, len);
        }
    }

    public static int tranYuanToFen(String sum){
        // 计算交易费
        String newtotal_fee = "" + sum;
        if(newtotal_fee.indexOf(".") == -1){
            // 没找到的话
            newtotal_fee = newtotal_fee + ".00";
        } else if(newtotal_fee.indexOf(".") == newtotal_fee.length()-1) {
            newtotal_fee = newtotal_fee + "00";
        } else if(newtotal_fee.indexOf(".") == newtotal_fee.length()-2) {
            newtotal_fee = newtotal_fee + "0";
        } else if(newtotal_fee.indexOf(".") == newtotal_fee.length()-3) {
            newtotal_fee = newtotal_fee + "";
        }
        // 分
        int total = Integer.parseInt(newtotal_fee.replace(".", ""));
        return total;
    }

    // <summary>
    // 选出要的值 头与尾之间的内容 一般用于读取XML标签的内容
    // </summary>
    // <tou>返回值</wei>
    // 例如：<vmcode>HZ000123</vmcode>
    public static String xuan_zhi(String yuan, String tou, String wei)
    {
        String xuan_str = "";
        int jing_wei1 = yuan.indexOf(tou);
        int jing_wei2 = yuan.indexOf(wei);
        int jing_wei3 = tou.length();
        if (jing_wei1 > -1 && jing_wei2 > -1)
        {
            xuan_str = yuan.substring(jing_wei1 + jing_wei3, jing_wei2);
        }
        return xuan_str;
    }

    public static String get5(String goodscode){
        if(goodscode.length()==0) return "00000";
        else if(goodscode.length()==1) return "0000"+goodscode;
        else if(goodscode.length()==2) return "000"+goodscode;
        else if(goodscode.length()==3) return "00"+goodscode;
        else if(goodscode.length()==4) return "0"+goodscode;
        else if(goodscode.length()==5) return ""+goodscode;
        else return goodscode.substring(0,5);
    }

    public static void setNowStock(ArrayList<HashMap<String,Object>> al,String goodscode,int stock,int pricefen){
        for(HashMap<String,Object> hm:al){
            if(((String)hm.get("goodscode")).equals(goodscode)){
                // 说明找到一样的了
                int now = (Integer)hm.get("stock");
                hm.put("stock",now+stock);
                return;
            }
        }
        HashMap<String,Object> hm = new HashMap<String,Object>();
        hm.put("goodscode",goodscode);
        hm.put("stock",stock);
        hm.put("pricefen",pricefen);
        al.add(hm);
    }

    public static String getNowStockStr(ArrayList<HashMap<String,Object>> al){
        String rtn = "";
        for(HashMap<String,Object> hm:al){
            rtn += get5((String)hm.get("goodscode"))+get5(""+(Integer)hm.get("pricefen"))+(Integer)hm.get("stock") + "_";
        }
        if(rtn.length()>0) rtn = rtn.substring(0,rtn.length()-1);
        return rtn;
    }

}
