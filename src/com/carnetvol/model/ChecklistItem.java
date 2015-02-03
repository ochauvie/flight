package com.carnetvol.model;

public class ChecklistItem {

	private int id;
	private String action;
	private int order;
	private boolean checked;
	
	
	
	public ChecklistItem(String action, int order) {
		super();
		this.action = action;
		this.order = order;
		this.checked = false;
	}
	
	public ChecklistItem(int id, String action, int order) {
		super();
		this.id = id;
		this.action = action;
		this.order = order;
		this.checked = false;
	}
	
	/**
	 * Getter id
	 * @return the id
	 */
	public int getId() {
		return id;
	}



	/**
	 * Getter checked
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Setter checked
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
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
