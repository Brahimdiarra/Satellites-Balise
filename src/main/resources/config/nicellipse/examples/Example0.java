package org.example.nicellipse.examples;

import org.example.nicellipse.component.NiLabel;
import org.example.nicellipse.component.NiSpace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Example0 {
	public static void main(String[] args) {
		JPanel space = new NiSpace("Empty Space", new Dimension(300, 200));
		JLabel label = new NiLabel("space " + space.getWidth() + "x" + space.getHeight() );
		label.setFont(new Font("Arial", Font.BOLD, 24));
		label.setForeground(Color.darkGray);
		label.setBackground(Color.yellow);
		space.add(label);

		JFrame frame = new JFrame("Empty Space");
		WindowAdapter wa = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(wa);
		frame.getContentPane().add(space);
		frame.pack();
		frame.setVisible(true);

	
	}
}
