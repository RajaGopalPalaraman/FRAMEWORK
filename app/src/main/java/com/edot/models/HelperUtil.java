package com.edot.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public final class HelperUtil {

    static final String LOG_TAG = "EDOTModelLogTag";

    public static HashMap<String,HashMap<String,String>> readProperties(InputStream inputStream, String field, List<String> subFields)
    {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            String root = properties.getProperty(field);
            if (root == null || root.isEmpty())
            {
                Log.d(LOG_TAG,"HelperUtil::readProperties: property '"+ field +"' missing");
                return null;
            }
            String[] values = root.split(",");
            HashMap<String,HashMap<String,String>> map = new HashMap<>();
            for (String name : values)
            {
                HashMap<String,String> attrMap = new HashMap<>();
                for (String attr : subFields)
                {
                    String key = name+"_"+attr;
                    String value = properties.getProperty(key);
                    if(value == null || value.isEmpty())
                    {
                        attrMap = null;
                        Log.d(LOG_TAG,"property '"+ key +"' missing");
                        break;
                    }
                    attrMap.put(attr,value);
                }
                if(attrMap != null) {
                    map.put(name, attrMap);
                }
            }
            Log.d(LOG_TAG,map.toString());
            return map;
        } catch (Exception e) {
            Log.e(LOG_TAG,e.getLocalizedMessage());
            return null;
        }
    }

    public static Object getFieldFromClass(@NonNull Class targetClass, @Nullable Object classInstance, @NonNull String field)
    {
        Object object = null;
        if(targetClass == null || field == null || field.isEmpty())
        {
            Log.d(LOG_TAG,"HelperUtil::getFieldFromClass: Invalid arguments");
            return null;
        }
        try {
            Field declaredField = targetClass.getField(field);
            object = declaredField.get(classInstance);
        } catch (Exception e) {
            Log.d(LOG_TAG,e.getLocalizedMessage());
            return null;
        }
        return object;
    }

    /**
     *
     * @param jsonArray
     * @param keyFieldIndex - keys for the map that should be unique within jsonArray,
     *                      otherwise DuplicateValueFoundException will be thrown
     * @param innerKeys - List of
     * @return - null if jsonArray is null or innerKeys is null or empty,
     * otherwise HashMap&lt;String,HashMap&lt;String,String&gt;&gt;
     * @throws JSONException - exception thrown by JSONArray class
     */

    public static HashMap<String,HashMap<String,String>> jsonToHashMap(JSONArray jsonArray,
                                                                       int keyFieldIndex,
                                                                       List<String> innerKeys)
            throws JSONException {
        if (jsonArray != null && innerKeys != null && innerKeys.size() > 0)
        {
            keyFieldIndex = (keyFieldIndex > -1 && keyFieldIndex < innerKeys.size())? keyFieldIndex : 0;
            HashMap<String,HashMap<String,String>> mapHashMap = new HashMap<>();
            int length = jsonArray.length();
            for (int i=0 ;i<length ;i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                HashMap<String,String> innerMap = new HashMap<>();
                for (String k : innerKeys) {
                    innerMap.put(k,object.getString(k));
                }
                if (mapHashMap.containsKey(object.getString(innerKeys.get(keyFieldIndex)))) {
                    throw new DuplicateValueFoundException(object.getString(innerKeys.get(keyFieldIndex)));
                }
                mapHashMap.put(object.getString(innerKeys.get(keyFieldIndex)), innerMap);
            }
            return mapHashMap;
        }
        return null;
    }

}
