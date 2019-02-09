package com.edot.network;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import static com.edot.network.NetworkHelperUtil.convertHashMapToString;
import static com.edot.network.NetworkHelperUtil.convertParamStringToHashMap;

public final class HttpGETClient implements HttpClient {

    private static final String LOG_TAG = "HttpGETClientLogTag";

    private InputStream inputStream;

    @Override
    public boolean establishConnection(String url) {
        if (url.isEmpty())
        {
            return false;
        }
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            Log.d(LOG_TAG,"Exception while making connection with server-url "
                    +url+" : "+e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean establishConnection(String url, HashMap<String, String> params) {
        if (url.isEmpty())
        {
            return false;
        }
        HashMap<String,String> paramsInURl = convertParamStringToHashMap(url);
        if (params != null) {
            paramsInURl.putAll(params);
        }
        String paramString = convertHashMapToString(paramsInURl);
        if (!paramString.isEmpty()) {
            int index = url.indexOf('?');
            if (index != -1)
            {
                url = url.substring(0,index+1);
            }
            else
            {
                url = url + "?";
            }
            url = url + paramString;
            Log.d(LOG_TAG,"Parameter appended URL : "+url);
        }
        return establishConnection(url);
    }

    @Override
    public boolean establishConnection(String url, HashMap<String, String> params,
                                       String key, boolean appendSecurityHeader) {
        return establishConnection(url,params);
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
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
