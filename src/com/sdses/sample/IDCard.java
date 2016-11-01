package com.sdses.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sdses.peripheral.ComShell;

import org.apache.cordova.BuildConfig;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IDCard {

    private static Message msg;

    public static void readIDCard(ComShell mComShell,Handler mHandler) {
        byte[] returnBuff = new byte[1280];
        byte[] wzinfo = new byte[256];
        int deviceId = 1;
        int para = 1;
        int[] returnBuffLen = new int[1];
        if(mComShell==null){
            msg = mHandler.obtainMessage(-001);
            mHandler.sendMessage(msg);
            return;
        }
        mComShell.OpenDevice(1, 10000, mHandler);//打开二代证设备
        int nRet = mComShell.ReadInfo(deviceId, para, returnBuff, returnBuffLen);
        JSONObject jsonID = new JSONObject();
        try {
            if ((nRet == 0) && (returnBuffLen[0] > 0)) {
                //TODO
                System.arraycopy(returnBuff, 0, wzinfo, 0, 256);
                jsonID.put("name", mComShell.GetName(wzinfo));
                jsonID.put("gender", mComShell.GetGender(wzinfo));
                jsonID.put("nation", mComShell.GetNational(wzinfo));
                jsonID.put("birthday", mComShell.GetBirthday(wzinfo));
                jsonID.put("address", mComShell.GetAddress(wzinfo));
                jsonID.put("cardNum", mComShell.GetIndentityCard(wzinfo));
                jsonID.put("registInstitution", mComShell.GetIssued(wzinfo));
                jsonID.put("validStartDate", mComShell.GetStartDate(wzinfo));
                jsonID.put("validEndDate", mComShell.GetEndDate(wzinfo));
                String authFilePath = "/mnt/sdcard/";
                int nret = 0;
                try {
                    nret = mComShell.GetPic(authFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (nret > 0) {
                    Bitmap bm = BitmapFactory.decodeFile("/mnt/sdcard/" + "/zp.bmp");
                    String idPicPath = saveImage(bm);
                    jsonID.put("photoPath", idPicPath);
                } else {
                    msg = mHandler.obtainMessage(-102);
                    mHandler.sendMessage(msg);
                    return;
                }
                msg = mHandler.obtainMessage(00,jsonID);
                mHandler.sendMessage(msg);
            } else {
                msg = mHandler.obtainMessage(-101);
                mHandler.sendMessage(msg);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 保存图片
     */
    public static String saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "IDCard");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
