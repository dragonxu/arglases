package com.panxsoft.xiaojingxiuxiu.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class MyHttp {

	private static final String TAG = "MyHttp";

	private String vmserviceurl = "";   
	private String vmservicepara = "";   
	private String soapreturn = "";      

	
	public String post(String pathUrl, String requestString)
	{
		vmserviceurl = pathUrl;
		vmservicepara = requestString;
		soapreturn="";       // 返回是空值，应该是联网失败的

		try
		{
			// 启动线程
			Thread http_soapThread = new http_soap();
			http_soapThread.setPriority(Thread.MAX_PRIORITY);// 最大优先级
			http_soapThread.start();// 启动线程
			http_soapThread.join();// 主线程等待这个线程运行完成再往下执行
			
		} catch (InterruptedException e)   //InterruptedException
		{
			soapreturn="ex";   // ex表示服务器后台发生异常！
			
			e.printStackTrace();
		}
		
		return soapreturn;
	}

	/**
	 * 
	 * 因为，在安卓主线程里，不能使用网络，只能新开个线程 第一种方法
	 */

	private class http_soap extends Thread
	{
		public void run()
		{
			try
			{
				//LogUtils.i("请求的网址：" + vmserviceurl);
				//LogUtils.i("发送出去的数据为：" + vmservicepara);

				String rtn = httpUrlConnection(vmserviceurl, vmservicepara);
				if(rtn.length() == 0){
					//联网失败，再试一次
					//Log.i(TAG, "联网失败，再试一次");
					rtn = httpUrlConnection(vmserviceurl, vmservicepara);
				}

				//LogUtils.i("收到回复的数据为：" + (rtn.length()==0?"(空)":rtn));

				soapreturn = rtn;

			} catch (Exception e){

				e.printStackTrace();
			}
		}
	}



	/**
	 * 发起POST请求，发送XML数据，并接收回复的数据(底层函数)
	 * 
	 * @param pathUrl
	 *            要发送的网址
	 * @param requestString
	 *            要发送的内容
	 * @return 收到的内容
	 */
	private String httpUrlConnection(String pathUrl, String requestString)
	{

		String hu_str = "";
		try
		{
			// 建立连接
			URL url = new URL(pathUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			// //设置连接属性
			httpConn.setDoOutput(true);// 使用 URL 连接进行输出
			httpConn.setDoInput(true);// 使用 URL 连接进行输入
			httpConn.setUseCaches(false);// 忽略缓存
			httpConn.setConnectTimeout(12000);// 连接超时 12秒
			httpConn.setReadTimeout(20000);// 读取超时 单位毫秒       20秒
			httpConn.setRequestMethod("POST");// 设置URL请求方法
			httpConn.setUseCaches(false);// Post 请求不能使用缓存

			// 设置请求属性
			// 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
			byte[] requestStringBytes = requestString.getBytes("UTF-8");
			httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);
			httpConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			httpConn.setRequestProperty("Connection", "close");// 不维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");

			// 建立输出流，并写入数据
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.close();
			// 获得响应状态
			int responseCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode)
			{// 连接成功
				// 当正确响应时处理数据
				StringBuilder sb = new StringBuilder();
				String readLine;
				BufferedReader responseReader;
				// 处理响应流，必须与服务器响应流输出的编码一致
				responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
				while ((readLine = responseReader.readLine()) != null)
				{
					sb.append(readLine);
				}
				responseReader.close();
				hu_str = sb.toString();
			}
			httpConn.disconnect();
		} catch (ConnectException ex){
			Log.e(TAG,"网络连接不可用!");
		} catch (SocketTimeoutException ex){
            Log.e(TAG,"取得返回数据超时!");
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return hu_str;
	}


}
