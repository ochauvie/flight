package com.olivier.model;

import android.graphics.Color;

public class Aeronef {
	
	public static final String T_PLANEUR = "Planeur";
	public static final String T_AVION = "Avion";
	public static final String T_PARAMOTEUR = "Paramoteur";
	public static final String T_HELICO = "Helico";
	public static final String T_AUTO = "Automobile";
	public static final String T_DIVERS = "Divers";
	
	private int id;
	private String name;
	private String type;
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
	 * Getter name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setter name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
	public static int getColor(String type) {
		if (Aeronef.T_PLANEUR.equals(type)) {
		    return Color.RED;
		  } else if (Aeronef.T_AVION.equals(type)) {
			  return Color.BLUE;
		  } else if (Aeronef.T_HELICO.equals(type)) {
			  return Color.GREEN;
		  } else if (Aeronef.T_PARAMOTEUR.equals(type)) {
			  return Color.MAGENTA;
		  } else if (Aeronef.T_AUTO.equals(type)) {
			  return Color.GRAY;
		  } else if (Aeronef.T_DIVERS.equals(type)) {
			  return Color.LTGRAY;
		  } else {
			  return Color.WHITE;
		  }
	}
	
	
}
