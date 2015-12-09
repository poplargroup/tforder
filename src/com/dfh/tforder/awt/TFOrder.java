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
		Menu menuFile = new Menu("�ļ�");
		MenuItem menuFileTick = new MenuItem("����");
		menuFileTick.addActionListener(this);
		menuFile.add(menuFileTick);
		menuFile.addSeparator();
		MenuItem menuFileStart = new MenuItem("����");
		menuFileStart.addActionListener(this);
		menuFile.add(menuFileStart);
		MenuItem menuFileStop = new MenuItem("ֹͣ");
		menuFileStop.addActionListener(this);
		menuFile.add(menuFileStop);
		menuFile.addSeparator();
		MenuItem menuFileExit = new MenuItem("�˳�");
		menuFileExit.addActionListener(this);
		menuFile.add(menuFileExit);
		meneBar.add(menuFile);

		Menu menuSet = new Menu("����");
		MenuItem menuSetParam = new MenuItem("����");
		menuSetParam.addActionListener(this);
		menuSet.add(menuSetParam);
		meneBar.add(menuSet);

		Menu menuHelp = new Menu("����");
		MenuItem menuHelpManu = new MenuItem("˵��");
		menuHelpManu.addActionListener(this);
		menuHelp.add(menuHelpManu);
		menuHelp.addSeparator();
		MenuItem menuHelpAbout = new MenuItem("����");
		menuHelpAbout.addActionListener(this);
		menuHelp.add(menuHelpAbout);
		meneBar.add(menuHelp);
		setMenuBar(meneBar);

		JToolBar jtb = new JToolBar();
		String[] s = { "����", "����", "����", "ֹͣ", "�˳�" };
		int size = s.length;
		JButton[] button = new JButton[size];
		for (int i = 0; i < size; i++) {
			button[i] = new JButton(s[i]);
			button[i].addActionListener(this);
			if ("����".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/param.png")));
			} else if ("����".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/tick.png")));
			} else if ("����".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/start.png")));
			} else if ("ֹͣ".equals(s[i])) {
				button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class, "images/stop.png")));
			} else if ("�˳�".equals(s[i])) {
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

		jTabbedPane.addTab("����", null, paramPanel, "�����������");
		jTabbedPane.addTab("����", null, tickPanel, "������ʾ���");
		jTabbedPane.addTab("��¼", null, orderPanel, "��¼��ʾ���");
		add(jTabbedPane, BorderLayout.CENTER);
		setTitle("�������ծ�ڻ��Զ��µ�����v0.1��֤�ʹ�");
		setSize(1024, 640);
		setLocation(180, 90);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int ret = JOptionPane.showConfirmDialog(null, "ȷ���˳��Զ��µ����ߣ�", "ȷ��", JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.OK_OPTION)
					System.exit(0);
			}
		});
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("����")) {
			jTabbedPane.setSelectedIndex(0);
		} else if (s.equals("����")) {
			jTabbedPane.setSelectedIndex(1);
			tickPanel.start();
		} else if (s.equals("����")) {
			jTabbedPane.setSelectedIndex(2);
			orderPanel.start();
		} else if (s.equals("ֹͣ")) {
			jTabbedPane.setSelectedIndex(2);
			orderPanel.stop();
		} else if (s.equals("�˳�")) {
			int ret = JOptionPane.showConfirmDialog(null, "ȷ���˳��Զ��µ����ߣ�", "ȷ��", JOptionPane.YES_NO_OPTION);
			if (ret == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
		} else if (s.equals("˵��")) {
			JOptionPane.showMessageDialog(this, "ʹ�÷�����\n" + "1�����ò���\n" + "2��������ģ���������\n" + "3������������Զ��µ�\n" + "4�����ֹͣ��ֹͣ�µ������µ����������");
		} else if (s.equals("����")) {
			JOptionPane.showMessageDialog(this, "�������ծ�ڻ��Զ��µ�����\n Version: 0.1\n Build id: 0.1-20151112\n (c) Copyright DFHAM, Ltd. 2015. All rights reserved.");
		}
	}

	public static void main(String[] args) {
		new TFOrder();
	}

}
