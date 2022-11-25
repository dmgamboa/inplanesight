package com.inplanesight.ui.find;

import android.graphics.Bitmap;

public class ViewPagerItem {
    Bitmap imageMap;

    public ViewPagerItem(Bitmap imageMap) {
        this.imageMap = imageMap;
    }

    public Bitmap getImage() {
        return imageMap;
    }

    public void setImage(Bitmap imageMap) {
        this.imageMap = imageMap;
    }
}