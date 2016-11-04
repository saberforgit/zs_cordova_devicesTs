package com.example.peripheraldemo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.psbc.peripheral.ComShell;
import com.psbc.peripheral.M03PForBank;
import com.psbc.peripheral.PrinterShell;
import com.viewat.util.LogUtil;
import com.zsmarter.device.R;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";

	View rootView;
	WebView webView;
	PeripheralSS ss = null;
	private M03PForBank m03pBank = null;
	private ComShell mComShell;
	private PrinterShell mPrinterShell;
	private OpenCardApplication myApplication;
	Context ctx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		myApplication = (OpenCardApplication) getActivity().getApplication();
		ctx = getActivity();
		rootView = inflater.inflate(R.layout.base_fragment, container, false);
		webView = (WebView) rootView.findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();
		// 能够执行javascript脚本
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("utf-8");
		// 弹出对话框
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient());

		// 点击获取外设
		webView.addJavascriptInterface(new ToPeripheral(), "doPeripheral");
		webView.loadUrl("file:///android_asset/html/NetworkCheck.html");

		return rootView;
	}

	/**
	 * 外设内部类,用于连接外设，传递数据
	 * 
	 * @author lenvov
	 */
	class ToPeripheral {
		@JavascriptInterface
		public void toPeripheral(final int key) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mComShell = myApplication.getComShell(ctx);
					if (key == 5 || key == 7 || key == 8) {
						mPrinterShell = myApplication.getPrinterShell(ctx);
					}
					ss = new PeripheralSS(mComShell, mPrinterShell);
					if (mComShell == null)
						LogUtil.w(TAG, "建立蓝牙连接失败");
					else {
						int nRet;
						switch (key) {
						case 1: // 读取二代身份证信息
							ss.showAlertDialog(getActivity(), webView,
									mHandler, R.string.putidcard, 1);
							break;
						case 2: // 获取（磁条卡）卡号

							nRet = mComShell.OpenDevice(2, 10000, null);// 打开磁条卡设备时不需要指定消息接收的Handler
							Log.e("",
									"mComShell.OpenDevice(2, 10000, null) nRet = "
											+ nRet);
							if (nRet == 0) {
								ss.showAlertDialog(getActivity(), webView,
										mHandler, R.string.getcardinfo, 2);
							} else {
								Toast.makeText(getActivity(),
										R.string.openfailure,
										Toast.LENGTH_SHORT).show();
							}
							break;
						case 3: // 获取密文键盘值
							ss.getEncyptionKeyPin(webView, getActivity());
							break;
						case 4: // 写卡
							LogUtil.w(TAG, "写卡开始");
							ss.writeIcCard(webView, getActivity());
							break;
						case 5: // 卡申请单据打印
							ss.printerContent(webView, getActivity(),
									String.valueOf(key));
							break;
						case 6: // 读取IC卡
							try {
								ss.readIcCard(getActivity(), webView);
								// readIcCard();
							} catch (Exception e) {
								LogUtil.e(TAG, "读取IC卡 erro: " + e.getMessage());
							}
							break;
						case 7: // 卡激活单据打印
							ss.printerContent(webView, getActivity(),
									String.valueOf(key));
							break;
						case 8: // 渠道开通单据打印
							ss.printerContent(webView, getActivity(),
									String.valueOf(key));
							break;
						default:
							break;
						}
					}
				}
			});
		}
	}

	Handler mHandler = new Handler();
}
