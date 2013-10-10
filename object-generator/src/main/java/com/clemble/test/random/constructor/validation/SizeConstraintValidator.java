package com.clemble.test.random.constructor.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.validation.constraints.Size;

public class SizeConstraintValidator {

	public Class<?> getValidationAnnotation(){
		return Size.class;
	}
	
	public void apply(Field field, Method method) {
		Annotation sizeAnnotation = field.getAnnotation(Size.class);
		method.getAnnotation(Size.class);
		
	}
}
