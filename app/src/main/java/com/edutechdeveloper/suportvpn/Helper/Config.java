package com.edutechdeveloper.suportvpn.Helper;

import com.anchorfree.partner.api.data.Country;
import com.anjlab.android.iab.v3.BillingProcessor;

import java.util.Arrays;
import java.util.List;

public class Config {

    //Don't change
    //hydra SKD keys
    public static int countStart = 0;


    public static final String appSecretCode = "854df6289er6787sdf58sdr5wer6897sr587asdf858"; //Please contact me in what's app: +8801792064472

    public static final String carrierID = "Dora_free_vpn";//Please contact me in what's app: +8801792064472
    public static final String baseURL = "https://backend.northghost.com/";

    //LICENSE KEY FROM GOOGLE PLAY CONSOLE
    public static final String lisence_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsfaq3juvloBo5jDBoLnhqcmGcKeEG9xTTynHHEMc6NmAIH2M550FRvYHZkri5misG48L6sKxbOH/XNqgheBWodpvkp+f7KuACd1KATXXbdcJwsYLvKFOrKdgM2iYd6WG7lUop4yGwc9gS/B09nbsPIAPUZgBet7U76x629hrw1UxatCpw1lBA/zJcz9AyIQjEWTn4DOLn0niOjTeZDWQpEpmbi3vaAebEpqAG8OT0gM7E0uIEKNMOCPPSkXNYrQ/VFxDoovM/TpOotp6lCqRuiPq2T9K7iT8IQdzHbQRvkphjvxpFm9OacmrIVftMDhsCfJCVlyux4i190gM52EnRwIDAQAB";

    //Remove Ads subscription id
    public static final String remove_ads_one_month = "suportvpn";
    public static final String remove_ads_six_month = "suportvpn2";
    public static final String remove_ads_one_year = "suportvpn3";

    //country list

    public static List<Country> regions =  Arrays.asList(
            new Country("ca"),
            new Country("us"),
            new Country("gb"),
            new Country("sg"),
            new Country("in"),
            new Country("id"),
            new Country("de"),
            new Country("no"),
            new Country("hk"),
            new Country("ru"),
            new Country("jp"),
            new Country("dk"),
            new Country("ua"),
            new Country("fr"),
            new Country("br"),
            new Country("se"),
            new Country("ie"),
            new Country("ch"),
            new Country("it"),
            new Country("mx"),
            new Country("es"),
            new Country("ar"),
            new Country("au"),
            new Country("cz"),
            new Country("ro"),
            new Country("tr"),
            new Country("nl")
    );



    /*settings parameters (don't change them these are auto controlled by application flow)*/
    public static boolean ads_subscription = false;

    /*google IAP*/
    public static BillingProcessor bp;
    public static boolean isBPavailable = false;
}
