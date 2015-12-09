package com.dfh.tforder.util;

import java.net.URL;

/**
 * @author zhaoyang
 * 
 */
public class Utility {

	@SuppressWarnings("unchecked")
	public static URL getURL(Class clazz, String name) {
		return clazz.getClassLoader().getResource(name);
	}

}