package com.example.peripheraldemo;

import android.app.ProgressDialog;
import android.content.Context;

import com.viewat.util.LogUtil;

public class Tool {

	private static final String TAG = "Tool";

	/**
	 * 转16进制String
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		try {
			hs = "";
			String stmp = "";
			for (int n = 0; n < b.length; n++) {
				stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
				if (n < b.length - 1)
					hs = hs + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return hs.toUpperCase();
	}

	/**
	 * 数据加载提示框
	 * 
	 * @param context
	 * @return
	 * @author bypan
	 */
	public static ProgressDialog setProgressDialog(Context context, String msg) {
		ProgressDialog mProgress = new ProgressDialog(context);
		try {
			mProgress.setMessage(msg);
			mProgress.setCancelable(false);
			mProgress.setCanceledOnTouchOutside(false);
			mProgress.show();
		} catch (Exception e) {
			e.printStackTrace();
			mProgress = new ProgressDialog(context);
		}
		return mProgress;
	}

	/**
	 * 销毁数据加载提示框
	 * 
	 * @param dialog
	 * @author panby
	 */
	public static void dismissDialog(ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	/**
	 * 组装TrData交易数据
	 * 
	 * @param P
	 *            授权金额
	 * @param Q
	 *            其它金额
	 * @param R
	 *            交易货币代码
	 * @param S
	 *            交易日期
	 * @param T
	 *            交易类型
	 * @param U
	 *            交易时间
	 * @param V
	 *            版本号
	 * @return
	 */
	public static String packageTrData(String P, String Q, String R, String S,
									   String T, String U, String V) {
		String trData = "";
		trData = P + Q + R + S + T + U + V;
		return trData;
	}
}
