package com.zsmarter.watermark;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.zsmarter.utils.BitmapUtil;
import com.zsmarter.utils.LogUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.zsmarter.watermark.WaterMarkConfig.LEFT_BOTTOM;
import static com.zsmarter.watermark.WaterMarkConfig.LEFT_TOP;
import static com.zsmarter.watermark.WaterMarkConfig.MARK_ALL;
import static com.zsmarter.watermark.WaterMarkConfig.MARK_PHOTO;
import static com.zsmarter.watermark.WaterMarkConfig.MARK_STRING;
import static com.zsmarter.watermark.WaterMarkConfig.MIDDLE;
import static com.zsmarter.watermark.WaterMarkConfig.RIGHT_BOTTOM;
import static com.zsmarter.watermark.WaterMarkConfig.RIGHT_TOP;


/**
 * Created by wangxf on 2016/10/28.
 */

public class WaterMark extends CordovaPlugin {

    private Activity mContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(cordova.getClass().getName(), "initialize..");
        mContext = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject res = new JSONObject();
        try {
        JSONObject jsonObject = args.getJSONObject(0);
        WaterMarkOption option = new WaterMarkOption();
        option.setType(jsonObject.optString("type"));
        option.setPostion(jsonObject.optString("postion"));
        option.setPenColor(jsonObject.optString("penColor"));
        option.setStringMarkText(jsonObject.optString("stringMarkText"));
        option.setSourcePhotoPath(jsonObject.optString("MarkPhotoPath"));
        option.setMarkPhotoPath(jsonObject.optString("markPhotoPath"));
        option.setMarkPhotoBase64(jsonObject.optString("markPhotoBase64"));
        option.setSourcePhotoBase64(jsonObject.optString("sourcePhotoBase64"));
        option.setInSampleSize(jsonObject.optInt("inSampleSize"));
        option.setStringMarkSize(jsonObject.optInt("stringMarkSize"));
        option.setAlpha(jsonObject.optInt("alpha"));
        option.setLeftStringPadding(jsonObject.optInt("leftStringPadding"));
        option.setRightStringPadding(jsonObject.optInt("rightStringPadding"));
        option.setBottomStringPadding(jsonObject.optInt("bottomStringPadding"));
        option.setTopStringPadding(jsonObject.optInt("topStringPadding"));
        option.setLeftPhotoPadding(jsonObject.optInt("leftPhotoPadding"));
        option.setRightPhotoPadding(jsonObject.optInt("rightPhotoPadding"));
        option.setBottomPhotoPadding(jsonObject.optInt("bottomPhotoPadding"));
        option.setTopPhotoPadding(jsonObject.optInt("topPhotoPadding"));
        String base64Photo = createWaterMark(option);
        res.put("base64Photo", base64Photo);
        res.put("code", "00");
        callbackContext.success(res);
        }catch (Exception e){
            res.put("code", "01");
            res.put("message",e.toString());
            callbackContext.error(res);
        }
        return true;
    }

    private String createWaterMark(WaterMarkOption option) throws JSONException {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = option.getInSampleSize();
        if (option.getSourcePhotoBase64() != null && option.getSourcePhotoBase64() != "") {
            bitmap = BitmapUtil.base64ToBitmap(option.getSourcePhotoBase64(), options);
            option.setSourceBitmap(bitmap);
            LogUtils.d(bitmap.getWidth()+"+"+bitmap.getHeight());
        }
        if (option.getSourcePhotoPath() != null && option.getSourcePhotoPath() != "") {
            bitmap = BitmapFactory.decodeFile(option.getSourcePhotoPath(), options);
            option.setSourceBitmap(bitmap);
        }
        switch (option.getType()) {
            case MARK_STRING:
                bitmap = createStringWaterMark(option);
                break;
            case MARK_PHOTO:
                bitmap = createPhotoWaterMark(option);
                break;
            case MARK_ALL:
                bitmap = createStringWaterMark(option);
                option.setSourcePhotoBase64(BitmapUtil.bitmapToBase64(bitmap));
                bitmap = createPhotoWaterMark(option);
                break;
            default:
                    option.setStringMarkSize(bitmap.getWidth()/25);
                    option.setRightStringPadding(bitmap.getWidth()/25);
                    option.setBottomStringPadding(bitmap.getWidth()/25);
                bitmap = createStringWaterMark(option);
                break;
        }
        return BitmapUtil.bitmapToBase64(bitmap);
    }

    private Bitmap createStringWaterMark(WaterMarkOption option) throws JSONException {
        Bitmap bitmap = option.getSourceBitmap();
        switch (option.getPostion()) {
            case LEFT_TOP:
                bitmap = ImageUtil.drawTextToLeftTop(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getLeftStringPadding(), option.getTopStringPadding(), option.getAlpha());
                break;
            case RIGHT_TOP:
                bitmap = ImageUtil.drawTextToRightTop(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getRightStringPadding(), option.getTopStringPadding(), option.getAlpha());
                break;
            case LEFT_BOTTOM:
                bitmap = ImageUtil.drawTextToLeftBottom(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getLeftStringPadding(), option.getBottomStringPadding(), option.getAlpha());
                break;
            case RIGHT_BOTTOM:
                bitmap = ImageUtil.drawTextToRightBottom(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getRightStringPadding(), option.getBottomStringPadding(), option.getAlpha());
                break;
            case MIDDLE:
                bitmap = ImageUtil.drawTextToCenter(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getAlpha());
                break;
            default:
                bitmap = ImageUtil.drawTextToRightBottom(mContext, bitmap, option.getStringMarkText(), option.getStringMarkSize(), option.getPenColor(), option.getRightStringPadding(), option.getBottomStringPadding(), option.getAlpha());
                break;
        }
        return bitmap;
    }

    private Bitmap createPhotoWaterMark(WaterMarkOption option) throws JSONException {
        Bitmap bitmap = null;
        Bitmap markPhoto = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = option.getInSampleSize();
        if (option.getSourcePhotoBase64() != null && option.getSourcePhotoBase64() != "") {
            bitmap = BitmapUtil.base64ToBitmap(option.getSourcePhotoBase64(), options);
        }
        if (option.getSourcePhotoPath() != null && option.getSourcePhotoPath() != "") {
            bitmap = BitmapFactory.decodeFile(option.getSourcePhotoPath(), options);
        }
        if (option.getMarkPhotoBase64() != null && option.getMarkPhotoBase64() != "") {
            markPhoto = BitmapUtil.base64ToBitmap(option.getMarkPhotoBase64(), options);
        }
        if (option.getMarkPhotoPath() != null && option.getMarkPhotoPath() != "") {
            markPhoto = BitmapFactory.decodeFile(option.getMarkPhotoPath(),options);
//            markPhoto = BitmapUtil.getImageFromAssetsFile(mContext,"www/img/logo.png");
        }
        switch (option.getPostion()) {
            case LEFT_TOP:
                bitmap = ImageUtil.createWaterMaskLeftTop(mContext, bitmap, markPhoto, option.getLeftPhotoPadding(), option.getTopPhotoPadding(), option.getAlpha());
                break;
            case RIGHT_TOP:
                bitmap = ImageUtil.createWaterMaskRightTop(mContext, bitmap, markPhoto, option.getRightPhotoPadding(), option.getTopPhotoPadding(), option.getAlpha());
                break;
            case LEFT_BOTTOM:
                bitmap = ImageUtil.createWaterMaskLeftBottom(mContext, bitmap, markPhoto, option.getLeftPhotoPadding(), option.getBottomPhotoPadding(), option.getAlpha());
                break;
            case RIGHT_BOTTOM:
                bitmap = ImageUtil.createWaterMaskRightBottom(mContext, bitmap, markPhoto, option.getRightPhotoPadding(), option.getBottomPhotoPadding(), option.getAlpha());
                break;
            case MIDDLE:
                bitmap = ImageUtil.createWaterMaskCenter(bitmap, markPhoto, option.getAlpha());
                break;
            default:
                bitmap = ImageUtil.createWaterMaskRightBottom(mContext, bitmap, markPhoto, option.getRightPhotoPadding(), option.getBottomPhotoPadding(), option.getAlpha());
                break;
        }
        return bitmap;
    }
}
