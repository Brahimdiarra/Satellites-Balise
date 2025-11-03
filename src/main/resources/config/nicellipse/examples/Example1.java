package org.example.nicellipse.examples;

import org.example.nicellipse.component.NiEllipse;
import org.example.nicellipse.component.NiImage;
import org.example.nicellipse.component.NiSpace;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Example1 {
	public static void main(String[] args) throws IOException {
		NiSpace space = new NiSpace("Space with circles", new Dimension(400, 400));
				
		NiEllipse circle = new NiEllipse();
		circle.setBounds(0, 0, 300, 300);
		space.add(circle);
		
		NiEllipse inner = new NiEllipse();
		inner.setSize(100, 100);
		inner.setCenter(circle.getCenter());
		inner.setBackground(Color.yellow);
		circle.add(inner);
		
		NiImage image = new NiImage(new File("I_love_Brest_city.jpg"));
		image.setCenter(circle.getCenter());
		circle.add(image);
		
		
		space.setBackground(Color.blue);
		space.setOpaque(false);
		space.openInWindow();
		
		circle.setCenter(space.getCenter());
	}

}
