package com.stresstest.random.permutations;

public class ObjectWithDefaultEnum {
	
	final private DefaultEnum defaultEnum;
	
	public ObjectWithDefaultEnum(final DefaultEnum enumPublic) {
		this.defaultEnum = enumPublic;
	}

	public DefaultEnum getDefaultEnum() {
		return defaultEnum;
	}
}
