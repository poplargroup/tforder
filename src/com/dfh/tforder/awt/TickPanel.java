package com.dfh.tforder.awt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.dfh.tforder.tick.Exchange;
import com.dfh.tforder.tick.LiveFeed;
import com.dfh.tforder.tick.TradeableInstrumentImpl;
import com.dfh.tforder.util.PropertyFactory;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class TickPanel extends JPanel implements ActionListener {

	// 标识是否已订阅行情
	private static AtomicBoolean ticked = new AtomicBoolean(false);

	public static Boolean getTicked() {
		return ticked.get();
	}

	private LiveFeed feed = new LiveFeed();

	private static JTextArea consoleArea = new JTextArea();

	public static JTextArea getConsoleArea() {
		return consoleArea;
	}

	public TickPanel() {
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JScrollPane consoleScroll = new JScrollPane(consoleArea);
		consoleScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(consoleScroll, BorderLayout.CENTER);
		consoleArea.setLineWrap(true);
		consoleArea.setEditable(false);
		consoleArea.setBackground(Color.black);
		consoleArea.setForeground(Color.CYAN);
		consoleArea.setText("");

		JButton clearButton = new JButton("清空");
		clearButton.addActionListener(this);
		add(clearButton, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("清空")) {
			consoleArea.setText("");
		}
	}

	public void start() {
		if (ticked.get()) {
			JOptionPane.showMessageDialog(this, "行情已订阅，不需重复订阅");
			return;
		}
		consoleArea.append("started!\n");

		Properties prop = PropertyFactory.getProperties();
		String host = prop.getProperty("host");
		int port = Integer.parseInt(prop.getProperty("port"));
		feed.setHost(host);
		feed.setPort(port);
		feed.springInit();
		String symbolA = prop.getProperty("liquidBad");
		String symbolB = prop.getProperty("liquidGood");

		TradeableInstrumentImpl insA = new TradeableInstrumentImpl();
		insA.setSymbol(symbolA);
		insA.setExchange(Exchange.INDEX_FUTURE);
		TradeableInstrumentImpl insB = new TradeableInstrumentImpl();
		insB.setSymbol(symbolB);
		insB.setExchange(Exchange.INDEX_FUTURE);
		try {
			// feed.unsubscribeInternal();
			feed.subscribeInternal(insA);
			feed.subscribeInternal(insB);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ticked.set(true);
	}

	public void stop() {
		try {
			feed.unsubscribeInternal();// 取消订阅
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ticked.set(false);
		consoleArea.append("stopped!\n");
	}

}
