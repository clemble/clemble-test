package com.stresstest.random.permutations;

public class ObjectWithPublicEnum {

	final private PublicEnum publicEnum;
	
	public ObjectWithPublicEnum(final PublicEnum enumPublic) {
		this.publicEnum = enumPublic;
	}

	public PublicEnum getPublicEnum() {
		return publicEnum;
	}
}
