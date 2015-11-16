package com.dfh.tforder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileSystemView;

import com.dfh.tforder.util.Utility;

/**
 * @author zhaoyang
 *
 */
@SuppressWarnings("serial")
public class TFOrder extends JFrame implements ActionListener {

	private JTabbedPane jTabbedPane = new JTabbedPane();

	public JTabbedPane getjTabbedPane() {
		return jTabbedPane;
	}

	public void setjTabbedPane(JTabbedPane jTabbedPane) {
		this.jTabbedPane = jTabbedPane;
	}

	public TFOrder() {
		init();
	}

//	  private String currentDirectory;
//
//	  public String getCurrentDirectory() {
//	    return currentDirectory;
//	  }
//
//	  public void setCurrentDirectory(String currentDirectory) {
//	    this.currentDirectory = currentDirectory;
//	  }


	  private void init() {
//	    Global.getInstance().setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
	    MenuBar meneBar = new MenuBar();
	    Menu menuFile = new Menu("�ļ�");
	    MenuItem menuFileMain = new MenuItem("��ҳ");
	    menuFileMain.addActionListener(this);
	    menuFile.add(menuFileMain);
	    menuFile.addSeparator();
	    MenuItem menuFileStart = new MenuItem("ִ��");
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
	    String[] s = { "��ҳ", "����", "ִ��", "ֹͣ", "�˳�" };
	    int size = s.length;
	    JButton[] button = new JButton[size];
	    for (int i = 0; i < size; i++) {
	      button[i] = new JButton(s[i]);
	      button[i].addActionListener(this);
	      if ("��ҳ".equals(s[i])) {
	    	  button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class,"images/main.png")));
	      } else
	      if ("����".equals(s[i])) {
	    	  button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class,"images/param.png")));
	      } else
	      if ("ִ��".equals(s[i])) {
	    	  button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class,"images/start.png")));
	      } else
	      if ("ֹͣ".equals(s[i])) {
	    	  button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class,"images/stop.png")));
	      } else
	      if ("�˳�".equals(s[i])) {
	    	  button[i].setIcon(new ImageIcon(Utility.getURL(TFOrder.class,"images/exit.png")));
	      }
//	      button[i].setIconTextGap(-20);
	      button[i].setHorizontalTextPosition(JButton.CENTER);
	      button[i].setVerticalTextPosition(JButton.BOTTOM);
	      button[i].setBorderPainted(false);
	      jtb.add(button[i]);
	    }
	    jtb.setFloatable(false);
	    add(jtb, BorderLayout.NORTH);
	    
	    JPanel mainPanel = new MainPanel();
	    jTabbedPane.addTab("��ҳ", null, mainPanel, "��ʾ���");
	    JPanel paramPanel = new ParamPanel();
	    jTabbedPane.addTab("����", null, paramPanel, "�����������");
	    add(jTabbedPane, BorderLayout.CENTER);
	    setTitle("�������ծ�ڻ��Զ��µ�����v0.1��֤�ʹ�");
	    setSize(800, 600);
	    setLocation(300, 100);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    setVisible(true);
	  }

	  public void actionPerformed(ActionEvent e) {
	    String s = e.getActionCommand();
	    if (s.equals("��ҳ")){
	    	jTabbedPane.setSelectedIndex(0);
	    } else
		if (s.equals("����")){
			jTabbedPane.setSelectedIndex(1);
		} else
	    if (s.equals("�˳�")) {
	      System.exit(0);
	    } else
	    if (s.equals("˵��")) {
	      JOptionPane.showMessageDialog(this, "ʹ�÷�����\n" + "1��XXX\n" + "2��XXX\n" + "3��XXX");
	    } else
	    if (s.equals("����")) {
	      JOptionPane.showMessageDialog(this, "�������ծ�ڻ��Զ��µ�����\n Version: 0.1\n Build id: 0.1-20151112\n (c) Copyright DFHAM, Ltd. 2015. All rights reserved.");
	    }
	  }

	  public static void main(String[] args) {
	    new TFOrder();
	  }

}
