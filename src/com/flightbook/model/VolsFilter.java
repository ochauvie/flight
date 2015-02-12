package com.flightbook.model;

import java.util.Date;

/**
 * Created by o.chauvie on 11/02/2015.
 */
public class VolsFilter {

    private Date dateDebut;
    private Date dateFin;
    private String typeAeronef;

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }



    public String getTypeAeronef() {
        return typeAeronef;
    }

    public void setTypeAeronef(String typeAeronef) {
        this.typeAeronef = typeAeronef;
    }
}
