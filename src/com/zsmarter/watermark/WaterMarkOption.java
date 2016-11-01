package com.zsmarter.watermark;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * @project android
 * Created by wangxf on 2016/10/31
 */

public class WaterMarkOption {
    String type;
    String postion;
    String stringMarkText;
    String sourcePhotoPath;
    String markPhotoPath;
    String sourcePhotoBase64;
    String markPhotoBase64;
    int penColor;
    int inSampleSize;
    int stringMarkSize;
    int alpha;
    int leftStringPadding;
    int rightStringPadding;
    int bottomStringPadding;
    int topStringPadding;
    int leftPhotoPadding;
    int rightPhotoPadding;
    int bottomPhotoPadding;
    int topPhotoPadding;
    Bitmap sourceBitmap;
    Bitmap markBitmap;

    public Bitmap getSourceBitmap() {
        return sourceBitmap;
    }

    public void setSourceBitmap(Bitmap sourceBitmap) {
        this.sourceBitmap = sourceBitmap;
    }

    public Bitmap getMarkBitmap() {
        return markBitmap;
    }

    public void setMarkBitmap(Bitmap markBitmap) {
        this.markBitmap = markBitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = postion;
    }

    public int getPenColor() {
        return penColor;
    }

    public void setPenColor(String penColor) {
        if(penColor==null||penColor==""){
            this.penColor = Color.BLACK;
        }else{
            this.penColor = Color.parseColor(penColor);
        }
    }

    public String getStringMarkText() {
        return stringMarkText;
    }

    public void setStringMarkText(String stringMarkText) {
        this.stringMarkText = stringMarkText;
    }

    public String getSourcePhotoPath() {
        return sourcePhotoPath;
    }

    public void setSourcePhotoPath(String sourcePhotoPath) {
        this.sourcePhotoPath = sourcePhotoPath;
    }

    public String getMarkPhotoPath() {
        return markPhotoPath;
    }

    public void setMarkPhotoPath(String markPhotoPath) {
        this.markPhotoPath = markPhotoPath;
    }

    public int getInSampleSize() {
        return inSampleSize;
    }

    public void setInSampleSize(int inSampleSize) {
        this.inSampleSize = inSampleSize;
    }

    public int getStringMarkSize() {
        return stringMarkSize;
    }

    public void setStringMarkSize(int stringMarkSize) {
        this.stringMarkSize = stringMarkSize;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        if(alpha==0){
            alpha = 255;
        }
        this.alpha = alpha;
    }

    public int getLeftStringPadding() {
        return leftStringPadding;
    }

    public void setLeftStringPadding(int leftPadding) {
        this.leftStringPadding = leftPadding;
    }

    public int getRightStringPadding() {
        return rightStringPadding;
    }

    public void setRightStringPadding(int rightPadding) {
        this.rightStringPadding = rightPadding;
    }

    public int getBottomStringPadding() {
        return bottomStringPadding;
    }

    public void setBottomStringPadding(int bottomPadding) {
        this.bottomStringPadding = bottomPadding;
    }

    public int getTopStringPadding() {
        return topStringPadding;
    }

    public void setTopStringPadding(int topPadding) {
        this.topStringPadding = topPadding;
    }

    public int getLeftPhotoPadding() {
        return leftPhotoPadding;
    }

    public void setLeftPhotoPadding(int leftPhotoPadding) {
        this.leftPhotoPadding = leftPhotoPadding;
    }

    public int getRightPhotoPadding() {
        return rightPhotoPadding;
    }

    public void setRightPhotoPadding(int rightPhotoPadding) {
        this.rightPhotoPadding = rightPhotoPadding;
    }

    public int getBottomPhotoPadding() {
        return bottomPhotoPadding;
    }

    public void setBottomPhotoPadding(int bottomPhotoPadding) {
        this.bottomPhotoPadding = bottomPhotoPadding;
    }

    public int getTopPhotoPadding() {
        return topPhotoPadding;
    }

    public void setTopPhotoPadding(int topPhotoPadding) {
        this.topPhotoPadding = topPhotoPadding;
    }

    public String getSourcePhotoBase64() {
        return sourcePhotoBase64;
    }

    public void setSourcePhotoBase64(String sourcePhotoBase64) {
        this.sourcePhotoBase64 = sourcePhotoBase64;
    }

    public String getMarkPhotoBase64() {
        return markPhotoBase64;
    }

    public void setMarkPhotoBase64(String markPhotoBase64) {
        this.markPhotoBase64 = markPhotoBase64;
    }

    @Override
    public String toString() {
        return "WaterMarkOption{" +
                "type='" + type + '\'' +
                ", postion='" + postion + '\'' +
                ", stringMarkText='" + stringMarkText + '\'' +
                ", sourcePhotoPath='" + sourcePhotoPath + '\'' +
                ", markPhotoPath='" + markPhotoPath + '\'' +
                ", penColor=" + penColor +
                ", inSampleSize=" + inSampleSize +
                ", stringMarkSize=" + stringMarkSize +
                ", alpha=" + alpha +
                ", leftStringPadding=" + leftStringPadding +
                ", rightStringPadding=" + rightStringPadding +
                ", bottomStringPadding=" + bottomStringPadding +
                ", topStringPadding=" + topStringPadding +
                ", leftPhotoPadding=" + leftPhotoPadding +
                ", rightPhotoPadding=" + rightPhotoPadding +
                ", bottomPhotoPadding=" + bottomPhotoPadding +
                ", topPhotoPadding=" + topPhotoPadding +
                ", sourcePhotoBase64=" + sourcePhotoBase64 +
                ", markPhotoBase64=" + markPhotoBase64 +
                '}';
    }
}
