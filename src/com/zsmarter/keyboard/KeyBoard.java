package com.zsmarter.keyboard;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.Call;
import android.util.Log;

import com.zsmarter.utils.BitmapUtil;
import com.zsmarter.utils.LogUtils;
import com.zsmarter.watermark.ImageUtil;
import com.zsmarter.watermark.WaterMarkOption;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by wangxf on 2016/10/28.
 */

public class KeyBoard extends CordovaPlugin {

    private Activity mContext;
    private CallbackContext mCallBack;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(cordova.getClass().getName(), "initialize..");
        mContext = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallBack = callbackContext;
        JSONObject jsonObject = args.getJSONObject(0);
        String type = jsonObject.optString("type");
        switch (action){
        case "showKeyBoard":
            showKeyBoard(type);
            break;
        }
        return true;
    }

    private void showKeyBoard(String type) {
        Intent intent = new Intent(mContext,KeyBoardActivity.class);
        intent.putExtra("type",type);
        cordova.startActivityForResult(this,intent,00);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode==200){
            String pass = intent.getStringExtra("encodePassword");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("encodePassword",pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.mCallBack.success(jsonObject);
        }
    }
}
