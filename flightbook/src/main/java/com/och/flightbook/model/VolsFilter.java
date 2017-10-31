package com.och.flightbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Filter class for flights list
 */
public class VolsFilter implements Serializable {

    private Date dateDebut;
    private Date dateFin;
    private TypeAeronef typeAeronef;
    private Aeronef aeronef;
    private Site site;
    private List<TypeAeronef> types;

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

    public TypeAeronef getTypeAeronef() {
        return typeAeronef;
    }

    public void setTypeAeronef(TypeAeronef typeAeronef) {
        this.typeAeronef = typeAeronef;
    }

    public Aeronef getAeronef() {
        return aeronef;
    }

    public void setAeronef(Aeronef aeronef) {
        this.aeronef = aeronef;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<TypeAeronef> getTypes() {
        if (types == null) {
            types = new ArrayList<>();
        }
        return types;
    }

    public void setTypes(List<TypeAeronef> types) {
        this.types = types;
    }
}
