package com.edot.models;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
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
    protected LinearLayout linearLayout;

    protected LinearViewModel(Context context) {
        this.context = context;
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
    }

    public void setLayoutOrientation(@LinearLayoutCompat.OrientationMode int orientation)
    {
        if(orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            linearLayout.setOrientation(orientation);
        }
    }

    @Override
    public abstract View renderChildMap(HashMap<String,String> childMap);

    @Override
    public View renderMap(HashMap<String,HashMap<String,String>> map)
    {
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
                if(view != null) {
                    linearLayout.addView(view);
                }
            }
        } catch (Exception e)
        {
            Log.d(HelperUtil.LOG_TAG,e.getLocalizedMessage());
        }

        return linearLayout;
    }

}
