package com.dfh.tforder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUtil {

	public static void WriteIn(String str) {
		try {
			Properties prop = PropertyFactory.getProperties();
			String path = prop.getProperty("path");
			String inFileName = path+"in.txt";
			File file = new File(inFileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file, true);
			StringBuffer sb = new StringBuffer();
			sb.append(str);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<String> ReadOut(String MsgId, String Orderidx, String Msg, String MsgTime) {
		List<String> outList = new ArrayList<String>();
		try {
			Properties prop = PropertyFactory.getProperties();
			String path = prop.getProperty("path");
			String outFileName = path+"out.txt";
			File file = new File(outFileName);
			if (!file.exists() || file.isDirectory())
				throw new FileNotFoundException();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String lineOut = null;
			lineOut = br.readLine();
			while (lineOut != null) {
				String[] lineArr = lineOut.split("\\|");
				if (("03_MsgID@"+MsgId).equals(lineArr[2]) && ("05_OrderIdx@"+Orderidx).equals(lineArr[4]) && ("04_Msg@"+Msg).equals(lineArr[3]) && (new String("02_MsgTime@")+MsgTime).compareTo(lineArr[1]) < 0) {
					outList.add(lineOut);
				}
				lineOut = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outList;
	}

}
