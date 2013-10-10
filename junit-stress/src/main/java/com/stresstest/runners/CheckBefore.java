package com.stresstest.runners;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value = {METHOD})
@Retention(RUNTIME)
public @interface CheckBefore {

}
