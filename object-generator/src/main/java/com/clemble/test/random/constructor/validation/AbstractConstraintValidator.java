package com.clemble.test.random.constructor.validation;

import java.lang.annotation.Annotation;

import com.clemble.test.random.constructor.ClassPropertySetter;

abstract public class AbstractConstraintValidator {
	
	public <T> void applicable(ClassPropertySetter<T> propertySetter) {
		
	}

	abstract public Class<? extends Annotation> getValidationAnnotation();

}
