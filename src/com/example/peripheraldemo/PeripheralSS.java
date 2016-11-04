package com.example.peripheraldemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.psbc.peripheral.ComShell;
import com.psbc.peripheral.M03PForBank;
import com.psbc.peripheral.PrinterShell;
import com.sdses.tool.Util;
import com.viewat.util.LogUtil;
import com.zsmarter.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 神思外设
 * 
 * @author lenvov
 */
@SuppressLint("NewApi")
public class PeripheralSS {
	private static final String TAG = PeripheralSS.class.getSimpleName();
	private M03PForBank m03pBank = null;
	private ComShell mComShell;
	private PrinterShell mPrinterShell;

	// 初始化方法
	public PeripheralSS(ComShell mComShell, PrinterShell printerShell) {
		this.mComShell = mComShell;
		this.mPrinterShell = printerShell;

		if (mComShell instanceof com.zlzk.peripheral.ComShell) { // 神思
			m03pBank = new com.zlzk.peripheral.M03PForBank();
		}

		// TODO 请各位外设人员在此写自家M03PForBank的类

		// if(mComShell instanceof com.nt.peripheral.ComShell.NtComShell){ //南天
		// m03pBank = new com.nt.peripheral.M03PForBank.NtM03PForBank();
		// }
		// if(mComShell instanceof com.guoguang.peripheral.ComShell){ //国光
		// m03pBank = new com.guoguang.peripheral.M03PForBank();
		// }
		// m03pBank = new com.nt.peripheral.M03PForBank.NtM03PForBank();
	}

