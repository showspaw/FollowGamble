package com.heros.follow.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.heros.follow.utils.annotation.NotJsonData;

//MyExclusionStrategy.java - gson 過濾參數的設定
public class MyExclusionStrategy implements ExclusionStrategy {

	public MyExclusionStrategy() {

	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(NotJsonData.class) != null;
	}
}
