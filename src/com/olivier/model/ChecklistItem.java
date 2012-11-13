package com.olivier.model;

public class ChecklistItem {

	private int id;
	private String action;
	private int order;
	
	
	
	public ChecklistItem(String action, int order) {
		super();
		this.action = action;
		this.order = order;
	}
	
	public ChecklistItem(int id, String action, int order) {
		super();
		this.id = id;
		this.action = action;
		this.order = order;
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
	 * Getter action
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * Setter action
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * Getter order
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * Setter order
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	
	
}
