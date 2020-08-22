package com.aliendroid.latihanjson;

public class Config {

    /*
    Ubah ON_OFF_JSON = "0"; menjadi ON_OFF_JSON = "1";
    jika menggunakan iklan online
     */
    public static String ON_OFF_JSON = "0";
    public static  String LINK_Json="http://hexa.web.id/latihan_json.json";

    /*
    PENGATURAN_IKLAN="1" untuk Admob
    PENGATURAN_IKLAN="2" untuk FAN
    PENGATURAN_IKLAN="3" untuk StartApp
     */
    public static String PENGATURAN_IKLAN="1";

    /*
    ID Admob, untuk appid ada di string.xml
     */
    public static String ADMOB_INTER ="ca-app-pub-3940256099942544/1033173712";
    public static String ADMOB_NATIV = "ca-app-pub-3940256099942544/2247696110";
    public static String ADMOB_BANNER = "ca-app-pub-3940256099942544/6300978111";

    /*
    ID FAn
    TESTMODE_FAN = true untuk pengujian
    dan TESTMODE_FAN = false untuk publish app
     */
    public static String FAN_INTER ="YOUR_PLACEMENT_ID";
    public static String FAN_BANNER_NATIVE="YOUR_PLACEMENT_ID";
    public static String FAN_BANNER = "YOUR_PLACEMENT_ID";
    public static String FAN_BANNER_BIG = "YOUR_PLACEMENT_ID";
    public static String FAN_REWARD ="YOUR_PLACEMENT_ID" ;
    public static boolean TESTMODE_FAN = true ;

    /*
    ID StartApp
     */
    public static String STRATAPPID="123456789";

    /*Redirect App, ubah STATUS = "1" untuk melakukan redirect ke aplikasi baru, fitur ini harus tetap dalam
      kedaaan STATUS = "0"; selama aplikasi masih live
       */
    public static String STATUS = "0";
    public static String LINK = "https://play.google.com/store/apps/details?id=com.alquranterjemahanindonesia.guruandroid";


}
