package com.hipalsports.enums;

/**
 * 性别。
 */
public enum GenderNum {
	
	MAN(0, "MAN"), WOMEN(1, "WOMEN");
	
	private final int value;
	private final String reasonPhrase;

	private GenderNum(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}
	
	public String reasonePhrase() {
		return this.reasonPhrase;
	}

}
