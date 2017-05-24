package com.och.flightbook.model;


import com.och.flightbook.tools.JsonExclude;

import java.io.Serializable;

public class Site implements Serializable {

    @JsonExclude private int id;
	private String name;
	private String comment;
    private int isDefault;
	
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

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
