package com.dfh.tforder.awt;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import com.dfh.tforder.lic.ValidChecker;
import com.dfh.tforder.util.Utility;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class TFOrder extends JFrame implements ActionListener {

	private JTabbedPane jTabbedPane = new JTabbedPane();
	private ParamPanel paramPanel = new ParamPanel();
	private TickPanel tickPanel = new TickPanel();
	private OrderPanel orderPanel = new OrderPanel();

	public TFOrder() {
		init();
	}

	private void init() {
		ValidChecker.check(this);
		MenuBar meneBar = new MenuBar();
		Menu menuFile = new Menu("文件");
		MenuItem menuFileTick = new MenuItem("订阅");
		menuFileTick.addActionListener(this);
		menuFile.add(menuFileTick);
		menuFile.addSeparator();
		MenuItem menuFileStart = new MenuItem("启动");
		menuFileStart.addActionListener(this);
		menuFile.add(menuFileStart);
		MenuItem menuFileStop = new MenuItem("停止");
		menuFileStop.addActionListener(this);
		menuFile.add(menuFileStop);
		menuFile.addSeparator();
		MenuItem menuFileExit = new MenuItem("退出");
		menuFileExit.addActionListener(this);
		menuFile.add(menuFileExit);
		meneBar.add(menuFile);

		Menu menuSet = new Menu("设置");
		MenuItem menuSetParam = new MenuItem("参数");
		menuSetParam.addActionListener(this);
		menuSet.add(menuSetParam);
		meneBar.add(menuSet);

		Menu menuHelp = new Menu("帮助");
		MenuItem menuHelpManu = new MenuItem("说明");
		menuHelpManu.addActionListener(this);
		menuHelp.add(menuHelpManu);
		menuHelp.addSeparator();
		MenuItem menuHelpAbout = new MenuItem("关于");
		menuHelpAbout.addActionListener(this);
		menuHelp.add(menuHelpAbout);
		meneBar.add(menuHelp);
		setMenuBar(meneBar);

		JToolBar jtb = new JToolBar();
		String[] s = { "参数", "订阅", "启动", "停止", "退出" };
		int size = s.length;
		JButton[] button = new JButton[size];
		for (int i = 0; i < size; i++) {
			button[i] = new JButton(s[i]);
			button[i].addActionListener(this);
			if ("参数".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/param.png")));
			} else if ("订阅".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/tick.png")));
			} else if ("启动".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/start.png")));
			} else if ("停止".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/stop.png")));
			} else if ("退出".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/exit.png")));
			}
			// button[i].setIconTextGap(-20);
			button[i].setHorizontalTextPosition(JButton.CENTER);
			button[i].setVerticalTextPosition(JButton.BOTTOM);
			button[i].setBorderPainted(false);
			jtb.add(button[i]);
		}
		jtb.setFloatable(false);
		add(jtb, BorderLayout.NORTH);

		jTabbedPane.addTab("参数", null, paramPanel, "参数设置面板");
		jTabbedPane.addTab("行情", null, tickPanel, "行情显示面板");
		jTabbedPane.addTab("记录", null, orderPanel, "记录显示面板");
		add(jTabbedPane, BorderLayout.CENTER);
		setTitle("东方红国债期货自动下单工具v0.1东证资管");
		setSize(1024, 640);
		setLocation(180, 90);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int ret = JOptionPane.showConfirmDialog(null, "确认退出自动下单工具？", "确认", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.OK_OPTION)
					System.exit(0);
			}
		});
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("参数")) {
			jTabbedPane.setSelectedIndex(0);
		} else if (s.equals("订阅")) {
			jTabbedPane.setSelectedIndex(1);
			tickPanel.start();
		} else if (s.equals("启动")) {
			jTabbedPane.setSelectedIndex(2);
			orderPanel.start();
		} else if (s.equals("停止")) {
			jTabbedPane.setSelectedIndex(2);
			orderPanel.stop();
		} else if (s.equals("退出")) {
			int ret = JOptionPane.showConfirmDialog(null, "确认退出自动下单工具？", "确认", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
		} else if (s.equals("说明")) {
			JOptionPane.showMessageDialog(this, "使用方法：\n" + "1、设置参数\n" + "2、点击订阅，订阅行情\n" + "3、点击启动，自动下单\n" + "4、点击停止，停止下单（已下单会继续处理）");
		} else if (s.equals("关于")) {
			JOptionPane.showMessageDialog(this, "东方红国债期货自动下单工具\n Version: 0.1\n Build id: 0.1-20151112\n (c) Copyright DFHAM, Ltd. 2015. All rights reserved.");
		}
	}

	public static void main(String[] args) {
		new TFOrder();
	}

}
