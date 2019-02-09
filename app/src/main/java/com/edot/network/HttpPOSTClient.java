package com.edot.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.edot.network.NetworkHelperUtil.convertHashMapToString;
import static com.edot.network.NetworkHelperUtil.generateSecurityHeader;

public class HttpPOSTClient implements HttpClient {

    private static final String LOG_TAG = "HttpPOSTClientLogTag";

    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    public boolean establishConnection(String url) {
        return establishConnection(url,null);
    }

    @Override
    public boolean establishConnection(String url, HashMap<String, String> params) {
        return establishConnection(url,params,null,false);
    }

    @Override
    public boolean establishConnection(String url, HashMap<String, String> params,
                                       String key, boolean appendSecurityHeader) {
        if (url.isEmpty())
        {
            return false;
        }
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            String paramString = convertHashMapToString(params);
            if (!paramString.isEmpty()) {
                byte[] paramsBytes = paramString.getBytes(StandardCharsets.UTF_8);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(paramsBytes.length));
                urlConnection.setRequestProperty("Content-Language", "en-US");
                if (appendSecurityHeader) {
                    urlConnection.setRequestProperty(SECURITY_HEADER_PROPERTY_NAME,
                            generateSecurityHeader(key, params));
                }
                urlConnection.setDoOutput(true);
                outputStream = urlConnection.getOutputStream();
                if (!writeFormData(paramsBytes))
                {
                    return false;
                }
            }
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            Log.d(LOG_TAG,"Exception while making connection with server-url "
                    +url+" : "+e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    private boolean writeFormData(byte[] data) {
        try {
            outputStream.write(data);
            return true;
        } catch (IOException e) {
            Log.d(LOG_TAG,"Exception while writing data in body : "+e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public void closeConnection() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d(LOG_TAG,"Exception while closing IOStreams : "+e.getLocalizedMessage());
            }
        }
    }

}
