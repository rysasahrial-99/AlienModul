package com.aliendroid.latihanjson;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.AudienceNetworkAds;

/**
 * Sample class that shows how to call initialize() method of Audience Network SDK.
 */
public class AudienceNetworkInitializeHelper
        implements AudienceNetworkAds.InitListener {

    /**
     * It's recommended to call this method from Application.onCreate().
     * Otherwise you can call it from all Activity.onCreate()
     * methods for Activities that contain ads.
     *
     * @param context Application or Activity.
     */
    public static void initialize(Context context) {
        AudienceNetworkAds
                .buildInitSettings(context)
                .withInitListener(new AudienceNetworkInitializeHelper())
                .initialize();
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }
}
