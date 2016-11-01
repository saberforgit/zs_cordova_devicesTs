package com.example.peripheraldemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.viewat.common.Gobal;
import com.viewat.util.ByteUtil;
import com.viewat.util.LogUtil;
import com.viewat.util.StringUtil;
import com.zlzk.peripheral.ComShell;
import com.zlzk.peripheral.M03PForBank;
import com.zsmarter.device.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.zsmarter.device.R.*;

public class MyTestActivity extends Activity implements OnClickListener {

	private Button IdCardBtn;
	private Button mBtnGetICCardInfo;
	private Button getmscBtn;

	byte[] returnBuff = new byte[2048];
	int[] returnBuffLen = new int[1];
	public ComShell comShell = null;

	String mStrResult = null;
	int ret;
	byte slot = 0;
	int mcrNum = 0;

	byte[] encrypId = new byte[13];

	public static final String tag = "SomeActivity";
	private static final String TAG = "SomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.activity_main_vpos);
		comShell = new ComShell(this, "PSBC-ZL-JCWS", handler);// 1978 0322
		IdCardBtn = (Button) findViewById(id.IdCardBtn);
		IdCardBtn.setOnClickListener(this);

		mBtnGetICCardInfo = (Button) findViewById(id.getBtn);
		mBtnGetICCardInfo.setOnClickListener(this);

		getmscBtn = (Button) findViewById(id.getmscBtn);
		getmscBtn.setOnClickListener(this);

		if (!comShell.GetComShellStatus()) {
			Toast.makeText(getApplicationContext(), "Connect BT Fail",
					Toast.LENGTH_LONG).show();
		}

