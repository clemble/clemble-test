package com.stresstest.random.constructor.validation;

import java.lang.annotation.Annotation;

import com.stresstest.random.constructor.ClassPropertySetter;

abstract public class AbstractConstraintValidator {
	
	public <T> void applicable(ClassPropertySetter<T> propertySetter) {
		
	}

	abstract public Class<? extends Annotation> getValidationAnnotation();

}
