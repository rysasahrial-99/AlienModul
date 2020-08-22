package com.aliendroid.latihanjson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import static com.aliendroid.latihanjson.Config.LINK;
import static com.aliendroid.latihanjson.Config.PENGATURAN_IKLAN;
import static com.aliendroid.latihanjson.Config.STATUS;
import static com.aliendroid.latihanjson.Config.STRATAPPID;
import static com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE;

public class MainActivity extends AppCompatActivity {
    /*
    Intertitial Ads
     */
    private InterstitialAd mInterstitialAd;
    public static com.facebook.ads.InterstitialAd interstitialAdfb;

   /*
   GDRP
    */
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;

    /*
    TAG Firebase
     */
    private static final String TAG = "MyFirebaseMsgService";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Konfigurasi Redirect App
         */
        if (STATUS.equals("1")) {

            String str = LINK;
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(str)));
            finish();
        }

        /*
        Pilihan iklan dari Radio Button
         */
        RadioGroup list_action = findViewById(R.id.rdiklan);
        list_action.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id){
                    case R.id.rdAdmob:
                        PENGATURAN_IKLAN = "1";
                        break;
                    case R.id.rdfan:
                        PENGATURAN_IKLAN = "2";
                        break;
                    case R.id.rdstart:
                        PENGATURAN_IKLAN = "3";
                        StartAppSDK.init(MainActivity.this, STRATAPPID, true);
                        StartAppAd.disableSplash();
                        break;

                }
            }
        });



        /*
        Firebase
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
               // Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                         //   Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        /*
        MobileAds.initialize Admob
         */
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        /*
        GDPR ADMOB
         */

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings
                        .DebugGeography
                        .DEBUG_GEOGRAPHY_NOT_EEA)
                .addTestDeviceHashedId("TEST-DEVICE-HASHED-ID")
                .build();

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {
                        // The consent information state was updated.
                        // You are now ready to check if a form is available.
                        if (consentInformation.isConsentFormAvailable()) {
                            loadForm();
                        }
                    }
                },
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(FormError formError) {
                        // Handle the error.
                    }
                });


        /*
        Targeting iklan untuk anak kecil, rating dll. Silahkan ubah sesuai panduan admob dan app
         */
        RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        /*
        Intertitial Admob
         */
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Config.ADMOB_INTER);
        mInterstitialAd.loadAd(new AdRequest.Builder().addKeyword("asuransi").addKeyword("forex").build());

        /*
        Intertitial FAN
         */
        interstitialAdfb = new com.facebook.ads.InterstitialAd(MainActivity.this,  Config.FAN_INTER);
        interstitialAdfb.loadAd();


        /*
        Tombol memunculkan iklan inter, dan switch iklan diatur dr json atau radiobutton untuk contoh
         */
        Button munculinter = findViewById(R.id.btninter);
        munculinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.PENGATURAN_IKLAN.equals("1")){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                } else if (Config.PENGATURAN_IKLAN.equals("2")){
                    if (interstitialAdfb == null || !interstitialAdfb.isAdLoaded()) {
                        interstitialAdfb.loadAd();
                    } else {
                        interstitialAdfb.show();

                    }
                } else if (PENGATURAN_IKLAN.equals("3")) {
                    StartAppAd.showAd(MainActivity.this);
                }
            }
        });


          /*
        Tombol memunculkan banner
         */
        Button muncubanner = findViewById(R.id.btbanner);
        muncubanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), BannerActivity.class);
                startActivity(intent);
            }
        });

           /*
        Tombol memunculkan banner
         */
        Button btnative = findViewById(R.id.btnative);
        btnative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               exitapp();
            }
        });

        Button btreward= findViewById(R.id.btreward);
        btreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitapp();
            }
        });


    }

    /*
    Load GDPR
     */
    public void loadForm(){
        UserMessagingPlatform.loadConsentForm(
                this,
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                    @Override
                    public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                        MainActivity.this.consentForm = consentForm;
                        if(consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                            consentForm.show(
                                    MainActivity.this,
                                    new ConsentForm.OnConsentFormDismissedListener() {
                                        @Override
                                        public void onConsentFormDismissed(@Nullable FormError formError) {
                                            // Handle dismissal by reloading form.
                                            loadForm();
                                        }
                                    });
                        }

                    }
                },
                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                    @Override
                    public void onConsentFormLoadFailure(FormError formError) {
                        /// Handle Error.
                    }
                }
        );
    }

    void gotoUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
    private void exitapp() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_baseline_add_alarm_24);
        builder.setTitle("Info Sc");
        builder.setMessage("Tunggu Update Source code, pantau terus kami di fanpage dan chanel Aliendroid");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Fanpage",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                gotoUrl("https://www.facebook.com/aliendroidstudio/");
                dialog.dismiss();


            }
        });

        builder.setNegativeButton("Youtube",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                gotoUrl("https://www.youtube.com/channel/UCuelwr-6eUtae6KGZqVpiMg");
                dialog.dismiss();


            }
        });

        builder.setNeutralButton("Close",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

}