package com.edot.models;

import android.support.annotation.NonNull;
import android.util.Log;

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
                Log.d(LOG_TAG,"property '"+ field +"' missing");
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

    public static Object getStaticFieldFromClass(@NonNull Class targetClass,@NonNull String field)
    {
        Object object = null;
        if(targetClass == null || field == null || field.isEmpty())
        {
            Log.d(LOG_TAG,"HelperUtil::getFieldFromClass: Invalid arguments");
            return null;
        }
        try {
            Field declaredField = targetClass.getField(field);
            object = declaredField.get(null);
        } catch (Exception e) {
            Log.d(LOG_TAG,e.getLocalizedMessage());
            return null;
        }
        return object;
    }

}
