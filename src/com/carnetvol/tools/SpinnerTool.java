package com.carnetvol.tools;

import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

/**
 * Created by o.chauvie on 05/02/2015.
 */
public class SpinnerTool {

    public static void SelectSpinnerItemByValue(Spinner spnr, String value)
    {
        ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(((String)adapter.getItem(position)).equals(value)) {
                spnr.setSelection(position);
                return;
            }
        }
    }


}
