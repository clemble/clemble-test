package com.stresstest.random.permutations.external;

public class ExternalObjectWithDefaultEnum {
	final private ExternalDefaultEnum externalDefaultEnum;

	public ExternalObjectWithDefaultEnum(final ExternalDefaultEnum enumPublic) {
		this.externalDefaultEnum = enumPublic;
	}

	public ExternalDefaultEnum getExternalDefaultEnum() {
		return externalDefaultEnum;
	}
}
