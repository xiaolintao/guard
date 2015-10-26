package com.hipalsports.enums;

public enum UserEnum {

	UNKNOWN(-1, "UNKNOWN"),EMAIL(0, "EMAIL"), PHONE(1, "PHONE");
	
	private final int value;
	private final String reasonPhrase;

	private UserEnum(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}
	
	public String reasonePhrase() {
		return this.reasonPhrase;
	}
	
	public static UserEnum getEnum(int value) {
		UserEnum[] enums = UserEnum.values();
		
		for (UserEnum item : enums) {
			if (item.value == value) {
				return item;
			}
		}
		
		return UNKNOWN;
	}
}
