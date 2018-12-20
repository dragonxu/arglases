package android_serialport_api;

import android.os.SystemClock;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ComTrackMagex {
    private static final String TAG = "ComTrackMagex~~~";
    
    public static int activeThreadCount=0;
    // 时分秒
    private static final SimpleDateFormat formathms = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    // 接线的串口线位置，默认是这个
    private String portName = "/dev/ttyS4";

    // 串口对象
    private boolean isOpen = false;
    private SerialPort serialPort = null;

    // 输入与输出流
    private InputStream is = null;
    private OutputStream os = null;

    // 读取数据的线程
    private boolean readThreadisRunning = false;
    private ReadThread mReadThread;

    private byte[] rxByteArray = null;// 接收到的字节信息

    /**
     * 构造函数
     * @param portName 串口标记，如果是空，表示使用默认的串口
     */
    public ComTrackMagex(String portName){
        if(portName.length()>0) {
            this.portName = portName;
        }
    }

    /**
     * 打开串口
     */
    public void openSerialPort() {
        boolean ret = false;
        if (!isOpen) {
            try {
                // 9600赫兹，无校验的方式通讯
                serialPort = new SerialPort(new File(portName), 9600, 0,1,SerialPort.PARITY_CHECK.NONE.ordinal());
                os = serialPort.getOutputStream();
                is = serialPort.getInputStream();
                ret = true;
                isOpen = true;

                Log.i(TAG,portName + " 打开串口成功!");
            } catch (SecurityException e) {
                Log.e(TAG,portName + " 打开串口失败open fail" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG,portName + " 打开串口失败rw fail" + e.getMessage());
            }
        }

        // (2) 打开串口成功
        if (ret) {
            readThreadisRunning = true;
            mReadThread = new ReadThread();
            mReadThread.setName("读取机器的串口线程：" + formathms.format(new Date(System.currentTimeMillis())));
            mReadThread.start();
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {

        if (mReadThread != null) {
            readThreadisRunning = false;
            SystemClock.sleep(100);    //暂停0.1秒保证mReadThread线程结束
        }

        if (this.is != null) {
            try {
                this.is.close();
                this.is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.os != null) {
            try {
                this.os.close();
                this.os = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serialPort != null) {
            serialPort.close();
            Log.i(TAG,portName + " 关闭串口成功!");
        }

        isOpen = false;
    }

    private class ReadThread extends Thread {
        // 接收到的字节信息
        //byte[] rxByteArray = null;
        // 临时变量：接收到的字节信息
        byte[] rxByteArrayTemp = null;

        @Override
        public void run() {
            super.run();

            activeThreadCount++;
            while (readThreadisRunning) {
                byte ret[];
                byte data[] = new byte[1024];

                try {
                    if (is != null ) {
                        int bytes  = is.available();
                        if(bytes > 0){
                            bytes = is.read(data);
                            ret = new byte[bytes];
                            //Log.i(TAG,"ReadThread中发现串口有数据过来了,长度:" + bytes + ";内容=" + bytesToHexString(data,bytes));
                            System.arraycopy(data, 0, ret, 0, bytes);

                            // 发现有信息后就追加到临时变量
                            rxByteArrayTemp = ArrayAppend(rxByteArrayTemp, ret,bytes);

                        }else{
                            ret = new byte[0];
                            // 这次发现没有信息，如果以前有信息的，那就是我们要的数据
                            if (rxByteArrayTemp != null) {
                                rxByteArray = ArrayAppend(rxByteArrayTemp, null, 0);
                                rxByteArrayTemp = null;
                            }

                        }
                    } else {
                        ret = new byte[0];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //return;
                }

                // 每100个毫秒去读取数据
                SystemClock.sleep(100);

            }
            activeThreadCount--;
        }
    }


    private byte charToByteAscii(char ch)
    {
        byte byteAscii = (byte)ch;

        return byteAscii;
    }
//温度
    public String gettemp(){
        String orderstr = "H"+ "46" + "B2" + "*";
        byte[] fa_cmd = orderstr.getBytes();

        rxByteArray = null;			// 接收到的字节信息

        try {
            if (os != null) {
                os.write(fa_cmd, 0, fa_cmd.length);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long nowTime = System.currentTimeMillis();
        long endWhile = nowTime + 5000;  // 最多等待5000毫秒=5秒
        while(nowTime < endWhile){
            SystemClock.sleep(100);
            if(rxByteArray == null){
                // 需要再次发送,继续循环
                nowTime = System.currentTimeMillis();
                continue;  //while
            } else {
                break;
            }
        }

        if(rxByteArray == null){
            Log.e("ComTrackMagex","gettemp 没有收到返回值");
            return "";
        }

        // 哈哈，有数据了
        String backstr = "";
        for(int k=0;k<rxByteArray.length;k++){
            backstr += (char)rxByteArray[k];
        }

        String wendustr2 = backstr.substring(7,11);
        byte wendu1 =  (byte)(Integer.parseInt(wendustr2.substring(0,2),16));
        byte wendu2 =  (byte)(Integer.parseInt(wendustr2.substring(2,4),16));

        int wenduint = (int) (((wendu1 << 8) | wendu2 & 0xff));

        String wenduStr = ""+wenduint;
        if(wenduStr.length()==1) wenduStr = "0"+wenduStr;
        if(wenduStr.length()==2 && wenduStr.substring(0,1).equals("-")) wenduStr = "-0"+wenduStr.substring(1);
        wenduStr = wenduStr.substring(0,wenduStr.length()-1)+"."+wenduStr.substring(wenduStr.length()-1);

        return backstr+"("+wenduStr+")";
    }

    public String getStatus(){
        String orderstr = "H"+ "11" + "1" + "0" + "0" + "0" + "6B" + "*";
        byte[] fa_cmd = orderstr.getBytes();

        rxByteArray = null;			// 接收到的字节信息

        try {
            if (os != null) {
                os.write(fa_cmd, 0, fa_cmd.length);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long nowTime = System.currentTimeMillis();
        long endWhile = nowTime + 5000;  // 最多等待5000毫秒=5秒
        while(nowTime < endWhile){
            SystemClock.sleep(100);
            if(rxByteArray == null){
                // 需要再次发送,继续循环
                nowTime = System.currentTimeMillis();
                continue;  //while
            } else {
                break;
            }
        }

        if(rxByteArray == null){
            Log.e("ComTrackMagex","getStatus 没有收到返回值");
            return "";
        }

        // 哈哈，有数据了
        String backstr = "";
        for(int k=0;k<rxByteArray.length;k++){
            backstr += (char)rxByteArray[k];
        }
        Log.i(TAG,"接收到getstatus数据：" + backstr);

        String machinestatus1 = backstr.substring(4,6);
        byte bms1 = (byte)(Integer.parseInt(machinestatus1,16));
        String machinestatus2 = backstr.substring(6,8);
        byte bms2 = (byte)(Integer.parseInt(machinestatus2,16));
        String deliverystatus = backstr.substring(12,14);
        byte bds = (byte)(Integer.parseInt(deliverystatus,16));

        String mode = "User mode";
        if((byte)( bms2 & 0x01) == (byte)0x01){
            mode = "Tech mode";

            //Log.e("ComTrackMagex", "Tech mode 、查询发送次数："  + whilecount);
        }
        String door = "Door closed";
        if((byte)( bms1 & 0x04) == (byte)0x04){
            door = "Door open";

            //Log.e("ComTrackMagex", "Door open 、查询发送次数："  + whilecount);
        }
        String machine = "Machine ok";
        if((byte)( bms1 & 0x08) == (byte)0x08){
            machine = "Out of order";

            //Log.e("ComTrackMagex", "Out of order 、查询发送次数："  + whilecount);
        }
        Log.i("ComTrackMagex",mode + " , "+door + " , "+machine);

//        0	Machine busy
//        1	Vending automa running
//        2	Delivery ok
//        3	Delivery error
//        4	Last product delivered
//        5	Take the product message
//        6	Initialization running

        String ds = "";
        // 有结果，查看是否有错误
        if((byte)( bds & 0x01) == (byte)0x01){
            ds += "Machine busy,";
        }
        if((byte)( bds & 0x02) == (byte)0x02){
            ds += "Vending automa running,";
        }
        if((byte)( bds & 0x04) == (byte)0x04){
            ds += "Delivery ok,";
        }
        if((byte)( bds & 0x08) == (byte)0x08){
            ds += "Delivery error,";
        }
        if((byte)( bds & 0x10) == (byte)0x10){
            ds += "Last product delivered,";
        }
        if((byte)( bds & 0x20) == (byte)0x20){
            ds += "Take the product message,";
        }
        if((byte)( bds & 0x40) == (byte)0x40){
            ds += "Initialization running,";
        }

        return backstr+"("+backstr.substring(4,8)+")("+backstr.substring(12,14)+")("+mode + " , "+door + " , "+machine+")("+(ds.length()==0?"":ds.substring(0,ds.length()-1))+")";

    }
//复位
    public String motorrest(int hang,int lie)
    {
        String liestr = ""+lie;
        if(lie==10) liestr = "A";
        else if(lie==11) liestr = "B";
        else if(lie==12) liestr = "C";
        else if(lie==13) liestr = "D";
        else if(lie==14) liestr = "E";
        else if(lie==15) liestr = "F";

        String rtn = "发送命令后无应答";

        String str = "H"+ "44" + "1" + hang + liestr ;

        int ck = 0;
        for(int i=0;i<str.length();i++){
            ck += (int)charToByteAscii(str.charAt(i));
        }
        ck = ck%256;
        String ckstr = String.format("%02x", ck).toUpperCase();
        String orderstr = str + ckstr + "*";
        byte[] fa_cmd = orderstr.getBytes();

        Log.i("ComTrackMagex",orderstr);

        rxByteArray = null;			// 接收到的字节信息

        try {
            if (os != null) {
                os.write(fa_cmd, 0, fa_cmd.length);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long nowTime = System.currentTimeMillis();
        long endWhile = nowTime + 5000;  // 最多等待5000毫秒=5秒
        while(nowTime < endWhile){
            SystemClock.sleep(100);
            if(rxByteArray == null){
                // 需要再次发送,继续循环
                nowTime = System.currentTimeMillis();
                continue;  //while
            } else {
                break;
            }
        }

        if(rxByteArray == null){
            Log.e("ComTrackMagex","motorrest 没有收到返回值");
            return rtn;
        }

        // 哈哈，有数据了
        String backstr = "";
        for(int k=0;k<rxByteArray.length;k++){
            backstr += (char)rxByteArray[k];
        }
        Log.i(TAG,"接收到motorrest数据：" + backstr);

        String istarting = backstr.substring(3,4);
        return backstr+"("+istarting+")";

    }


    /**
     * 售货函数
     *
     * @param hang  货道行坐标（1-9）
     * @param lie   货到列坐标(0-9)
     * @return 返回货道结构类型
     */
    public String[] vend_out_ind(int hang,int lie,int fen,String goodscode,String payway,String payinfo)
    {
        String liestr = ""+lie;
        if(lie==10) liestr = "A";
        else if(lie==11) liestr = "B";
        else if(lie==12) liestr = "C";
        else if(lie==13) liestr = "D";
        else if(lie==14) liestr = "E";
        else if(lie==15) liestr = "F";

        String rtn = "出货命令发送3次对方无应答";

        long nowTime = 0l;
        long endWhile = 0l;

        String str = "H"+ "12" + "1" + hang + liestr + String.format("%02x", fen/256).toUpperCase() + String.format("%02x", fen%256).toUpperCase() + "1" + "0000";
        int ck = 0;
        for(int i=0;i<str.length();i++){
            ck += (int)charToByteAscii(str.charAt(i));
        }
        ck = ck%256;
        String ckstr = String.format("%02x", ck).toUpperCase();
        String orderstr = str + ckstr + "*";
        byte[] fa_cmd = orderstr.getBytes();

        boolean isSendOk = false;
        for(int i=1;i<=3;i++) {
            Log.i(TAG,"发送出货指令数据：" + orderstr +"、命令发送次数："  + i);

            rxByteArray = null;			// 接收到的字节信息

            try {
                if (os != null) {
                    os.write(fa_cmd, 0, fa_cmd.length);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            nowTime = System.currentTimeMillis();
            endWhile = nowTime + 5000;  // 最多等待5000毫秒=5秒
            while(nowTime < endWhile){
                SystemClock.sleep(100);
                if(rxByteArray == null){
                    // 需要再次发送,继续循环
                    nowTime = System.currentTimeMillis();
                    continue;  //while
                } else {
                    break;
                }
            }

            if(rxByteArray == null){
                continue;  //for
            }

            // 哈哈，有数据了
            String backstr = "";
            for(int k=0;k<rxByteArray.length;k++){
                backstr += (char)rxByteArray[k];
            }
            Log.i(TAG,"接收到出货指令数据：" + backstr+"、命令发送次数："  + i);



            // 返回信息
            if(rxByteArray.length == 7){
                String result = backstr.substring(3,4);
                if(result.equals("0")){
                    rtn = "配置错误";

                    Log.e("ComTrackMagex", "配置错误、命令发送次数："  + i);
                } else if(result.equals("2")){
                    rtn = "设备不可用";
                    Log.e("ComTrackMagex", "设备不可用、命令发送次数："  + i+";暂停：3秒后继续发送");
                    SystemClock.sleep(3000);   //等待3秒


                } else {
                    // 1 表示开始出货啦
                    rtn = "";
                    isSendOk = true;
                    break;
                }
            } else {
                rtn = "收到应答的字节长度不对"+rxByteArray.length;

                Log.e("ComTrackMagex", "收到应答的字节长度不对、命令发送次数："  + i);
                //break;
            }

        }
        if(!isSendOk) {
            // 发送开始出货命令失败了
            return new String[]{rtn, "" + hang + lie, "" + fen, goodscode, payway, payinfo};
        }

        rtn = "发送出货指令成功，查询出货结果中";
        // 开始查询结果
        orderstr = "H"+ "11" + "1" + "0" + "0" + "0" + "6B" + "*";
        fa_cmd = orderstr.getBytes();


        // 到这一步说明，发送出货指令成功了，机器正在出货。接下是 查询指令执行结果
        long nowMills = System.currentTimeMillis(); // 现在的毫秒数
        long whileEndMills = nowMills + 120*1000;  // 最长120秒的时间，等待出货完成！

        int whilecount = 0;
        while (nowMills < whileEndMills) {
            whilecount++;

            Log.i(TAG,"发送查询指令数据：" + orderstr +"、命令发送次数："  + whilecount);

            rxByteArray = null;			// 接收到的字节信息
            try {
                if (os != null) {
                    os.write(fa_cmd, 0, fa_cmd.length);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            nowTime = System.currentTimeMillis();
            endWhile = nowTime + 2000;  // 最多等待2秒
            while(nowTime < endWhile){
                SystemClock.sleep(100);
                if(rxByteArray == null){
                    // 需要再次发送,继续循环
                    nowTime = System.currentTimeMillis();
                    continue;  //while
                } else {
                    break;
                }
            }

            // 刷新时间
            nowMills = System.currentTimeMillis();
            if(rxByteArray == null){
                // 需要再次发送,继续循环
                continue;
            }


            // 哈哈，有数据了
            String backstr = "";
            for(int k=0;k<rxByteArray.length;k++){
                backstr += (char)rxByteArray[k];
            }

            Log.i(TAG,"接收到查询指令数据：" + backstr+"("+backstr.substring(4,8)+")("+backstr.substring(12,14)+"),查询发送次数："  + whilecount);



            String machinestatus1 = backstr.substring(4,6);
            byte bms1 = (byte)(Integer.parseInt(machinestatus1,16));
            String machinestatus2 = backstr.substring(6,8);
            byte bms2 = (byte)(Integer.parseInt(machinestatus2,16));
            String deliverystatus = backstr.substring(12,14);
            byte bds = (byte)(Integer.parseInt(deliverystatus,16));

            String mode = "User mode";
            if((byte)( bms2 & 0x01) == (byte)0x01){
                mode = "Tech mode";

                //Log.e("ComTrackMagex", "Tech mode 、查询发送次数："  + whilecount);
            }
            String door = "Door closed";
            if((byte)( bms1 & 0x04) == (byte)0x04){
                door = "Door open";

                //Log.e("ComTrackMagex", "Door open 、查询发送次数："  + whilecount);
            }
            String machine = "Machine ok";
            if((byte)( bms1 & 0x08) == (byte)0x08){
                machine = "Out of order";

                //Log.e("ComTrackMagex", "Out of order 、查询发送次数："  + whilecount);
            }
            Log.i("ComTrackMagex",mode + " , "+door + " , "+machine);


            boolean isBusy = false;
            if((byte)( bds & 0x01) == (byte)0x01 || (byte)( bds & 0x02) == (byte)0x02){
                isBusy = true;

                Log.e("ComTrackMagex", "isBusy = true 、查询发送次数："  + whilecount);
            }
            if(isBusy){
                // 说明正在处理中，需要再次发送
                SystemClock.sleep(2000);   //等待2秒
                // 继续循环
                nowMills = System.currentTimeMillis();
                continue;
            }

            //0	Machine busy
            //1	Vending automa running
            //2	Delivery ok
            //3	Delivery error
            //4	Last product delivered
            //5	Take the product message
            //6	Initialization running


            // 有结果，查看是否有错误
            if((byte)( bds & 0x04) == (byte)0x04) {
                rtn = "";
                // 出货完成了，那么退出
                break;
            } else if((byte)( bds & 0x08) == (byte)0x08){
                rtn = "Delivery error";
                // 出货完成了，那么退出
                break;
            } else {
                rtn = "Delivery error other";

                Log.e("ComTrackMagex", "Delivery error other 、查询发送次数："  + whilecount);
            }

            SystemClock.sleep(2000);   //等待2秒
        }

        return new String[]{rtn,""+hang+lie,""+fen,goodscode,payway,payinfo};





    }

    /**
     * 将源数组追加到目标数组
     *
     * @param byte_1 原来的
     * @param byte_2 要追加的
     * @param size 要追加的长度
     * @return 返回一个新的数组，包括了原数组1和原数组2
     */
    private byte[] ArrayAppend(byte[] byte_1, byte[] byte_2,int size)
    {
        // java 合并两个byte数组

        if (byte_1 == null && byte_2 == null)
        {
            return null;
        } else if (byte_1 == null)
        {
            byte[] byte_3=new byte[ size];
            System.arraycopy(byte_2, 0, byte_3, 0, size);
            return byte_3;
            //return byte_2;
        } else if (byte_2 == null)
        {
            byte[] byte_3=new byte[byte_1.length ];
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
            return byte_3;
            //return byte_1;
        } else
        {
            byte[] byte_3 = new byte[byte_1.length + size];
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
            System.arraycopy(byte_2, 0, byte_3, byte_1.length, size);
            return byte_3;
        }

    }

    /**
     * 转换字节为十六进制
     * @param src 原来
     * @param size 长度
     * @return 结果
     */
    private String bytesToHexString(byte[] src, int size) {
        String ret = "";
        if (src == null || size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            if(ret.length()>0) {
                ret =  ret + "" + hex;
            } else {
                ret = hex;
            }
        }
        return ret.toUpperCase(Locale.US);
    }

    /**
     * 转换一个字节为十六进制
     * @param src 原来一个字节
     * @return 结果
     */
    private String byteToHexString(byte src) {

        String hex = Integer.toHexString(src & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }

        return hex.toUpperCase(Locale.US);
    }

    private int tranHex2ToInt(String hex){
        if(hex.length()!=2)
            return 0;

        return Integer.parseInt(hex,16);
    }

    private int asciitonum(String ascii){
        switch(ascii) {
            case "30":
                return 0;
            case "31":
                return 1;
            case "32":
                return 2;
            case "33":
                return 3;
            case "34":
                return 4;
            case "35":
                return 5;
            case "36":
                return 6;
            case "37":
                return 7;
            case "38":
                return 8;
            case "39":
                return 9;
            default:
                return 0;
        }
    }


}
