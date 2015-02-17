package com.flightbook.model;



import android.graphics.Color;

import com.flightbook.tools.JsonExclude;

import java.io.Serializable;
import java.util.Date;

public class Accu implements Serializable {

    @JsonExclude private int id;
	private TypeAccu type;
    private int nbElements;
    private int capacite;
    private float voltage;
    private int tauxDecharge;
    private int numero;
    private String nom;
    private String marque;
    private Date dateAchat;
    private int nbCycles;

	/**
	 * Getter id
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Setter id
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

    public TypeAccu getType() {
        return type;
    }

    public void setType(TypeAccu type) {
        this.type = type;
    }

    public int getNbElements() {
        return nbElements;
    }

    public void setNbElements(int nbElements) {
        this.nbElements = nbElements;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getTauxDecharge() {
        return tauxDecharge;
    }

    public void setTauxDecharge(int tauxDecharge) {
        this.tauxDecharge = tauxDecharge;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Date getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Date dateAchat) {
        this.dateAchat = dateAchat;
    }

    public int getNbCycles() {
        return nbCycles;
    }

    public void setNbCycles(int nbCycles) {
        this.nbCycles = nbCycles;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    public static int getColor(String type) {
        if (TypeAccu.LIPO.name().equals(type)) {
            return Color.rgb(219, 23, 2);
        } else if (TypeAccu.LIFE.name().equals(type)) {
            return Color.rgb(49, 140, 231);
        } else if (TypeAccu.LIION.name().equals(type)) {
            return Color.rgb(31, 160, 85);
        } else if (TypeAccu.NIMH.name().equals(type)) {
            return Color.rgb(246, 220, 18);
        } else if (TypeAccu.NICD.name().equals(type)) {
            return Color.GRAY;
        } else if (TypeAccu.PB.name().equals(type)) {
            return Color.rgb(47, 30, 14);
        } else {
            return Color.MAGENTA;
        }
    }
}
