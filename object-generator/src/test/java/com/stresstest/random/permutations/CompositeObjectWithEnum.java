package com.stresstest.random.permutations;

public class CompositeObjectWithEnum {

	final private int value;

	final private PublicEnum publicEnum;

	public CompositeObjectWithEnum(int value, PublicEnum publicEnum) {
		this.value = value;
		this.publicEnum = publicEnum;
	}

	public int getValue() {
		return value;
	}

	public PublicEnum getPublicEnum() {
		return publicEnum;
	}
}
