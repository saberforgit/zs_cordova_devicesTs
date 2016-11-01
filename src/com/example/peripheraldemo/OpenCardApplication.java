package com.example.peripheraldemo;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.psbc.peripheral.ComShell;
import com.psbc.peripheral.PrinterShell;
import com.viewat.util.LogUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OpenCardApplication extends Application {
	private static final String TAG = OpenCardApplication.class.getSimpleName();
	// 外设定义
	private ComShell mComShell = null;
	private PrinterShell mPrinterShell = null;

	private Context context;
	// 全局变量初始化
	public static Map Global = new HashMap();

	@SuppressWarnings("unchecked")
	public static void setGlobal(Object key, Object value) {
		Global.put(key, value);
		LogUtil.d(TAG, "关键字" + key, "关键值" + value);
	}

	public static Object getGlobal(Object key) {
		return Global.get(key);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化日志文件
		LogUtil logUtil = new LogUtil();
		//logUtil.setLogFileName("/sdcard/psbc/operate.log");
		logUtil.setLogFileName("/sdcard/mobileBusinessLog/zlzk_lib.log");
		
		// 获取本地蓝牙适配器
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		// 蓝牙关闭，强制打开蓝牙
		if (!mAdapter.isEnabled()) {
			mAdapter.enable();
		}
		myRegisterReceiver();
		// 初始化外设
		// try {
		// mComShell = new ComShell(context, "SDS", mHandler);
		// mPrinterShell = new PrinterShell(context,"Qsp",mHandler);
		// myRegisterReceiver();
		// } catch (IOException e) {
		// LogUtil.e(TAG, e.getMessage());
		// }
	}

	/**
	 * 获取ComShell对象
	 * 
	 * @return
	 */
	// public ComShell getComShell() {
	// if (mComShell != null) {
	// if (mComShell.bShellOk)
	// return mComShell;
	// else {
	// mComShell.CreateConnection();
	// if (mComShell.bShellOk) {
	// return mComShell;
	// }
	// }
	// }
	// return mComShell;
	// }

	/**
	 * 获取PrinterShell对象
	 * 
	 * @return
	 */
	// public PrinterShell getPrinterShell() {
	// Log.w("ComShell", "on getPrinterShell 00");
	// if (mPrinterShell != null) {
	// if (mPrinterShell.getBTServiceStatus()) {
	// Log.w("ComShell", "on getPrinterShell 11");
	// return mPrinterShell;
	// } else {
	// Log.w("ComShell", "on getPrinterShell 22");
	// mPrinterShell.openConnectionToBTPriner();
	// if (mPrinterShell.getBTServiceStatus()) {
	// return mPrinterShell;
	// }
	// }
	// }
	// return mPrinterShell;
	// }

	/**
	 * 获取ComShell对象
	 * 
	 * @return
	 */
	public ComShell getComShell(Context ctx) {
		ComShell comshell = null;
		try {
			comshell = (ComShell) OpenCardApplication.getGlobal("mComShell");
			if (comshell != null) {
				if (comshell.GetComShellStatus()) {
					return comshell;
				} else {
					comshell = getPeripheralComShell(ctx);
					if (comshell.GetComShellStatus()) {
						setComShell(comshell);
						return comshell;
					}
				}
			} else {
				comshell = getPeripheralComShell(ctx);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return comshell;
	}

	/**
	 * 获取各厂家可用的外设
	 * 
	 * @param ctx
	 * @return
	 */
	private ComShell getPeripheralComShell(Context ctx) {
		ComShell comshell = null;
		// 神思
		// comshell = new com.sdses.peripheral.ComShell(ctx, "SDS", null);
		// if (comshell.GetComShellStatus()) {
		// setComShell(comshell);
		// return comshell;
		// }

		try {
			comshell = new com.zlzk.peripheral.ComShell(ctx, "PSBC-ZL-JCWS",
					null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (comshell.GetComShellStatus()) {
			setComShell(comshell);
			return comshell;
		}
		// TODO 请各位厂商人员在此写自家comshell的类
		return null;
	}

	/**
	 * 获取ComShell对象
	 * 
	 * @return
	 */
	public PrinterShell getPrinterShell(Context ctx) {
		PrinterShell printShell = null;
		try {
			printShell = (PrinterShell) OpenCardApplication
					.getGlobal("mPrintShell");
			if (printShell != null) {
				if (printShell.GetPrinterShellStatus()) {
					return printShell;
				} else {
					printShell = getUsefulPrinterShell(ctx);
					if (printShell.GetPrinterShellStatus()) {
						setPrintShell(printShell);
						return printShell;
					}
				}
			} else {
				printShell = getUsefulPrinterShell(ctx);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return printShell;
	}

	/**
	 * 查找厂家中可用的打印机
	 * 
	 * @param ctx
	 * @return
	 */
	private PrinterShell getUsefulPrinterShell(Context ctx) {
		PrinterShell printShell = null;
		// 神思
		printShell = new com.sdses.peripheral.PrinterShell(ctx, "Qsp", mHandler);
		// printShell = new
		// com.sdses.peripheral.PrinterShell(getApplicationContext(), "Qsp",
		// null);
		if (printShell.GetPrinterShellStatus()) {
			setPrintShell(printShell);
			return printShell;
		}
		// 南天

		// if (printShell.GetPrinterShellStatus()) {
		// return printShell;
		// }
		// 国光
		// printShell = new com.guoguang.peripheral.PrinterShell(ctx, "",
		// mHandler);
		// if (printShell.GetPrinterShellStatus()) {
		// return printShell;
		// }
		// TODO 请各位外设人员在此写自家打印机的类

		return null;
	}

	// 将comshell对象存储到application中
	public void setComShell(ComShell mComShell) {
		setGlobal("mComShell", mComShell);
	}

	// 将PrinterShell对象存储到application中
	public void setPrintShell(PrinterShell mPrintShell) {
		setGlobal("mPrintShell", mPrintShell);
	}

	// 从application中移除comshell
	public void removeComShell() {
		try {
			OpenCardApplication.Global.remove("mComShell");
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	// 从application中移除PrinterShell
	public void removePrinterShell() {
		try {
			OpenCardApplication.Global.remove("mPrintShell");
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 终止外设连接
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		unregisterReceiver(LocalReciever);
		LogUtil.w("ComShell", "OpenCardApplication onTerminate 00");
		if (mComShell != null) {
			try {
				mComShell.Destroy();
				mComShell = null;
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtil.w("ComShell", "OpenCardApplication onTerminate 11");
	}

	/**
	 * 注册BroadcastReceiver监听器
	 */
	private void myRegisterReceiver() {
		registerReceiver(LocalReciever, new IntentFilter(
				"android.bluetooth.device.action.ACL_CONNECTED"));
		registerReceiver(LocalReciever, new IntentFilter(
				"android.bluetooth.device.action.ACL_DISCONNECTED"));
		registerReceiver(LocalReciever, new IntentFilter(
				"android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED"));

	}

	private BroadcastReceiver LocalReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"android.bluetooth.device.action.ACL_DISCONNECTED")) {
				LogUtil.w("ComShell",
						"In BroadcastReceiver ACL_DISCONNECTED...");
				// 此时可以弹出对话框提示用户 蓝牙的连接已断开
				// Tool.publicAlertDialog(context,context.getResources().getString(R.string.bluetoothunconnect));
			}
			if (intent.getAction().equals(
					"android.bluetooth.device.action.ACL_CONNECTED")) {
				LogUtil.w("ComShell", "In BroadcastReceiver ACL_CONNECTED...");
			}
		}
	};

	public static final int MESSAGE_STATE_CHANGE = 1;
	Handler mHandler = new Handler();
	// Handler mHandler = new Handler(){//处理UI绘制
	// //mhandler = new Handler() {
	// public void handleMessage(Message msg) {
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case MESSAGE_STATE_CHANGE:// 蓝牙连接状态
	// switch (msg.arg1) {
	// case BlueToothService.STATE_CONNECTED:
	// Log.w("ComShell","BlueToothService.STATE_CONNECTED");
	// break;
	// case BlueToothService.STATE_CONNECTING:
	// Log.w("ComShell","BlueToothService.STATE_CONNECTING");
	// break;
	// case BlueToothService.STATE_LISTEN:
	// Log.w("ComShell","BlueToothService.STATE_LISTEN");
	// break;
	// case BlueToothService.STATE_NONE:
	// Log.w("ComShell","BlueToothService.STATE_NONE");
	// break;
	// case BlueToothService.SUCCESS_CONNECT:
	// // mPrinterShell.setBTServiceStatus(true);
	// Log.w("ComShell","BlueToothService.SUCCESS_CONNECT");
	// break;
	// case BlueToothService.FAILED_CONNECT:
	// // mPrinterShell.setBTServiceStatus(false);
	// Log.w("ComShell","BlueToothService.FAILED_CONNECT");
	// break;
	// case BlueToothService.LOSE_CONNECT:
	// // mPrinterShell.setBTServiceStatus(false);
	// Log.w("ComShell","BlueToothService.LOSE_CONNECT");
	// break;
	// }
	// break;
	// }
	// }
	// };

	// ------------新加
	// 建立一个用来存储所有Activity对象的list
	private List<Activity> activities = null;
	private static OpenCardApplication instance;

	public OpenCardApplication() {
		activities = new LinkedList();
	}

	/**
	 * 单例模式中获取唯一的CreditCardApplication实例
	 */
	public static OpenCardApplication getInstance() {
		if (null == instance) {
			instance = new OpenCardApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activities != null && activities.size() > 0) {
			if (!activities.contains(activity)) {// 如果没有放过该Activity，那么就放到list列表中去
				activities.add(activity);
			}
		} else {
			activities.add(activity);
		}
	}

	/**
	 * 遍历关闭所有Activity
	 */
	public void closeActivities() {
		if (activities != null && activities.size() > 0) {
			for (int i = 0; i < activities.size(); i++) {
				Activity myActivity = activities.get(i);
				myActivity.finish();
			}
		}
		System.exit(0);
	}

}
