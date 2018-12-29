package com.edot.models;

import android.view.View;

public interface Model<T1,T2> {

    View renderChildMap(T2 childMap);
    View renderMap(T1 map);

}
