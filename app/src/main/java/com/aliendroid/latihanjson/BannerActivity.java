package com.aliendroid.latihanjson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.Mrec;

import static com.aliendroid.latihanjson.Config.ADMOB_BANNER;
import static com.aliendroid.latihanjson.Config.FAN_BANNER;
import static com.aliendroid.latihanjson.Config.FAN_BANNER_BIG;
import static com.aliendroid.latihanjson.Config.PENGATURAN_IKLAN;

public class BannerActivity extends AppCompatActivity {

    /*
    variabel BANNER FAN
     */
    private com.facebook.ads.AdView bannerAdView;
    private RelativeLayout bannerAdContainer;

    /*
    variable BANNER Admob
     */
    private FrameLayout adContainerView;
    private AdView adView;

    /*
    Big banner untuk Admob, fan dan startapp
     */
    private RelativeLayout bannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);


        bannerLayout = (RelativeLayout) findViewById(R.id.bigbanner);
        bannerAdContainer = (RelativeLayout)findViewById(R.id.banner_container);
        adContainerView = findViewById(R.id.ad_view_container);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(ADMOB_BANNER);
        adContainerView.addView(adView);

        if (PENGATURAN_IKLAN.equals("1")){
            loadBanner();
            BannerAdmobMedium();
        }  else if (PENGATURAN_IKLAN.equals("2")){
            bannerfan();
            bannerfanBesar();
        } else if (PENGATURAN_IKLAN.equals("3")){
            /*
             Konfigurasi Banner StartApp
            */

            Banner startAppBanner = new Banner(BannerActivity.this);
            RelativeLayout.LayoutParams bannerParameters =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
            bannerAdContainer.addView(startAppBanner, bannerParameters);





            Mrec startAppMrec = new Mrec(this);
            RelativeLayout.LayoutParams mrecParameters =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            mrecParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);


// Add to main Layout
            bannerLayout.addView(startAppMrec, mrecParameters);
        }

    }
    /*
    Konfigurasi Banner Admob
    */
    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder() .build();

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    /*
    Konfigurasi Banner FAN
     */
    private void bannerfan(){
        bannerAdView= new com.facebook.ads.AdView(this, FAN_BANNER, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        // Add the ad view to your activity layout
        bannerAdContainer.addView(bannerAdView);
        // Request an ad
        bannerAdView.loadAd();
    }

    /*
  Konfigurasi Banner FAN
   */
    private void bannerfanBesar(){

        bannerAdView= new com.facebook.ads.AdView(this, FAN_BANNER_BIG, com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        // Add the ad view to your activity layout
        bannerLayout.addView(bannerAdView);
        // Request an ad
        bannerAdView.loadAd();
    }




    /*
        Kongigurasi Admob Kotak/besar
         */
    private void BannerAdmobMedium() {
        adView = new AdView(BannerActivity.this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(ADMOB_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {


            }

            @Override
            public void onAdFailedToLoad(int errorCode) {


                bannerLayout.setVisibility(View.GONE);
            }

        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bannerLayout.addView(adView, params);
    }




    public void onBackPressed() {

      finish();
    }

}