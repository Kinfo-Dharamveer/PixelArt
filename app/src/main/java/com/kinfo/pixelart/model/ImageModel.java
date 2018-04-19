package com.kinfo.pixelart.model;

/**
 * Created by kinfo on 4/17/2018.
 */

public class ImageModel {

    private int image;
    private int ad_status;
    private int subscription_status;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getAd_status() {
        return ad_status;
    }

    public void setAd_status(int ad_status) {
        this.ad_status = ad_status;
    }

    public int getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(int subscription_status) {
        this.subscription_status = subscription_status;
    }
}
