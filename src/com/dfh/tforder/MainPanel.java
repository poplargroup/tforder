package com.dfh.tforder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author zhaoyang
 *
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener {

	public MainPanel() {
		init();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	    JButton outFileChooseButton = new JButton("Ö´ÐÐ");
	    outFileChooseButton.addActionListener(this);
	    add(outFileChooseButton);
		

	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
	}
	
	public void start(){
		
	}

	public void stop(){
		
	}
	
}
