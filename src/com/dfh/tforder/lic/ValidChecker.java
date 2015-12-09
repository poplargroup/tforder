package com.dfh.tforder.lic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.dfh.tforder.util.PropertyFactory;

public class ValidChecker {

	private String currentDirectory;

	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public static void check(JFrame jf) {
		File licFile = new File("license.lic");
		if (!licFile.exists()) {
			JOptionPane.showMessageDialog(jf, "license文件未找到");
			System.exit(0);
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(licFile));
			String licEn = br.readLine();
			if (licEn != null) {
				Properties prop = PropertyFactory.getProperties();
				String privateKeyStr = prop.getProperty("privateKeyStr");
				PrivateKey privateKey = RSACoder.getPrivateKey(privateKeyStr);
				String licDe = RSACoder.decrypt(privateKey, licEn);
				if (!isDate(licDe)) {
					JOptionPane.showMessageDialog(jf, "license文件错误");
					System.exit(0);
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String today = df.format(new Date());
				if (licDe.compareTo(today) < 0) {
					JOptionPane.showMessageDialog(jf, "license文件已过期");
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(jf, "license文件校验错误");
			System.exit(0);
		}
	}

	/**
	 * 判断日期格式和范围
	 */
	public static boolean isDate(String date) {
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		boolean dateType = mat.matches();
		return dateType;
	}

}
