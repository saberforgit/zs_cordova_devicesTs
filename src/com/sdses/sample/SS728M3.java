package com.sdses.sample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sdses.fpapplication.SdsesBacnCard;
import com.sdses.peripheral.ComShell;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SS728M3 extends CordovaPlugin {
    private static ComShell mComShell;
    private static CallbackContext mCallBack;
    private static final String TAG = "M3";
    private Activity mContext;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 00:
                        JSONObject IDJson = (JSONObject) msg.obj;
                        mCallBack.success(IDJson);
                        closeM3CardModule(1);
                        Log.d("ID", IDJson.toString());
                        break;
                    case 01:
                        String MagCardNo = (String) msg.obj;
                        JSONObject magCardJson = new JSONObject();
                        magCardJson.put("magno", MagCardNo);
                        Log.d("Mag卡号", magCardJson.toString());
                        mCallBack.success(magCardJson);
                        closeM3CardModule(2);

                        break;
                    case 02:
                        String ICCardNo = (String) msg.obj;
                        JSONObject ICCardJson = new JSONObject();
                        ICCardJson.put("icno", ICCardNo);
                        Log.d("IC卡号", ICCardJson.toString());
                        mCallBack.success(ICCardJson);

                        break;
                    case 101:
                        JSONObject errorID00 = new JSONObject();
                        errorID00.put("code", msg.what);
                        errorID00.put("message", "IDCardModule is not init");
                        mCallBack.error(errorID00);
                        Log.d("ID", "IDCardModule is not init");
                        break;
                    case -111:
                        JSONObject errorM3 = new JSONObject();
                        errorM3.put("code", msg.what);
                        errorM3.put("message", "bluetooth is not connect");
                        mCallBack.error(errorM3);
                        Log.d("M3", "bluetooth is not connect");
                        break;
                    case -112:
                        JSONObject errorM301 = new JSONObject();
                        errorM301.put("code", msg.what);
                        errorM301.put("message", "bluetooth is not connect");
                        mCallBack.error(errorM301);
                        Log.d("M3", "bluetooth is not connect");
                        break;
                    case -113:
                        JSONObject errorM302 = new JSONObject();
                        errorM302.put("code", msg.what);
                        errorM302.put("message", "bluetooth is not open");
                        mCallBack.error(errorM302);
                        Log.d("M3", "bluetooth is not open");
                        break;
                    case -101:
                        JSONObject errorID01 = new JSONObject();
                        errorID01.put("code", msg.what);
                        errorID01.put("message", "read IDCard error");
                        mCallBack.error(errorID01);
                        closeM3CardModule(1);
                        Log.d("IC卡号", "read IDCard error");
                        break;
                    case -102:
                        JSONObject errorID02 = new JSONObject();
                        errorID02.put("code", msg.what);
                        errorID02.put("message", "get IDPic error");
                        mCallBack.error(errorID02);
                        closeM3CardModule(1);
                        Log.d("IC卡号", "get IDPic error");
                        break;
                    case -13:
                        JSONObject errorIC01 = new JSONObject();
                        errorIC01.put("code", msg.what);
                        errorIC01.put("message", "ICCardModule is not init");
                        mCallBack.error(errorIC01);
                        Log.d("IC", "ICCardModule is not init");
                        break;
                    case -14:
                        JSONObject errorMag01 = new JSONObject();
                        errorMag01.put("code", msg.what);
                        errorMag01.put("message", "read IDCard error");
                        mCallBack.error(errorMag01);
                        Log.d("Mag", "MagCardModule is not init");
                        break;
                    case -131:
                        JSONObject errorIC02 = new JSONObject();
                        errorIC02.put("code", msg.what);
                        errorIC02.put("message", "read ICCard error");
                        mCallBack.error(errorIC02);
                        closeM3CardModule(2);
                        Log.d("IC", "read ICCard error");
                        break;
                    case -141:
                        JSONObject errorMag02 = new JSONObject();
                        errorMag02.put("code", msg.what);
                        errorMag02.put("message", "read MagCard error");
                        mCallBack.error(errorMag02);
                        closeM3CardModule(2);
                        Log.d("Mag", "read MagCard error");
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(cordova.getClass().getName(), "initialize..");
        mContext = cordova.getActivity();

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallBack = callbackContext;
        switch (action) {
            case "readIDCard":
                if (!getBluetoothStatus()) {
                    Message msg = handler.obtainMessage(-113);
                    handler.sendMessage(msg);
                    return true;
                }
                int res = initM3Client(mContext);
                if (res == -1) {
                    Message msg = handler.obtainMessage(-112);
                    handler.sendMessage(msg);
                    return true;
                }
                if (res == -2) {
                    Message msg = handler.obtainMessage(-111);
                    handler.sendMessage(msg);
                    return true;
                }
                IDCard.readIDCard(mComShell, handler);
                break;
            case "readICCardNum":
                if (!getBluetoothStatus()) {
                    Message msg = handler.obtainMessage(-113);
                    handler.sendMessage(msg);
                    return true;
                }
                int res01 = initM3Client(mContext);
                if (res01 == -1) {
                    Message msg = handler.obtainMessage(-112);
                    handler.sendMessage(msg);
                    return true;
                }
                if (res01 == -2) {
                    Message msg = handler.obtainMessage(-111);
                    handler.sendMessage(msg);
                    return true;
                }
                try {
                    ICCard.readICCardNum(handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "initMagClient":
                if (!getBluetoothStatus()) {
                    Message msg = handler.obtainMessage(-113);
                    handler.sendMessage(msg);
                    return true;
                }
                int res02 = initM3Client(mContext);
                if (res02 == -1) {
                    Message msg = handler.obtainMessage(-112);
                    handler.sendMessage(msg);
                    return true;
                }
                if (res02 == -2) {
                    Message msg = handler.obtainMessage(-111);
                    handler.sendMessage(msg);
                    return true;
                }
                Log.d("M3", "设备开启成功");
                if (ICCard.initMagCardModule(mComShell, handler) == -1) {
                    Log.d("Mag", "MagCardModule init error");
                    return true;
                }
                break;
            case "readMagCardNo":
                if (!getBluetoothStatus()) {
                    Message msg = handler.obtainMessage(-113);
                    handler.sendMessage(msg);
                    return true;
                }
                if (!mComShell.bShellOk) {
                    Message msg = handler.obtainMessage(-111);
                    handler.sendMessage(msg);
                    return true;
                }
                ICCard.readMagCardNo(mComShell, handler);
                break;
        }

        return true;
    }


    protected static int initM3Client(Activity mContext) {
        mComShell = SdsesBacnCard.getComShell(mContext);
        if (mComShell == null) {
            return -1;
        } else {
            if (!mComShell.bShellOk) {
                return -2;
            }
            return 0;
        }
    }

    /**
     * 关闭读卡设备
     */
    protected static int closeM3CardModule(int type) {
        int res = mComShell.CloseDevice(type);
        mComShell = null;
        return res;
    }

    /**
     * 检查蓝牙是否打开
     *
     * @return
     */
    protected static boolean getBluetoothStatus() {
        BluetoothAdapter btAdapt = BluetoothAdapter.getDefaultAdapter();
        if (btAdapt == null) {
            return false;
        }
        if (btAdapt.getState() != BluetoothAdapter.STATE_ON) {
            return false;
        }
        return true;
    }
}
