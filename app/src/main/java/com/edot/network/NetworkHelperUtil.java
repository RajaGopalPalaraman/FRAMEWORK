package com.edot.network;

import android.util.Log;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class NetworkHelperUtil {

    private static final String LOG_TAG = "NetworkHelperUtilLogTag";

    public static String convertHashMapToString(HashMap<String, String> params)
    {
        if (params == null || params.isEmpty())
        {
            return "";
        }
        boolean appendAmpersand = false;
        StringBuilder paramsStringBuilder = new StringBuilder();
        for (String name : params.keySet())
        {
            if (appendAmpersand)
            {
                paramsStringBuilder.append("&");
            }
            paramsStringBuilder.append(name);
            paramsStringBuilder.append("=");
            paramsStringBuilder.append(params.get(name));
            appendAmpersand = true;
        }
        Log.d(LOG_TAG,"Converted Params HashMap to String : "+paramsStringBuilder.toString());
        return paramsStringBuilder.toString();
    }

    public static String generateSecurityHeader(String key, HashMap<String,String> params)
    {
        String signature = "";
        if (key == null || key.isEmpty())
        {
            return signature;
        }
        try {
            String data = convertHashMapToString(params);
            byte[] decodedKey = Hex.decodeHex(key.toCharArray());
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "HmacSHA1");
            Mac mac = null;
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] signatureBytes = mac.doFinal(dataBytes);
            signature = new String(Base64.encodeBase64(signatureBytes), StandardCharsets.UTF_8);
            Log.d(LOG_TAG,"Signature generated : "+signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | DecoderException e) {
            Log.d(LOG_TAG,"Exception while generating SecurityHeader : "+e.getLocalizedMessage());
        }
        return signature;
    }

    public static HashMap<String,String> convertParamStringToHashMap(String url)
    {
        HashMap<String,String> paramsMap = new HashMap<>();
        int index = url.indexOf('?');
        if (index != -1)
        {
            String parameters = url.substring(index+1);
            String[] paramsArray = parameters.split("&");
            for (String s : paramsArray)
            {
                int i = s.indexOf('=');
                if (i != -1)
                {
                    paramsMap.put(s.substring(0,i),s.substring(i+1));
                }
                else
                {
                    paramsMap.put(s,"");
                }
            }
        }

        return paramsMap;
    }

    public static String readData(String url, HashMap<String,String> params)
    {
        HttpClient httpClient = new HttpPOSTClient();
        if (httpClient.establishConnection(url,params))
        {
            InputStream inputStream = httpClient.getInputStream();
            byte[] bytes = new byte[500];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(0);
            int length;
            try {
                while ((length = inputStream.read(bytes)) != -1)
                {
                    byteArrayOutputStream.write(bytes,0,length);
                }
                return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                Log.d(LOG_TAG,"Exception while reading data from server : "
                        +e.getLocalizedMessage());
            }
        }
        return null;
    }

}
