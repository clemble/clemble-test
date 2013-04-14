package com.stresstest.random.permutations.external;

public class ExternalObjectWithPublicEnum {

	final private ExternalPublicEnum externalPublicEnum;

	public ExternalObjectWithPublicEnum(final ExternalPublicEnum enumPublic) {
		this.externalPublicEnum = enumPublic;
	}

	public ExternalPublicEnum getExternalPublicEnum() {
		return externalPublicEnum;
	}

}
