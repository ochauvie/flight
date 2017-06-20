package com.och.flightbook.tools;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerTool {

    public static void SelectSpinnerItemByValue(Spinner spnr, Object value)
    {
        if (value!=null) {
            ArrayAdapter adapter = (ArrayAdapter) spnr.getAdapter();
            for (int position = 0; position < adapter.getCount(); position++) {
                if (( adapter.getItem(position)).toString().equals(value.toString())) {
                    spnr.setSelection(position);
                    return;
                }
            }
        }
    }


}
