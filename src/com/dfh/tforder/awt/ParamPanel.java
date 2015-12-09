package com.dfh.tforder.awt;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.dfh.tforder.order.OrderFeed;
import com.dfh.tforder.util.PropertyFactory;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class ParamPanel extends JPanel implements ActionListener {

	private JTextField hostField = new JTextField();
	private JTextField portField = new JTextField();
	private JTextField pathField = new JTextField();
	private JTextField accountNoField = new JTextField();
	private JTextField combinationNoField = new JTextField();
	private JTextField seatNoField = new JTextField();
	private JTextField liquidBadField = new JTextField();
	private JTextField liquidGoodField = new JTextField();
	private JTextField priceThresholdField = new JTextField();
	private JTextField lotThresholdField = new JTextField();
	private JTextField deltaPriceField = new JTextField();
	private JRadioButton buyBadSellGoodButton = new JRadioButton("买入流动差的卖出流动好的");
	private JRadioButton sellBadBuyGoodButton = new JRadioButton("卖出流动差的买入流动好的");
	Properties prop;

	public ParamPanel() {
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel setPanel = new JPanel();
		add(setPanel, BorderLayout.CENTER);

		JLabel hostLabel = new JLabel("行情订阅ip");
		setPanel.add(hostLabel);
		setPanel.add(hostField);
		JLabel portLabel = new JLabel("行情订阅port");
		setPanel.add(portLabel);
		setPanel.add(portField);
		JLabel pathLabel = new JLabel("交互文件路径");
		setPanel.add(pathLabel);
		setPanel.add(pathField);
		JLabel accountNoLabel = new JLabel("AccountNo");
		setPanel.add(accountNoLabel);
		setPanel.add(accountNoField);
		JLabel combinationNoLabel = new JLabel("CombinationNo");
		setPanel.add(combinationNoLabel);
		setPanel.add(combinationNoField);
		JLabel seatNoLabel = new JLabel("SeatNo");
		setPanel.add(seatNoLabel);
		setPanel.add(seatNoField);
		JLabel liquidBadLabel = new JLabel("流动性不好的产品");
		setPanel.add(liquidBadLabel);
		setPanel.add(liquidBadField);
		JLabel liquidGoodLabel = new JLabel("流动性好的产品");
		setPanel.add(liquidGoodLabel);
		setPanel.add(liquidGoodField);
		JLabel priceThresholdLabel = new JLabel("价差阀值");
		setPanel.add(priceThresholdLabel);
		setPanel.add(priceThresholdField);
		JLabel lotThresholdLabel = new JLabel("每单最大手数");
		setPanel.add(lotThresholdLabel);
		setPanel.add(lotThresholdField);
		JLabel deltaPriceLabel = new JLabel("报价变动单位");
		setPanel.add(deltaPriceLabel);
		setPanel.add(deltaPriceField);
		ButtonGroup group = new ButtonGroup();
		group.add(buyBadSellGoodButton);
		group.add(sellBadBuyGoodButton);
		setPanel.add(buyBadSellGoodButton);
		setPanel.add(sellBadBuyGoodButton);

		GridBagLayout layout = new GridBagLayout();
		setPanel.setLayout(layout);
		GridBagConstraints s = new GridBagConstraints();// 定义一个GridBagConstraints，是用来控制添加进的组件的显示位置
		s.gridx = 0;
		s.gridy = 0;
		layout.setConstraints(hostLabel, s);
		s.gridx = 1;
		s.gridy = 0;
		hostField.setColumns(20);
		layout.setConstraints(hostField, s);
		s.gridx = 0;
		s.gridy = 1;
		layout.setConstraints(portLabel, s);
		s.gridx = 1;
		s.gridy = 1;
		portField.setColumns(20);
		layout.setConstraints(portField, s);
		s.gridx = 0;
		s.gridy = 2;
		layout.setConstraints(pathLabel, s);
		s.gridx = 1;
		s.gridy = 2;
		pathField.setColumns(20);
		layout.setConstraints(pathField, s);
		s.gridx = 0;
		s.gridy = 3;
		layout.setConstraints(accountNoLabel, s);
		s.gridx = 1;
		s.gridy = 3;
		accountNoField.setColumns(20);
		layout.setConstraints(accountNoField, s);
		s.gridx = 0;
		s.gridy = 4;
		layout.setConstraints(combinationNoLabel, s);
		s.gridx = 1;
		s.gridy = 4;
		combinationNoField.setColumns(20);
		layout.setConstraints(combinationNoField, s);
		s.gridx = 0;
		s.gridy = 5;
		layout.setConstraints(seatNoLabel, s);
		s.gridx = 1;
		s.gridy = 5;
		seatNoField.setColumns(20);
		layout.setConstraints(seatNoField, s);
		s.gridx = 0;
		s.gridy = 6;
		layout.setConstraints(liquidBadLabel, s);
		s.gridx = 1;
		s.gridy = 6;
		liquidBadField.setColumns(20);
		layout.setConstraints(liquidBadField, s);
		s.gridx = 0;
		s.gridy = 7;
		layout.setConstraints(liquidGoodLabel, s);
		s.gridx = 1;
		s.gridy = 7;
		liquidGoodField.setColumns(20);
		layout.setConstraints(liquidGoodField, s);
		s.gridx = 0;
		s.gridy = 8;
		layout.setConstraints(priceThresholdLabel, s);
		s.gridx = 1;
		s.gridy = 8;
		priceThresholdField.setColumns(20);
		layout.setConstraints(priceThresholdField, s);
		s.gridx = 0;
		s.gridy = 9;
		layout.setConstraints(lotThresholdLabel, s);
		s.gridx = 1;
		s.gridy = 9;
		lotThresholdField.setColumns(20);
		layout.setConstraints(lotThresholdField, s);
		s.gridx = 0;
		s.gridy = 10;
		layout.setConstraints(deltaPriceLabel, s);
		s.gridx = 1;
		s.gridy = 10;
		deltaPriceField.setColumns(20);
		layout.setConstraints(deltaPriceField, s);
		s.gridx = 1;
		s.gridy = 11;
		layout.setConstraints(buyBadSellGoodButton, s);
		s.gridx = 1;
		s.gridy = 12;
		layout.setConstraints(sellBadBuyGoodButton, s);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		add(buttonPanel, BorderLayout.SOUTH);
		JButton clearButton = new JButton("清空");
		clearButton.addActionListener(this);
		buttonPanel.add(clearButton);
		JButton reloadButton = new JButton("重载");
		reloadButton.addActionListener(this);
		buttonPanel.add(reloadButton);
		JButton saveButton = new JButton("保存");
		saveButton.addActionListener(this);
		buttonPanel.add(saveButton);

		update();
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("清空")) {
			hostField.setText("");
			portField.setText("");
			pathField.setText("");
			accountNoField.setText("");
			combinationNoField.setText("");
			seatNoField.setText("");
			liquidBadField.setText("");
			liquidGoodField.setText("");
			priceThresholdField.setText("");
			lotThresholdField.setText("");
			deltaPriceField.setText("");
		} else if (s.equals("重载")) {
			update();
		} else if (s.equals("保存")) {
			boolean started = OrderFeed.getStarted();
			if (started) {
				JOptionPane.showMessageDialog(this, "运行过程中不能修改参数");
				update();
				return;
			}
			upload();
		}
	}

	public void update() {
		prop = PropertyFactory.getProperties();

		hostField.setText(prop.getProperty("host"));
		portField.setText(prop.getProperty("port"));
		pathField.setText(prop.getProperty("path"));
		accountNoField.setText(prop.getProperty("accountNo"));
		combinationNoField.setText(prop.getProperty("combinationNo"));
		seatNoField.setText(prop.getProperty("seatNo"));
		liquidBadField.setText(prop.getProperty("liquidBad"));
		liquidGoodField.setText(prop.getProperty("liquidGood"));
		priceThresholdField.setText(prop.getProperty("priceThreshold"));
		lotThresholdField.setText(prop.getProperty("lotThreshold"));
		deltaPriceField.setText(prop.getProperty("deltaPrice"));
		buyBadSellGoodButton.setSelected(prop.getProperty("buyBadSellGood").equals("1"));
		sellBadBuyGoodButton.setSelected(prop.getProperty("sellBadBuyGood").equals("1"));
	}

	public void upload() {
		if (new Float(lotThresholdField.getText()) <= 0) {
			JOptionPane.showMessageDialog(this, "每单最大手数要大于0");
			return;
		}
		prop.setProperty("host", hostField.getText());
		prop.setProperty("port", portField.getText());
		String path = pathField.getText();
		if (!path.endsWith("\\")) {
			path = path + "\\";
		}
		prop.setProperty("path", path);
		prop.setProperty("accountNo", accountNoField.getText());
		prop.setProperty("combinationNo", combinationNoField.getText());
		prop.setProperty("seatNo", seatNoField.getText());
		prop.setProperty("liquidBad", liquidBadField.getText());
		prop.setProperty("liquidGood", liquidGoodField.getText());
		prop.setProperty("priceThreshold", priceThresholdField.getText());
		prop.setProperty("lotThreshold", lotThresholdField.getText());
		prop.setProperty("deltaPrice", deltaPriceField.getText());
		prop.setProperty("buyBadSellGood", buyBadSellGoodButton.isSelected() ? "1" : "0");
		prop.setProperty("sellBadBuyGood", sellBadBuyGoodButton.isSelected() ? "1" : "0");
		PropertyFactory.saveProperty();

		update();
	}

}
