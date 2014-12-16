package com.flightbook.model;


import android.graphics.Color;

public class Aeronef {
	
	public static final String T_PLANEUR = "Planeur";
	public static final String T_AVION = "Avion";
	public static final String T_PARAMOTEUR = "Paramoteur";
	public static final String T_HELICO = "Helico";
	public static final String T_AUTO = "Automobile";
	public static final String T_DIVERS = "Divers";
	
	public static final String ID = "aeronefId";
	public static final String NAME = "aeronef";
	public static final String TYPE = "type";
	
	private int id;
	private String name;
	private String type;
	private float wingSpan;
	private float weight;
	private String engine;
	private String firstFlight;
	private String comment;
	
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
	
	/**
	 * Getter wingSpan
	 * @return the wingSpan
	 */
	public float getWingSpan() {
		return wingSpan;
	}
	
	/**
	 * Setter wingSpan
	 * @param wingSpan the wingSpan to set
	 */
	public void setWingSpan(float wingSpan) {
		this.wingSpan = wingSpan;
	}
	
	/**
	 * Getter weight
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}
	
	/**
	 * Setter weight
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	/**
	 * Getter engine
	 * @return the engine
	 */
	public String getEngine() {
		return engine;
	}
	
	/**
	 * Setter engine
	 * @param engine the engine to set
	 */
	public void setEngine(String engine) {
		this.engine = engine;
	}
	
	/**
	 * Getter firstFlight
	 * @return the firstFlight
	 */
	public String getFirstFlight() {
		return firstFlight;
	}
	
	/**
	 * Setter firstFlight
	 * @param firstFlight the firstFlight to set
	 */
	public void setFirstFlight(String firstFlight) {
		this.firstFlight = firstFlight;
	}
	
	/**
	 * Getter comment
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Setter comment
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Return the color type
	 * @param type the type
	 * @return the color
	 */
	public static int getColor(String type) {
		if (Aeronef.T_PLANEUR.equals(type)) {
		    return Color.RED;
		  } else if (Aeronef.T_AVION.equals(type)) {
			  return Color.BLUE;
		  } else if (Aeronef.T_HELICO.equals(type)) {
			  return Color.BLACK;
		  } else if (Aeronef.T_PARAMOTEUR.equals(type)) {
			  return Color.MAGENTA;
		  } else if (Aeronef.T_AUTO.equals(type)) {
			  return Color.GRAY;
		  } else if (Aeronef.T_DIVERS.equals(type)) {
			  return Color.LTGRAY;
		  } else {
			  return Color.YELLOW;
		  }
	}
	
	
}