	/**
	 * 读取外设公共弹出提示框
	 * 
	 * @param ctx
	 *            上下文
	 * @param msg
	 *            提示信息
	 * @param type
	 *            外设交易类型
	 */
	public void showAlertDialog(final Context ctx, final WebView webView,
								final Handler mHandler, int msg, final int type) {
		new AlertDialog.Builder(ctx).setTitle("DSFSF").setMessage(msg)
				.setPositiveButton("que ren", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						int nRet;
						switch (type) {
						case 1: // 读取二代身份证信息
							try {
								nRet = mComShell.OpenDevice(1, 10000, mHandler);// 打开二代证设备
								if (nRet == 0) { // 打开二代证成功
									JSONObject jo = getId2Card(ctx, webView);
									if (jo != null) {
										webView.loadUrl("javascript:show2IdCard("
												+ jo + ")");
									}
									LogUtil.e(TAG, "JO len = " + jo.length()
											+ " Value = " + jo.toString());
								} else { // 打开二代证失败
									LogUtils.d("打开失败");
								}
							} catch (Exception e) {
								e.printStackTrace();
								LogUtil.e(TAG, e.getMessage());
							}
							break;
						case 2: // 获取（磁条卡）卡号
							getMagneticCardInfo(ctx, webView, mHandler);
							break;
						default:
							break;
						}
					}
				}).show();
	}

	/**
	 * 获取二代身份证信息
	 */
	public JSONObject getId2Card(Context ctx, WebView webView) {
		try {
			JSONObject jo = null;
			byte[] returnBuff = new byte[1280];
			byte[] wzinfo = new byte[256];
			int deviceId = 1;
			int para = 1;
			int[] returnBuffLen = new int[1];
			LogUtil.d(TAG, "准备读取身份证");
			int nRet = mComShell.ReadInfo(deviceId, para, returnBuff,
					returnBuffLen);
			LogUtil.d(TAG, "身份证读取结果nRet：" + nRet);
			if ((nRet == 0) && (returnBuffLen[0] > 0)) {
				System.arraycopy(returnBuff, 0, wzinfo, 0, 256);
				try {
					LogUtil.w(TAG, "取卡信息成功");
					jo = new JSONObject();
					jo.put("name", mComShell.GetName(wzinfo)); // 姓名
					jo.put("idNo", mComShell.GetIndentityCard(wzinfo)); // 身份证号
					jo.put("gender", mComShell.GetGender(wzinfo)); // 性别
					jo.put("nation", mComShell.GetNational(wzinfo)); // 民族
					jo.put("birthday", mComShell.GetBirthday(wzinfo)); // 出生日期
					jo.put("address", mComShell.GetAddress(wzinfo)); // 住址
					jo.put("issued", mComShell.GetIssued(wzinfo)); // 签发机关
					jo.put("startAndEndDate", mComShell.GetStartDate(wzinfo)
							+ "-" + mComShell.GetEndDate(wzinfo)); // 有效期限

					// 将证件信息保存到application中
					OpenCardApplication.setGlobal("sex", String
							.valueOf(sexStringToNumber(mComShell
									.GetGender(wzinfo))));
					String busiLicsExpDate = mComShell.GetEndDate(wzinfo);
					busiLicsExpDate = busiLicsExpDate.replace("年", "")
							.replace("月", "").replace("日", "");
					OpenCardApplication.setGlobal("busiLicsExpDate",
							busiLicsExpDate);
					OpenCardApplication.setGlobal("certAddr",
							mComShell.GetIssued(wzinfo));

					// 获取并存储照片
					// String authFilePath = "/mnt/sdcard/";
					String authFilePath = "/mnt/sdcard/id2picture";
					int nret = 0;
					// try {
					// 获取照片
					nret = mComShell.GetPic(authFilePath);
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					if (nret > 0) {
						LogUtil.w(TAG, "获取图片成功" + nret);// getFilesDir()
						// jo.put("url", "/mnt/sdcard/zp.bmp");

						File file = new File("/mnt/sdcard/id2picture");
						File[] files = file.listFiles();
						File imgFile = files[0];
						String imgPath = imgFile.getAbsolutePath();
						// String imgPath = "mnt/sdcard/id2picture/zp.bmp";
						Log.e("", "imgPath = " + imgPath);// /mnt/sdcard/id2picture/base.dat
						jo.put("url", imgPath);
					} else {
						Toast.makeText(ctx, "获取图片失败，请重新获取", Toast.LENGTH_SHORT)
								.show();
						LogUtil.w(TAG, "获取图片失败" + nret);
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				} finally {
					mComShell.CloseDevice(1);
				}
			} else {
				LogUtil.w(TAG, "读取二代身份证信息失败");
			}
			// if (jo != null) {
			// webView.loadUrl("javascript:show2IdCard(" + jo + ")");
			// }
			return jo;
		} catch (Exception e) {
			LogUtil.e(TAG, "");
		} finally {
			mComShell.CloseDevice(1);
		}
		return null;
	}

	private int sexStringToNumber(String sexStr) {
		int sex = 0;
		try {
			if ("男".equals(sexStr)) {
				sex = 1;
			} else if ("女".equals(sexStr)) {
				sex = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return sex;
	}

	// Handler handler= new Handler() {
	// public void handleMessage(android.os.Message msg) {
	//
	// };
	// };

	/**
	 * 获取密文键盘值
	 * 
	 * @param webView
	 * @param ctx
	 */
	public void getEncyptionKeyPin(final WebView webView, final Context ctx) {
		final ProgressDialog dialog = Tool.setProgressDialog(ctx, "请输入密码");
		dialog.show();
		// final ProgressDialog dialog = new ProgressDialog(ctx);
		// dialog.setMessage(ctx.getString(R.string.putpassing));
		// dialog.show();
		String asymType = (String) OpenCardApplication.getGlobal("asymType");
		int length = 8; // 国际8，国密16
		if (AppConstans.ASYMTYPE2.equals(asymType)) { // 国密
			length = 16;
		}
		final byte[] recvBuff = new byte[length];
		final byte[] desBuff = new byte[8];
		// final String cardNum = (String) OpenCardApplication
		// .getGlobal("cardNumber");
		final String cardNum = "6230580000030375534";
		if ("".equals(cardNum) || null == cardNum) {
			dialog.dismiss();

			return;
		}
		// mComShell.OpenJpMode();// 123456
		final byte keyIndex = 1;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int nRet = mComShell.GetKeyPin(true, cardNum, recvBuff,
							keyIndex, 10000);
					JSONObject obj = new JSONObject();
					if (nRet == 0) {
						Tool.dismissDialog(dialog);
						try {
							String psw = Util.toHexStringNoSpace(recvBuff,
									recvBuff.length);
							if (!"".equals(psw)
									&& !"0300000000000000".equals(psw)) {
								obj.put("password", psw);
								webView.loadUrl("javascript:showFirstPass("
										+ obj + ")");
							} else {
								psw = "";
								obj.put("password", psw);
								webView.loadUrl("javascript:showFirstPass("
										+ obj + ")");
								Looper.prepare();
								Toast.makeText(ctx, "输入密码不能为空且长度必须为六位",
										Toast.LENGTH_SHORT).show();
								Looper.loop();

							}
						} catch (JSONException e) {
							e.printStackTrace();
							LogUtil.e(TAG, "showAlertDialog JSONException: "
									+ e.getMessage());
						} finally {
							mComShell.CloseDevice(51);
							// mComShell.CloseJpMode();
						}
					} else if (nRet == -2) {
						try {
							Looper.prepare();
							mComShell.CloseDevice(51);
							// mComShell.CloseJpMode();
							Tool.dismissDialog(dialog);
							obj.put("password", "");
							webView.loadUrl("javascript:showFirstPass(" + obj
									+ ")");
							Toast.makeText(ctx, "接收按键超时", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (nRet == -1) {
						try {
							obj.put("password", "");
							webView.loadUrl("javascript:showFirstPass(" + obj
									+ ")");
							Looper.prepare();
							mComShell.CloseDevice(51);
							// mComShell.CloseJpMode();
							Tool.dismissDialog(dialog);
							Toast.makeText(ctx, "取按键值失败", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else if (nRet == -3) {
						try {
							obj.put("password", "");
							webView.loadUrl("javascript:showFirstPass(" + obj
									+ ")");
							Looper.prepare();
							mComShell.CloseDevice(51);
							// mComShell.CloseJpMode();
							Tool.dismissDialog(dialog);
							Toast.makeText(ctx, "密码长度不足6位", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.e(TAG, e.getMessage());
				} finally {
					mComShell.CloseDevice(51);
					// mComShell.CloseJpMode();
				}
			}
		}).start();
	}

	/**
	 * 写卡
	 * 
	 * @param ctx
	 */
	public void writeIcCard(WebView webView, Context ctx) {
		try {
			String trAppDataStr = (String) OpenCardApplication
					.getGlobal("trAppData");
			String ARPC = (String) OpenCardApplication.getGlobal("arpc");
			// String pBoc = (String) OpenCardApplication.getGlobal("pBoc");
			String trData = (String) OpenCardApplication.getGlobal("trData");

			LogUtil.w(TAG, "TrAppData is: " + trAppDataStr);
			// 交易应用数据，将在检验ARPC时作为传入的数据。

			LogUtil.w(TAG, "pBoc is: " + ARPC);
			// 授权响应密文，后台返回的银联规范的55域数据，ASCII码的形式。
			// 若有执行脚本，需在后面添加脚本数据域，再执行脚本，没有脚本，则完成操作

			LogUtil.w(TAG, "trData is: " + trData);
			// P012000000000000Q012000000000000R003156S00820150726T00217U006155350V000

			byte[] scriptResult = new byte[1024];
			byte[] TC = new byte[1024];
			int nRet = m03pBank.SDSS_ExecuteScript(0x00, trData.getBytes(),
					ARPC.getBytes(), trAppDataStr.getBytes(), scriptResult, TC);
			if (nRet == -1) {
				Toast.makeText(ctx, "写卡失败！", Toast.LENGTH_SHORT).show();
			} else { // 写卡成功
				String str = new String(scriptResult);
				LogUtil.d(TAG, "写卡成功：" + str.trim());
				Toast.makeText(ctx, "写卡成功", Toast.LENGTH_SHORT).show();
				// 写卡成功，进行单据打印
				printerContent(webView, ctx, String.valueOf(5));
				// webView.loadUrl("javascript:disableButton()");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 单据打印
	 * 
	 * @param ctx
	 * @param flag
	 *            5-卡申请打印，6-卡激活单据打印，7-渠道开通单据打印
	 */
	public void printerContent(WebView webView, Context ctx, String flag) {
		try {
			List<Vector> content = new ArrayList<Vector>();
			Vector v = new Vector<Map<String, Object>>();

			// ******公共打印部分*********
			// 流水号
			String seqNo = (String) OpenCardApplication.getGlobal("seqNo");

			// TODO 标识
			// ******公共打印部分*********

			HashMap<String, Object> map1 = new HashMap<String, Object>();
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			HashMap<String, Object> map3 = new HashMap<String, Object>();

			map1.put("标题一", "卡申请");
			map1.put("标题二", "流水号");
			map1.put("标题三", "绿卡通IC卡申请成功");
			v.add(map1);
			v.add(map2);
			v.add(map3);

			content.add(v);
			// 打印两份单据（客户一份，留底一份）
			boolean bRet1 = mPrinterShell.printTicket(1, "中国邮政储蓄银行移动展业交易凭条",
					content);
			boolean bRet2 = mPrinterShell.printTicket(1, "中国邮政储蓄银行移动展业交易凭条",
					content);
			if (bRet1 && bRet2) {
				LogUtil.w(TAG, "打印成功！");
				Toast.makeText(ctx, "打印成功！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ctx, "打印失败！", Toast.LENGTH_SHORT).show();
				LogUtil.w(TAG, "打印失败！bRet1： " + bRet1 + ",bRet2: " + bRet2);
			}
		} catch (Exception e) {
			Toast.makeText(ctx, "打印失败！", Toast.LENGTH_SHORT).show();
			LogUtil.e(TAG, "printerContent error: " + e.getMessage());
		}
	}

	/**
	 * @param ctx
	 * @param webView
	 */
	public void readIcCard(final Context ctx, final WebView webView) {
		LogUtil.d(TAG, "读取IC卡");
		// final RotateProgressDialog dialog = new RotateProgressDialog(ctx);
		// dialog.setMessage(ctx.getString(R.string.readicing));
		// dialog.show();

		final ProgressDialog dialog = Tool.setProgressDialog(ctx,
				"正在读取IC卡信息，请稍候...");

		new Thread(new Runnable() {
			@Override
			public void run() {

				byte[] AIDList = new byte[255];
				byte[] TagLists = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J' };
				// byte[] UserInfo = new byte[1024 * 1024];
				byte[] UserInfo = new byte[1024];
				byte[] icTrack2Data = new byte[1024];
				try {
					int nRet = m03pBank.SDSS_GetICCardInfo(0, AIDList,
							TagLists, UserInfo, icTrack2Data);
					// int nRet = m03pBank.SDSS_GetICCardInfo(0, AIDList,
					// TagLists, UserInfo);
					LogUtil.d(TAG, "SDSS_GetICInfo  nRet=" + nRet);
					// int nRet = -1;
					if (nRet == -1) {
						Looper.prepare();
						Tool.dismissDialog(dialog);
						Toast.makeText(ctx, "读卡失败", Toast.LENGTH_SHORT).show();
						Looper.loop();
					} else {
						String str = new String(UserInfo).trim();
						LogUtil.w(TAG, "卡信息   = " + str);
						int nIndex = str.indexOf('A');
						if (nIndex == -1) {
							Looper.prepare();
							Tool.dismissDialog(dialog);
							Toast.makeText(ctx, "读卡号错误", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} else {
							String substr = str.substring(nIndex + 1,
									nIndex + 4);
							int len = Integer.parseInt(substr);
							String cardno = str.substring(nIndex + 4, nIndex
									+ 4 + len);

							LogUtil.e(TAG, "读取IC卡号 len = " + len + " cardno = "
									+ cardno);
							if (cardno.length() > 0) {
								JSONObject o = null;
								o = new JSONObject();
								try {
									o.put("cardNumber", cardno);
									OpenCardApplication.setGlobal("cardNumber",
											cardno);
									webView.loadUrl("javascript:showICBankCardId("
											+ o + ")");
								} catch (JSONException e) {
									Looper.prepare();
									Tool.dismissDialog(dialog);
									Looper.loop();
									e.printStackTrace();
									LogUtil.e(TAG,
											"showAlertDialog JSONException: "
													+ e.getMessage());
								}
							} else {
								Looper.prepare();
								Toast.makeText(ctx, "IC卡卡号为空",
										Toast.LENGTH_SHORT).show();
								Tool.dismissDialog(dialog);
								Looper.loop();
							}
						}
						// m03pBank.SDSS_ICPowerOff(0x00);

						int eIndex = str.indexOf('E');
						if (eIndex == -1) {
							Looper.prepare();
							Tool.dismissDialog(dialog);
							Toast.makeText(ctx, "读取二磁信息错误", Toast.LENGTH_SHORT)
									.show();
							Looper.loop();
						} else {
							String substr = str.substring(eIndex + 1,
									eIndex + 4);
							int len = Integer.parseInt(substr);
							String flagTrompt31 = str.substring(eIndex + 4,
									eIndex + 4 + len * 2);
							LogUtil.e(TAG, "读取二磁信息 len = " + len
									+ " flagTrompt31 = " + flagTrompt31);
							flagTrompt31 = flagTrompt31.trim();
							if (flagTrompt31.length() > 0) {
								// 交易类型(980201-卡申请，980202-卡激活)
								String proCode = (String) OpenCardApplication
										.getGlobal("proCode");
								OpenCardApplication.setGlobal("flagTrompt31",
										flagTrompt31);
							}
						}

						// ------------------
						// TODO 读取IC卡序列号信息
						String tempStr = str;
						tempStr = tempStr.substring(str.indexOf('F'),
								tempStr.length());
						int jIndex = tempStr.indexOf('J') + str.indexOf('F');
						if (jIndex == -1) {
							Looper.prepare();
							Tool.dismissDialog(dialog);
							LogUtil.d(TAG, "读取IC卡序列号错误");
							Looper.loop();
						} else {
							String substr = str.substring(jIndex + 1,
									jIndex + 4);
							int len = Integer.parseInt(substr);
							String icSeqNo = str.substring(jIndex + 4, jIndex
									+ 4 + len);
							LogUtil.e(TAG, "读取IC卡序列号 len = " + len
									+ " flagTrompt31 = " + icSeqNo);
							if (icSeqNo.length() > 0) {
								OpenCardApplication.setGlobal("cardOrderNum",
										icSeqNo);
							}
						}
						mComShell.CloseDevice(2);

						try {
							Thread.sleep(200);
							// 读取ARQC信息
							getArqc(ctx, false);
						} catch (Exception e) {
							Tool.dismissDialog(dialog);
							LogUtil.e(TAG, "getArqc error: " + e.getMessage());
						}

					}
				} catch (Exception e) {
					LogUtil.e(TAG, e.getMessage());
				} finally {
					webView.loadUrl("javascript:response()");
					Looper.prepare();
					// m03pBank.SDSS_ICPowerOff(0x00);
					Tool.dismissDialog(dialog);
					Looper.loop();
				}
			}
		}).start();
	}

	// 读取卡号（读取ARQC之前，先读取卡号，此处卡号需要存储）
	private int readIcCardNumber() {
		int mRet = -1;
		byte[] AIDList = new byte[255];
		byte[] TagLists = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' };
		byte[] UserInfo = new byte[1024];
		byte[] icTrack2Data = new byte[1024];
		try {
			mRet = m03pBank.SDSS_GetICCardInfo(0, AIDList, TagLists, UserInfo,
					icTrack2Data);
			LogUtil.d(TAG, "readIcCardNumber mRet: " + mRet);
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return mRet;
	}

	/**
	 * 获取ARQC信息 isReadCardNumber true-需要读取IC卡号，false-不需要读取卡号
	 * 
	 * @return
	 */
	public String getArqc(Context ctx, boolean isReadCardNumber) {
		String arqc = "";
		// 先读取卡号
		int mRet = 0;
		// int mRet = 0;
		if (isReadCardNumber) {
			// mRet = readIcCardNumber();
		} else {
			mRet = 0;
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			LogUtil.e(TAG, e1.getMessage());
		}
		if (mRet == 0) { // 读卡信息成功，则继续读取ARQC信息，否则不读取ARQC信息
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

				String date = dateFormat.format(new Date());
				String time = timeFormat.format(new Date());
				String trData = "";
				String P = "P012000000000000"; // 授权金额
				String Q = "Q012000000000000"; // 其它金额
				String R = "R003156"; // 交易货币代码
				StringBuffer S = new StringBuffer("S008");// 交易日期
				String T = "T00217"; // 交易类型
				StringBuffer U = new StringBuffer("U006");// 交易时间
				String V = "V000"; // 版本号

				trData = Tool.packageTrData(P, Q, R, S.append(date).toString(),
						T, U.append(time).toString(), V);
				byte[] trDataBuff = new byte[1024];
				byte[] TagList = {};
				byte[] ARQC = new byte[2048];
				String cardNo = (String) OpenCardApplication
						.getGlobal("cardNumber");
				LogUtil.d(TAG, "获取ARQC cardNo = " + cardNo);
				// 将卡号传给外设调用接口（新加）
				cardNo = cardNo.length() + cardNo;
				LogUtil.d(TAG, "获取ARQC cardNo = " + cardNo);
				System.arraycopy(cardNo.getBytes(), 0, ARQC, 0,
						cardNo.getBytes().length);
				byte[] trAppData = new byte[1024];
				LogUtil.d(TAG, "获取ARQC trData = " + trData);
				trDataBuff = trData.getBytes();
				int nRet = m03pBank.SDSS_GetARQC(0, trDataBuff, TagList, ARQC,
						trAppData);

				if (nRet == -1) {
					LogUtil.d(TAG, "获取ARQC失败！");
				} else {
					String str = new String(ARQC);
					String trAppDataStr = new String(trAppData);

					OpenCardApplication.setGlobal("pBoc", str.trim());
					OpenCardApplication.setGlobal("trAppData",
							trAppDataStr.trim());
					OpenCardApplication.setGlobal("trData", trData);

					LogUtil.d(TAG, "ARQC is:" + str);
					LogUtil.d(TAG, "trAppData is: " + trAppDataStr);
					return str;
				}
			} catch (Exception e) {
				LogUtil.e(TAG, "getArqc error: " + e.getMessage());
			}
		} else {
			Looper.prepare();
			Toast.makeText(ctx, "ARQC读取错误，请重新读取卡信息", Toast.LENGTH_LONG).show();
			Looper.loop();
		}
		return arqc;
	}

	/**
	 * 读取磁条卡二三磁道信息
	 * 
	 *            提示信息
	 *            外设交易类型
	 */
	public void getMagneticCardInfo(Context ctx, WebView webView,
									Handler mHandler) {
		try {
			byte[] returnBuff = new byte[1280];
			byte[] wzinfo = new byte[256];
			int deviceId = 2;// 磁条卡
			int para = 4;// 读第二三磁道
			int[] returnBuffLen = new int[1];
			// 读二三磁道信息
			int nRet = mComShell.ReadInfo(deviceId, para, returnBuff,
					returnBuffLen);

			if (nRet == 0) {
				if (returnBuffLen[0] > 0) {// 01 00
					int len2 = returnBuff[0] * 256 + returnBuff[1];
					byte[] ctk2Data = new byte[1024];
					System.arraycopy(returnBuff, 2, ctk2Data, 0, len2);

					int len3 = returnBuff[len2 + 2] * 256
							+ returnBuff[len2 + 3];
					byte[] ctk3Data = new byte[1024];
					System.arraycopy(returnBuff, len2 + 4, ctk3Data, 0, len3);

					String str = "第二磁道数据：" + new String(ctk2Data)
							+ "\r\n第三磁道数据：" + new String(ctk3Data);
					String ctk2DataStr = new String(ctk2Data);
					String ctk3DataStr = new String(ctk3Data);

					// 读取卡号
					String tmpkh = "", kh = "";
					int index;
					tmpkh = ctk2DataStr;
					index = tmpkh.indexOf("=");
					if ((index > 0) && (index < tmpkh.length()))
						kh = tmpkh.substring(0, index);
					else {
						index = tmpkh.indexOf(">");
						if ((index > 0) && (index < tmpkh.length()))
							kh = tmpkh.substring(0, index);
					}
					JSONObject obj = null;
					obj = new JSONObject();
					try {
						obj.put("cardNumber", kh);
						// edtIcInfo.setText(str);
						OpenCardApplication.setGlobal("cardNumber", kh);
						webView.loadUrl("javascript:showBankCardId(" + obj
								+ ")");
					} catch (JSONException e) {
						e.printStackTrace();
						LogUtil.e(
								TAG,
								"showAlertDialog JSONException: "
										+ e.getMessage());
					}

					// edtIcInfo.setText(str);
				} else {
					// edtIcInfo.setText("第三磁道无数据");
					// Toast.makeText(ctx, "第三磁道无数据",
					// Toast.LENGTH_SHORT).show();
					LogUtil.d(TAG, "getMagneticCardInfo 1第三磁道明文无数据");
				}
				// m_soundSucess.start();
			} else {
				// edtIcInfo.setText("取三磁道信息失败");
				// Toast.makeText(ctx, "取三磁道信息失败", Toast.LENGTH_SHORT).show();
				LogUtil.d(TAG, "getMagneticCardInfo 2第三磁道明文无数据");
			}

			// 重新获取密文磁道信息
			nRet = mComShell.OpenDevice(2, 10000, mHandler);
			deviceId = 2;// 磁条卡
			para = 9;// 读第二三磁道（密文）
			// 读二三磁道信息
			nRet = mComShell
					.ReadInfo(deviceId, para, returnBuff, returnBuffLen);

			if (nRet == 0) {
				if (returnBuffLen[0] > 0) {// 01 00
					int len2 = returnBuff[0] * 256 + returnBuff[1];
					byte[] ctk2Data = new byte[len2];
					System.arraycopy(returnBuff, 2, ctk2Data, 0, len2);

					int len3 = returnBuff[len2 + 2] * 256
							+ returnBuff[len2 + 3];
					byte[] ctk3Data = new byte[len3];
					System.arraycopy(returnBuff, len2 + 4, ctk3Data, 0, len3);

					String str = "第二磁道数据：" + Tool.byte2hex(ctk2Data)
							+ "\r\n第三磁道数据：" + Tool.byte2hex(ctk3Data);

					String ctk2DataStr = Tool.byte2hex(ctk2Data);
					String ctk3DataStr = Tool.byte2hex(ctk3Data);
					OpenCardApplication.setGlobal("track2Data", ctk2DataStr); // 存储加密过后的第二磁道信息
					OpenCardApplication.setGlobal("track3Data", ctk3DataStr); // 存储加密过后的第三磁道信息

					// edtIcInfo.setText(str);
				} else {
					// edtIcInfo.setText("第三磁道无数据");
					Toast.makeText(ctx, "第三磁道无数据", Toast.LENGTH_SHORT).show();
					LogUtil.d(TAG, "getMagneticCardInfo 1第三磁道密文无数据");
				}
				// m_soundSucess.start();
			} else {
				// edtIcInfo.setText("取三磁道信息失败");
				Toast.makeText(ctx, "取三磁道信息失败", Toast.LENGTH_SHORT).show();
				LogUtil.d(TAG, "getMagneticCardInfo 2第三磁道密文无数据");
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "getMagneticCardInfo error: " + e.getMessage());
		} finally {
			mComShell.CloseDevice(2);
		}
	}
}
