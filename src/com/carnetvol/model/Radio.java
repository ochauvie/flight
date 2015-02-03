package com.carnetvol.model;

import java.util.ArrayList;
import java.util.List;


public class Radio  {
	
	public static final String RADIO_ID = "radioId";
	
	private int id;
	private String name;
	private List<Switch> switchs = new ArrayList<Switch>();
	private List<Potar> potars = new ArrayList<Potar>();
	
	
	
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
	 * Getter switchs
	 * @return the switchs
	 */
	public List<Switch> getSwitchs() {
		return switchs;
	}
	/**
	 * Setter switchs
	 * @param switchs the switchs to set
	 */
	public void setSwitchs(List<Switch> switchs) {
		this.switchs = switchs;
	}
	/**
	 * Getter potars
	 * @return the potars
	 */
	public List<Potar> getPotars() {
		return potars;
	}
	/**
	 * Setter potars
	 * @param potars the potars to set
	 */
	public void setPotars(List<Potar> potars) {
		this.potars = potars;
	}
	
	public void addSwitch(Switch s) {
		switchs.add(s);
	}
	
	public void addPotar(Potar potar) {
		potars.add(potar);
	}
	
}
