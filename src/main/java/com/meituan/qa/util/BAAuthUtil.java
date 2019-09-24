package com.meituan.qa.util;

import org.apache.http.client.methods.HttpRequestBase;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.codec.binary.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BAAuthUtil {
    private static final String ALGORITHM_HMAC_SHA1 = "HmacSHA1";

    public static final String DateHeader = "Date";
    public static final String AuthorizationHeader = "Authorization";


    public static void authHttpRequest(HttpRequestBase httpRequest, String clientId, String secret) {
        String dateHeader = BAAuthUtil.getAuthDate(new Date());

        String authorizationHeader = BAAuthUtil.getSignature(
                httpRequest.getURI().getPath(), httpRequest.getMethod(), dateHeader, clientId, secret);

        httpRequest.setHeader(BAAuthUtil.DateHeader, dateHeader);
        httpRequest.setHeader(BAAuthUtil.AuthorizationHeader, authorizationHeader);
    }

    public static String getAuthDate(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    public static String getSignature(String requestPath, String requestMethod, String date, String clientID, String clientSecret) {
        String signature = "MWS " + clientID + ":";
        String string_to_sign = requestMethod + " " + requestPath + "\n" + date;

        try {
            signature += generateSignature(string_to_sign.getBytes(), clientSecret.getBytes());
        } catch (Exception e) {
            System.err.println("签名失败：" + string_to_sign + "," + clientSecret);
            e.printStackTrace();
        }

        return signature;
    }

    private static String generateSignature(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, ALGORITHM_HMAC_SHA1);
        Mac mac = Mac.getInstance(ALGORITHM_HMAC_SHA1);
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(data);
        return new String(Base64.encodeBase64(rawHmac));
    }
}
