package com.och.flightbook.model;

import com.och.flightbook.tools.JsonExclude;

import java.util.ArrayList;


public class Checklist {

	public static final String NAME = "checklistName";

    @JsonExclude private int id;
	private String name;
	private ArrayList<ChecklistItem> items = new ArrayList<ChecklistItem>();
	
	public Checklist(String name) {
		super();
		this.name = name;
	}
	
	public Checklist(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	 * Getter items
	 * @return the items
	 */
	public ArrayList<ChecklistItem> getItems() {
		return items;
	}
	/**
	 * Setter items
	 * @param items the items to set
	 */
	public void setItems(ArrayList<ChecklistItem> items) {
		this.items = items;
	}
	
	public void addItem(ChecklistItem item) {
		items.add(item);
	}
	
}
