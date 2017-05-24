package com.och.flightbook.activity.listener;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;


public class DateSetListener implements DatePickerDialog.OnDateSetListener {

    private EditText editText;

    public DateSetListener(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String day = String.valueOf(dayOfMonth);
        if (day.length()<2) {day = "0" + day;}
        String month = String.valueOf(monthOfYear+1);
        if (month.length()<2) {month = "0" + month;}
        String y = String.valueOf(year);
        if (y.length()<2) {y = "0" + y;}
        editText.setText(day + "/" + month + "/" + y);

    }
}
