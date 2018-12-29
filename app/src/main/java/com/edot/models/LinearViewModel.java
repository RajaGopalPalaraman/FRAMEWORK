package com.edot.models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Hashmap is used as data model
 */

public abstract class LinearViewModel implements Model<HashMap<String,HashMap<String,String>>,HashMap<String,String>> {

    private static final String EMPTY_MAP = "Empty Hash Map";

    protected Context context;

    protected LinearViewModel(Context context) {
        this.context = context;
    }

    @Override
    public abstract View renderChildMap(HashMap<String,String> childMap);

    @Override
    public View renderMap(HashMap<String,HashMap<String,String>> map)
    {
        LinearLayout linearLayout = new LinearLayout(context);

        try {
            if (map == null || map.isEmpty()) {
                Log.d(HelperUtil.LOG_TAG,EMPTY_MAP);
                return linearLayout;
            }

            Set<String> keySet = map.keySet();
            Object[] keyArray = keySet.toArray();
            Arrays.sort(keyArray);

            for (Object k : keyArray) {
                String key = (String) k;
                Log.d(HelperUtil.LOG_TAG,key);
                HashMap<String, String> childMap = map.get(key);
                View view = renderChildMap(childMap);
                linearLayout.addView(view);
            }
        } catch (Exception e)
        {
            Log.d(HelperUtil.LOG_TAG,e.getLocalizedMessage());
        }

        return linearLayout;
    }

}
