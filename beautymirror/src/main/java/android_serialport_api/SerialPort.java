/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialPort {

	enum PARITY_CHECK{
		NONE,
		ODD,
		EVEN,		
	}
	
	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;
	public byte[] m_revbuf;
	public String m_revstr = "";
	public int m_len;

	// 校验用什么，用check来区分
	public SerialPort(File device, int baudrate, int flags, int len,int check) throws SecurityException, IOException {
		m_len = len;
		m_revbuf = new byte[len];
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "777 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate, flags, check);// PARITY_CHECK.EVEN.ordinal()
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	// JNI
	/**
	 * open serial port and set parameters
	 * @param path
	 * @param baudrate
	 * @param flags
	 * @param checkMethod
	 * @return
	 */
	private native static FileDescriptor open(String path, int baudrate, int flags, int checkMethod);
	public native void close();
	//public native int recv();
	//public native int send();
	public native int recv(byte[] m_revbuf2,int length);
	public native int send(byte[] sendBuf,int length);
	
    /*public static void setData(byte[] data){
    	AndroidUARTCommApi.data = new byte[data.length];
    	System.arraycopy(data, 0, AndroidUARTCommApi.data, 0, data.length);
    }*/
    
	static {
		System.loadLibrary("serial_port");
	}
}
