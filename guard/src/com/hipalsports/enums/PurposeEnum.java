package com.hipalsports.enums;

/**
 * 验证码用途。
 */
public enum PurposeEnum {

	UNKNOWN(-1, "UNKNOWN"), REGISTER(0, "REGISTER"), RESET_PWD(1, "RESET_PWD");
	
	private final int value;
	private final String reasonPhrase;

	private PurposeEnum(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}
	
	public String reasonePhrase() {
		return this.reasonPhrase;
	}
	
	public static PurposeEnum getEnum(int value) {
		PurposeEnum[] enums = PurposeEnum.values();
		
		for (PurposeEnum item : enums) {
			if (item.value == value) {
				return item;
			}
		}
		
		return UNKNOWN;
	}
}
