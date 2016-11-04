package com.sdses.sample;

import android.os.Handler;
import android.os.Message;
import com.sdses.peripheral.ComShell;
import com.sdses.peripheral.M03PForBank;


public class ICCard {

    private static final String TAG = "M3";
    private static Handler icHandler;
    private static Message msg;

    /**
     * 读取ic芯片卡号
     */
    protected static void readICCardNum(Handler handler) {
        icHandler = handler;
        new Thread(new ReadCardThread()).start();
    }

    /**
     * 初始化MagCardModule
     */
    protected static int initMagCardModule(ComShell mComShell, Handler mHandler) {
        if(mComShell==null){
            return -1;
        }
        return mComShell.OpenDevice(2, 10000, mHandler);
    }

    /**
     * 读取磁条卡
     */
    protected static void readMagCardNo(ComShell mComShell, Handler mHandler) {
        byte[] returnBuff = new byte[1280];
        byte[] wzinfo = new byte[256];
        int deviceId = 2;//磁条卡
        int para = 4;//读第二磁道
        int[] returnBuffLen = new int[3];
        if(mComShell==null){
            msg = mHandler.obtainMessage(-111);
            mHandler.sendMessage(msg);
            return;
        }
        int nRet = mComShell.ReadInfo(deviceId, para, returnBuff, returnBuffLen);
        if (nRet == 0) {
            if (returnBuffLen[0] > 0) {
                String tmpkh = "", magCardNo = "";
                tmpkh = new String(returnBuff);
                int index;
                index = tmpkh.indexOf("=");

                if ((index > 0) && (index < tmpkh.length()))
                    magCardNo = tmpkh.substring(0, index);
                else {
                    index = tmpkh.indexOf(">");
                    if ((index > 0) && (index < tmpkh.length()))
                        magCardNo = tmpkh.substring(0, index);
                }
                magCardNo = tmpkh.substring(2, index);
                if (magCardNo.length() > 0) {
                    msg = mHandler.obtainMessage(01, magCardNo);
                    mHandler.sendMessage(msg);
                } else {
                    msg = mHandler.obtainMessage(-141);
                    mHandler.sendMessage(msg);
                }
            } else {
                msg = mHandler.obtainMessage(-141);
                mHandler.sendMessage(msg);
            }
        } else {
            msg = mHandler.obtainMessage(-141);
            mHandler.sendMessage(msg);
        }

    }

    private static class ReadCardThread implements Runnable {
        public ReadCardThread() {
        }

        public void run() {
            byte[] AIDList = new byte[255];
            byte[] TagList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
            byte[] UserInfo = new byte[1024];
            byte[] icTrack2Data = new byte[1024];
            M03PForBank m03pBank = new M03PForBank();
            if (m03pBank == null) {
                msg = icHandler.obtainMessage(-13);
                icHandler.sendMessage(msg);
                return;
            }
            int nRet = m03pBank.SDSS_GetICCardInfo(0x00, AIDList, TagList, UserInfo, icTrack2Data);
            if (nRet == -1) {
                msg = icHandler.obtainMessage(-131);
                icHandler.sendMessage(msg);
            } else {
                String str = new String(UserInfo);
                // 卡号
                int nIndex = str.indexOf('A');
                if (nIndex == -1) {
                    msg = icHandler.obtainMessage(-2, null);
                    icHandler.sendMessage(msg);
                } else {
                    String substr = str.substring(nIndex + 1, nIndex + 4);
                    int len = Integer.parseInt(substr);
                    String cardno = str.substring(nIndex + 4, nIndex + 4 + len);
                    if (cardno.length() > 0) {
                        msg = icHandler.obtainMessage(02, cardno);
                        icHandler.sendMessage(msg);
                    } else {
                        msg = icHandler.obtainMessage(-131);
                        icHandler.sendMessage(msg);
                    }
                }
            }
        }
    }

}
