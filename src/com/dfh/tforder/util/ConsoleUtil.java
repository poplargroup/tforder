package com.dfh.tforder.util;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.dfh.tforder.awt.OrderPanel;
import com.dfh.tforder.awt.TickPanel;

public class ConsoleUtil {

	private static final Logger log = Logger.getLogger(ConsoleUtil.class);

	public static void printTick(String str) {
		JTextArea tickArea = TickPanel.getConsoleArea();
		tickArea.append(str + "\n");
		tickArea.setCaretPosition(tickArea.getText().length());
	}

	public static void printOrder(String str) {
		JTextArea orderArea = OrderPanel.getConsoleArea();
		orderArea.append(str + "\n");
		orderArea.setCaretPosition(orderArea.getText().length());
		log.info(str);
	}

}
