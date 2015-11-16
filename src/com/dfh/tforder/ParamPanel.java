package com.dfh.tforder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class ParamPanel extends JPanel implements ActionListener {

	private JTextField liquidBadField = new JTextField();
	private JTextField liquidGoodField = new JTextField();
	private JTextField priceThresholdField = new JTextField();
	private JCheckBox buyBadSellGoodBox = new JCheckBox();
	private JCheckBox sellBadBuyGoodBox = new JCheckBox();

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
		JLabel buyBadSellGoodLabel = new JLabel("买入流动差的卖出流动好的");
		setPanel.add(buyBadSellGoodLabel);
		setPanel.add(buyBadSellGoodBox);
		JLabel sellBadBuyGoodLabel = new JLabel("卖出流动差的买入流动好的");
		setPanel.add(sellBadBuyGoodLabel);
		setPanel.add(sellBadBuyGoodBox);

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
		layout.setConstraints(buyBadSellGoodLabel, s);
		s.gridx = 1;
		s.gridy = 3;
		layout.setConstraints(buyBadSellGoodBox, s);
		s.gridx = 0;
		s.gridy = 4;
		layout.setConstraints(sellBadBuyGoodLabel, s);
		s.gridx = 1;
		s.gridy = 4;
		layout.setConstraints(sellBadBuyGoodBox, s);

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
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
//		if (s.equals("清空")) {
//			liquidBadField
//		} else if (s.equals("重载")) {
//
//		} else if (s.equals("保存")) {
//
//		}

	}

}
