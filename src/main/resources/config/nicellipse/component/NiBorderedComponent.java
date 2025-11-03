package org.example.nicellipse.component;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public interface NiBorderedComponent extends NiBasicComponent {

	default Border defaultBorder() {
		return BorderFactory.createLineBorder(this.defaultBorderColor());
	}

	default void defaultSetup() {
		this.setBackground(this.defaultBackground());
		this.setBorder(this.defaultBorder());
		this.setLayout(null);
	}

	void setBorder(Border border);

	default Stroke defaultStroke() {
		return new BasicStroke(this.defaultBorderWidth());
	}

	default int defaultBorderWidth() {
		return 1;
	}

	Border getBorder();

	default Color defaultBorderColor() {
		return Color.black;
	}

}
