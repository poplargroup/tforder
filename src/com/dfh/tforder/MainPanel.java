package com.dfh.tforder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author zhaoyang
 * 
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener {

	private JTextArea consoleArea = new JTextArea();

	public MainPanel() {
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

		JButton clearButton = new JButton("���");
		clearButton.addActionListener(this);
		add(clearButton, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if (s.equals("���")) {
			consoleArea.setText("");
		}
	}

	public void start() {
		consoleArea.append("startted!\n");
	}

	public void stop() {
		consoleArea.append("stopped!\n");

	}

}
