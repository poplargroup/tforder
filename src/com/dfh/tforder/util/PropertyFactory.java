package com.dfh.tforder.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhaoyang
 * 
 */
public class PropertyFactory {

	private static Properties prop;

	public static Properties getProperties() {
		if (prop == null) {
			prop = new Properties();
			loadProperty();
		}
		return prop;
	}

	public static void loadProperty() {
		try {
			InputStream inFile = new BufferedInputStream(new FileInputStream("param.properties"));
			prop.load(inFile);
			inFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveProperty() {
		try {
			FileOutputStream outFile = new FileOutputStream("param.properties", true);
			prop.store(outFile, "The New properties file");
			outFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
