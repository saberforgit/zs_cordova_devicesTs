package com.sdses.fpapplication;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sdses.peripheral.ComShell;
import com.sdses.peripheral.PrinterShell;
import com.sdses.serialport.BlueToothService;

public class SdsesBacnCard {
	private static ComShell mComShell=null;
	private static com.psbc.peripheral.PrinterShell mPrinterShell=null;
	private static final int REQUEST_ENABLE_BT = 2;

	public static void initApp(Activity mContext) {
		try {
			mComShell = new ComShell(mContext.getApplicationContext(),"",mhandler);
			mPrinterShell = new PrinterShell(mContext.getApplicationContext(),"Qsp",mhandler);
			myRegisterReceiver(mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void myRegisterReceiver(Activity mContext){
		mContext.registerReceiver(LocalReciever, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
		mContext.registerReceiver(LocalReciever, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
		mContext.registerReceiver(LocalReciever, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED"));
	}  
	private static BroadcastReceiver LocalReciever = new BroadcastReceiver() {
	  	@Override
		public void onReceive(Context context, Intent intent) {
	  		if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
	  			Log.w("ComShell","In BroadcastReceiver ACL_DISCONNECTED...");
	  			//此时可以弹出对话框提示用户 蓝牙的连接已断开
	  			
	  		}
	  		if (intent.getAction().equals("android.bluetooth.device.action.ACL_CONNECTED")) {
	  			Log.w("ComShell","In BroadcastReceiver ACL_CONNECTED...");
	  		}
	  	}
	};
/*	public ComShell getComShell(){
		if(mComShell!=null){
			if(mComShell.bShellOk)
				return mComShell;
			else{
				mComShell.CreateConnection();
				if(mComShell.bShellOk){
					return mComShell;
				}
			}
		}
		return mComShell;
	}*/
	public static ComShell getComShell(Activity mContext){
		if(mComShell==null){
			mComShell = new ComShell(mContext,"PSB",mhandler);
		}

	return (ComShell) mComShell;
}
//
//	public PrinterShell getPrinterShell(){
//			if(mPrinterShell==null){
//				mPrinterShell = new PrinterShell(getApplicationContext(),"Qsp",mhandler);
//			}
//
//		return (PrinterShell) mPrinterShell;
//	}
//
//

/*	public PrinterShell getPrinterShell(){
		Log.w("ComShell","on getPrinterShell 00");
		if(mPrinterShell!=null){
			if(mPrinterShell.getBTServiceStatus()){
				Log.w("ComShell","on getPrinterShell 11");
				return mPrinterShell;
			}else{
				Log.w("ComShell","on getPrinterShell 22");
				mPrinterShell.openConnectionToBTPriner();
				if(mPrinterShell.getBTServiceStatus()){
					return mPrinterShell;
				}
			}
		}
		return mPrinterShell;
	}*/
//	@Override
//	public void onTerminate() {
//		super.onTerminate();
//    	unregisterReceiver(LocalReciever);
//		Log.w("ComShell", "SdsesBacnCardApplication onTerminate 00");
//		//if((mComShell!=null)&&(mComShell.bShellOk)){
//		if(mComShell!=null){
//			Log.w("ComShell", " onTerminate 11");
//			try {
//				mComShell.Destroy();
//				mComShell = null;
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		Log.w("ComShell", "SdsesBacnCardApplication onTerminate 11");
//		//if((mPrinterShell!=null)&&(mPrinterShell.getBTServiceStatus())){
//		if(mPrinterShell!=null){
//			Log.w("ComShell", " onTerminate 22");
//			try {
//				mPrinterShell.Destroy();
//				mPrinterShell = null;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.exit(0);//
//	}
	private static final int REQUEST_EX = 1;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
    public static Handler mhandler = new Handler(){//处理UI绘制
	//mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:// 蓝牙连接状态
				switch (msg.arg1) {
				case BlueToothService.STATE_CONNECTED:
					Log.w("ComShell","BlueToothService.STATE_CONNECTED");
					break;
				case BlueToothService.STATE_CONNECTING:
					Log.w("ComShell","BlueToothService.STATE_CONNECTING");
					break;
				case BlueToothService.STATE_LISTEN:
					Log.w("ComShell","BlueToothService.STATE_LISTEN");
					break;
				case BlueToothService.STATE_NONE:
					Log.w("ComShell","BlueToothService.STATE_NONE");
					break;
				case BlueToothService.SUCCESS_CONNECT:
				//	mPrinterShell.setBTServiceStatus(true);
					Log.w("ComShell","BlueToothService.SUCCESS_CONNECT");
					break;
				case BlueToothService.FAILED_CONNECT:
					//mPrinterShell.setBTServiceStatus(false);
					Log.w("ComShell","BlueToothService.FAILED_CONNECT");
					break;
				case BlueToothService.LOSE_CONNECT:
					//mPrinterShell.setBTServiceStatus(false);
					Log.w("ComShell","BlueToothService.LOSE_CONNECT");
					break;
				}
				break;
			}
		}
	};
}
