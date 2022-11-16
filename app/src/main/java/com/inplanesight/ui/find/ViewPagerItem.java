package com.inplanesight.ui.find;

public class ViewPagerItem {
    String imageUrl;

    public ViewPagerItem(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}