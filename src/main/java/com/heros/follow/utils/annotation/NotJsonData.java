package com.heros.follow.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 *package hero.follow.annotation - gson使用,產生json時排除不需要的參數
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotJsonData {
	// Field tag only annotation
}