		testBmp();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		comShell.Destroy();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		switch (view.getId()) {
		case id.IdCardBtn:
			new Test_Thread().start();
			break;

		case id.getBtn:
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					M03PForBank ban = new M03PForBank();
					byte[] AIDList;
					String aidlist = "A000000333010101";
					AIDList = aidlist.getBytes();
					String taglist = "ABCDEFGHIJ";
					byte[] TagList = taglist.getBytes();
					byte[] UserInfo = new byte[2048];
					byte[] IcTrack2Data = new byte[256];
					ret = ban.SDSS_GetICCardInfo(slot, AIDList, TagList,
							UserInfo, IcTrack2Data);
					if (ret == 0) {
						mStrResult = "UserInfo: " + new String(UserInfo);
						mStrResult += "\nIcTrack2Data: "
								+ ByteUtil.bytearrayToHexString(IcTrack2Data,
										20);
					} else {
						mStrResult = "获取UserInfo失败";
					}

					Log.i(tag, "SDSS_GetICCardInfo mStrResult = " + mStrResult);
					ShowMsg(mStrResult, 12);
				}
			}).start();

			break;
		case id.getmscBtn:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					comShell.OpenDevice(2, 10000, handler);
					Arrays.fill(returnBuff, (byte) 0);

					if (++mcrNum > 9)
						mcrNum = 1;

					ret = comShell.ReadInfo(2, mcrNum, returnBuff,
							returnBuffLen);
					Log.d("----", "returnBuffLen==" + returnBuffLen[0]);
					if (ret == 0) {
						if (mcrNum <= 5)
							mStrResult = "获取磁道数据成功: " + mcrNum + "      "
									+ new String(returnBuff).trim();
						else
							mStrResult = "获取磁道数据成功: "
									+ mcrNum
									+ "      "
									+ ByteUtil.bytearrayToHexString(returnBuff,
											returnBuffLen[0]);
					} else {
						mStrResult = "获取磁道数据失败";
					}

					ShowMsg(mStrResult, 12);

					comShell.CloseDevice(2);
				}
			}).start();
			break;
		default:
			break;
		}
	}

	private void ShowMsg(String str, int what) {
		Message msg = new Message();
		msg.what = what;
		Bundle b = new Bundle();
		b.putString("MSG", str);
		msg.setData(b);
		handler.sendMessage(msg);
	}

	public class Test_Thread extends Thread {

		@Override
		public void run() {
			int ret;
			String str = null;
			returnBuff = new byte[2048];
			returnBuffLen = new int[1];

			comShell.OpenDevice(1, 10000, handler);
			ret = comShell.readIDCardInfo(returnBuff, returnBuffLen, 10000);
			if (ret == 0) {
				comShell.GetPic("");

				str = "姓名：" + comShell.GetName(returnBuff) + "\n";
				str += "性别：" + comShell.GetGender(returnBuff) + "\n";
				str += "民族：" + comShell.GetNational(returnBuff) + "\n";
				str += "生日：" + comShell.GetBirthday(returnBuff) + "\n";
				str += "身份证号码：" + comShell.GetIndentityCard(returnBuff) + "\n";
				str += "签证机关：" + comShell.GetIssued(returnBuff) + "\n";
				str += "有效日期：" + comShell.GetStartDate(returnBuff) + "\n";
				str += "有效日期：" + comShell.GetEndDate(returnBuff) + "\n";
				str += "出生年份：" + comShell.getYear(returnBuff) + "\n";
				str += "出生月份：" + comShell.getMonth(returnBuff) + "\n";
				str += "出生日：" + comShell.getDay(returnBuff) + "\n";

			} else {
				str = "读取二代证失败";
			}
			ShowMsg(str, 12);

			comShell.CloseDevice(1);
		}
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case Gobal.READIDCARD:
				// String name = comShell.GetName(returnBuff);
				// Log.d("TAG",
				// "mmm="+name+"="+comShell.GetAddress(returnBuff));
				// comShell.GetPic("");

				break;
			case 7:
				Bundle b1 = msg.getData();
				String strInfo1 = b1.getString("MSG");
				Toast.makeText(MyTestActivity.this, strInfo1, Toast.LENGTH_SHORT).show();
				break;
			case Gobal.OPENIDCARD:

				break;
			case 8:
				Toast.makeText(MyTestActivity.this, "关闭成功", Toast.LENGTH_SHORT).show();
				break;
			case Gobal.OPENMSC:
				Toast.makeText(MyTestActivity.this, "打开成功", Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Bundle b = msg.getData();
				String strInfo = b.getString("MSG");
				showDialog(strInfo);
				break;

			default:
				break;
			}
		};
	};

	public void showDialog(String info) {

		final EditText editText1 = new EditText(this);
		editText1.setEnabled(false);

		editText1.setText(info);
		new AlertDialog.Builder(this).setTitle("结果")
				.setIcon(android.R.drawable.ic_dialog_info).setView(editText1)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).setNegativeButton("取消", null).show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Api.isQuit_flag_set(false);
		Log.i("onStart", " onStart ");

		// pVaIDCard = new VaIDCard(m_handler);// 如不想处理消息则传入null

		// int iRetCode = Api.IDCard_Open();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Api.isQuit_flag_set(true);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private static String findSexZW(int sexcode) {
		if (sexcode == 0)
			return "未知";
		else if (sexcode == 1)
			return "男";
		else if (sexcode == 2)
			return "女";
		else if (sexcode == 9)
			return "未说明";
		return "未说明";
	}

	static String[][] Nations = new String[][] { { "无" }, { "汉" }, { "蒙古" },
			{ "回" }, { "藏" }, { "维吾尔" }, { "苗" }, { "彝" }, { "壮" }, { "布依" },
			{ "朝鲜" }, { "满" }, { "侗" }, { "瑶" }, { "白" }, { "土家" }, { "哈尼" },
			{ "哈萨克" }, { "傣" }, { "黎" }, { "傈僳" }, { "佤" }, { "畲" }, { "高山" },
			{ "拉祜" }, { "水" }, { "东乡" }, { "纳西" }, { "景颇" }, { "柯尔克孜" },
			{ "土" }, { "达斡尔" }, { "仫佬" }, { "羌" }, { "布朗" }, { "撒拉" },
			{ "毛南" }, { "仡佬" }, { "锡伯" }, { "阿昌" }, { "普米" }, { "塔吉克" },
			{ "怒" }, { "乌孜别克" }, { "俄罗斯" }, { "鄂温克" }, { "德昂" }, { "保安" },
			{ "裕固" }, { "京" }, { "塔塔尔" }, { "独龙" }, { "鄂伦春" }, { "赫哲" },
			{ "门巴" }, { "珞巴" }, { "基诺" }, { "其他" }, { "外国血" } };

	public static String findMinzuZW(int minzucode) {
		if (minzucode > 59)
			return "";
		return Nations[minzucode][0];
	}

	void testBmp() {

		// 38862 38556 306 132
		FileInputStream fis;
		try {
			InputStream fiss = new FileInputStream(
					Environment.getExternalStorageDirectory()
							+ "/id2picture/zp.bmp");
			int avail = 0;
			try {
				avail = fiss.available();
				Log.i("bmpData", " avail = " + avail);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("bmpData", " avail = " + avail);
			byte[] bmpData = new byte[avail];
			// byte[] zero = new byte[304];
			int len = 0;
			try {
				len = fiss.read(bmpData);
				Log.i("bmpData", "bmpData: len = " + len);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.i("bmpData", "bmpData: len = " + len);
			System.arraycopy(bmpData, 54 + 102 * 3, bmpData, 54, 102 * 3 * 125);

			Log.i("bmpData",
					"bmpData: " + ByteUtil.bytearrayToHexString(bmpData, 1024));
			try {
				fiss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ "/id2picture/zp.bmp");

			try {
				fos.write(bmpData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
