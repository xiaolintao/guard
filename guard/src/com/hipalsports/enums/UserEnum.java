package com.hipalsports.enums;

/**
 * 用户账号的类别。
 */
public enum UserEnum {

	UNKNOWN(-1, "UNKNOWN"),EMAIL(0, "EMAIL"), PHONE(1, "PHONE");

	private final int value;
	private final String reasonPhrase;

	private UserEnum(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return value;
	}

	public String reasonePhrase() {
		return reasonPhrase;
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
