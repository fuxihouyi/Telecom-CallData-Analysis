package com.abner.ct.web.Util;


import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;

public class PhoneUtil {
    /**
     * 提供与电话号码相关的运营商信息
     */
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    /**
     * 提供与电话号码有关的地理信息
     */
    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

    /**
     * 中国大陆区区号
     */
    private final static int COUNTRY_CODE = 86;

    /**
     * 功能：根据手机号 判断手机运营商
     *
     * @param phoneNumber 手机号码
     * @return 如：广东省广州市移动
     */
    public static String getCarrier(String phoneNumber) {

        long phone = Long.parseLong(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(COUNTRY_CODE);
        pn.setNationalNumber(phone);
        // 返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }

    /**
     * 功能：根据手机号 获取手机归属地
     *
     * @param phoneNumber 手机号码
     * @return 如：广东省广州市
     */
    public static String getGeo(String phoneNumber) {
        long phone = Long.parseLong(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(COUNTRY_CODE);
        pn.setNationalNumber(phone);
        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }

//    public static void main(String[] args) {
//        Long s = System.currentTimeMillis();
//        String geo = getGeo("13682396876");
//        String carrier = getCarrier("13682396876");
//        if( geo.length() == 0){
//            geo = "未知归属地";
//
//        }
//        System.out.println(geo);
//        System.out.println(carrier);
//        System.out.println(System.currentTimeMillis() - s + ":ms");
//    }


}
