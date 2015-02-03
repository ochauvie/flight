package com.carnetvol.model;



import java.io.Serializable;
import java.util.Date;

public class Accu implements Serializable {

	private int id;
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
}
