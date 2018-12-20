package com.panxsoft.xiaojingxiuxiu.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * 用字符串生成二维码
 * 
 * @author Administrator
 */
public class Create2DCodeUtil {
	/**
	 * 用字符串生成二维码
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap Create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		String outStr = null;
		Hashtable<EncodeHintType, String> hints = null;
		String encoding = guessAppropriateEncoding(str);
		if (encoding !=null) {
			hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		
		if (str != null && str.length() > 0) {
			try {
				outStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			MultiFormatWriter writer = new MultiFormatWriter();
			BitMatrix matrix = writer.encode(str, BarcodeFormat.QR_CODE, 480, 480,hints);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
					}
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap,具体参考api
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		} else {

			return null;
		}
	}
	
	/**
	 * 用字符串生成二维码
	 * 
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap Create2DCode(String str,int w ,int h) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		Log.d(">>w h", w +"," + h);
		String outStr = null;
		Hashtable<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(str);
		if (encoding !=null) {
			hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
			hints.put(EncodeHintType.MARGIN, 1);
//			EncodeHintType.DATA_MATRIX_SHAPE
		}
		
		if (str != null && str.length() > 0) {
			try {
				outStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			MultiFormatWriter writer = new MultiFormatWriter();
			BitMatrix matrix = writer.encode(str, BarcodeFormat.QR_CODE, w, h,hints);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
					}
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap,具体参考api
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			
			bitmap = Bitmap.createBitmap(bitmap, width/10, height/10, width*8/10, height*8/10, null, false);
			
			return bitmap;
		} else {

			return null;
		}
	}
	
	private static String guessAppropriateEncoding(CharSequence contents){
		for (int i = 0; i < contents.length(); i++) {
			if(contents.charAt(i)>0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
	
	

}
