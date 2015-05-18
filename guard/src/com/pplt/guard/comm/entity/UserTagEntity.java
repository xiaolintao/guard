package com.pplt.guard.comm.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 用户标签。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTagEntity {

	private int id;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
