package com.flightbook.model;

import java.util.Date;

public class Vol {

	private int id;
	private String aeronef;
	private String type;
	private int minutesVol;
	private int minutesMoteur;
	private int secondsMoteur;
	private Date dateVol;
	private String note;
	private String lieu;
	
	
	
	/**
	 * Getter lieu
	 * @return the lieu
	 */
	public String getLieu() {
		return lieu;
	}
	/**
	 * Setter lieu
	 * @param lieu the lieu to set
	 */
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	/**
	 * Getter note
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * Setter note
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * Getter type
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * Setter type
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Getter dateVol
	 * @return the dateVol
	 */
	public Date getDateVol() {
		return dateVol;
	}
	/**
	 * Setter dateVol
	 * @param dateVol the dateVol to set
	 */
	public void setDateVol(Date dateVol) {
		this.dateVol = dateVol;
	}
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
	/**
	 * Getter aeronef
	 * @return the aeronef
	 */
	public String getAeronef() {
		return aeronef;
	}
	/**
	 * Setter aeronef
	 * @param aeronef the aeronef to set
	 */
	public void setAeronef(String aeronef) {
		this.aeronef = aeronef;
	}
	/**
	 * Getter minutesVol
	 * @return the minutesVol
	 */
	public int getMinutesVol() {
		return minutesVol;
	}
	/**
	 * Setter minutesVol
	 * @param minutesVol the minutesVol to set
	 */
	public void setMinutesVol(int minutesVol) {
		this.minutesVol = minutesVol;
	}
	
	/**
	 * Getter minutesMoteur
	 * @return the minutesMoteur
	 */
	public int getMinutesMoteur() {
		return minutesMoteur;
	}
	/**
	 * Setter minutesMoteur
	 * @param minutesMoteur the minutesMoteur to set
	 */
	public void setMinutesMoteur(int minutesMoteur) {
		this.minutesMoteur = minutesMoteur;
	}
	/**
	 * Getter secondsMoteur
	 * @return the secondsMoteur
	 */
	public int getSecondsMoteur() {
		return secondsMoteur;
	}
	/**
	 * Setter secondsMoteur
	 * @param secondsMoteur the secondsMoteur to set
	 */
	public void setSecondsMoteur(int secondsMoteur) {
		this.secondsMoteur = secondsMoteur;
	}
	
	
	
	
}
