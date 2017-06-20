package com.och.flightbook.model;

import android.graphics.Color;

import com.och.flightbook.R;
import com.och.flightbook.sqllite.DbApplicationContext;

public enum TypeAeronef {
    ALL(R.string.opt_all, Color.BLACK),
    PLANEUR(R.string.opt_planeur, Color.rgb(219, 23, 2)),
    AVION(R.string.opt_avion, Color.rgb(49,140,231)),
    HELICO(R.string.opt_helico, Color.rgb(31,160,85)),
    PARAMOTEUR(R.string.opt_paramoteur, Color.rgb(246,220,18)),
    AUTO(R.string.opt_auto, Color.GRAY),
    DIVERS(R.string.opt_divers, Color.rgb(225,206,154));

    private final int label;
    private final int color;

    TypeAeronef(int label, int color) {
        this.label = label;
        this.color = color;
    }

    public int getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return DbApplicationContext.get().getString(label);
    }

}
