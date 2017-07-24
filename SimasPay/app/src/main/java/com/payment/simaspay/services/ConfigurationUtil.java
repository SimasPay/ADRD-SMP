package com.payment.simaspay.services;

/**
 *
 * @author Srinu
 */
public class ConfigurationUtil {

    private static String url = "http://115.119.120.118:8080/webapi/dynamic";
    private static String smsUrl = "http://app.m-campaigner.in/SendIndividualSMS.aspx?UserID=bWZpbm8=&Key=bWZpbm9eKiQ=&SenderName=mfino&SenderID=47&MobileNo=";
    static String repString;
    public static String getURL() {
        return url;
    }

    public static String getSmsUrl() {
        return smsUrl;
    }


    
    public static String replace(String oldStr, String newStr, String inString) {
        int start = inString.indexOf(oldStr);
        if (start == -1) {
            return inString;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(inString.substring(0, start));
        sb.append(newStr);
        sb.append(inString.substring(start + oldStr.length()));
        return sb.toString();
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        if (Constants.EMPTY_STRING.equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static String normalizeMDN(String MDN) {

        int start = 0;
        if (ConfigurationUtil.isBlank(MDN)) {
            return "";
        }

        MDN = MDN.trim();
        while (MDN.startsWith("234", start)) {
            start += "234".length();
        }

        while (start < MDN.length()) {
            if ('0' == MDN.charAt(start)) {
                start++;
            } else {
                break;
            }
        }

        return "234" + MDN.substring(start);
    }
}

