package com.dfh.tforder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.dfh.tforder.order.OrderFeed;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class OrderPanel extends JPanel {

	private OrderFeed orderFeed = new OrderFeed();

	private static JTextArea consoleArea = new JTextArea();

	public static JTextArea getConsoleArea() {
		return consoleArea;
	}

	public OrderPanel() {
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
	}

	public void start() {
		boolean ticked = TickPanel.getTicked();
		if (!ticked) {
			JOptionPane.showMessageDialog(this, "请先订阅行情");
			return;
		}
		boolean started = OrderFeed.getStarted();
		if (started) {
			JOptionPane.showMessageDialog(this, "已经启动，不需重新启动");
			return;
		}
		consoleArea.append("started!\n");
		orderFeed.start();
	}

	public void stop() {
		boolean running = OrderFeed.getRunning();
		if (running) {
			JOptionPane.showMessageDialog(this, "订单正在执行，不能停止");
			return;
		}
		orderFeed.stop();
		consoleArea.append("stopped!\n");
	}

}
