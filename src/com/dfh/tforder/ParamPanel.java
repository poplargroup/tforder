package com.dfh.tforder;

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
		layout.setConstraints(liquidBadLabel, s);
		s.gridx = 1;
		s.gridy = 0;
		liquidBadField.setColumns(20);
		layout.setConstraints(liquidBadField, s);
		s.gridx = 0;
		s.gridy = 1;
		layout.setConstraints(liquidGoodLabel, s);
		s.gridx = 1;
		s.gridy = 1;
		liquidGoodField.setColumns(20);
		layout.setConstraints(liquidGoodField, s);
		s.gridx = 0;
		s.gridy = 2;
		layout.setConstraints(priceThresholdLabel, s);
		s.gridx = 1;
		s.gridy = 2;
		priceThresholdField.setColumns(20);
		layout.setConstraints(priceThresholdField, s);
		s.gridx = 0;
		s.gridy = 3;
		layout.setConstraints(lotThresholdLabel, s);
		s.gridx = 1;
		s.gridy = 3;
		lotThresholdField.setColumns(20);
		layout.setConstraints(lotThresholdField, s);
		s.gridx = 0;
		s.gridy = 4;
		layout.setConstraints(deltaPriceLabel, s);
		s.gridx = 1;
		s.gridy = 4;
		deltaPriceField.setColumns(20);
		layout.setConstraints(deltaPriceField, s);
		s.gridx = 1;
		s.gridy = 5;
		layout.setConstraints(buyBadSellGoodButton, s);
		s.gridx = 1;
		s.gridy = 6;
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
		prop.setProperty("liquidBad", liquidBadField.getText());
		prop.setProperty("liquidGood", liquidGoodField.getText());
		prop.setProperty("priceThreshold", priceThresholdField.getText());
		prop.setProperty("lotThreshold", lotThresholdField.getText());
		prop.setProperty("deltaPrice", deltaPriceField.getText());
		prop.setProperty("buyBadSellGood", buyBadSellGoodButton.isSelected() ? "1" : "0");
		prop.setProperty("sellBadBuyGood", sellBadBuyGoodButton.isSelected() ? "1" : "0");
		PropertyFactory.saveProperty();
	}

}
