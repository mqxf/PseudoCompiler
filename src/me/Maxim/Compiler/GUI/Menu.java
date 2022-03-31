package me.Maxim.Compiler.GUI;

import javax.swing.JFrame;
import java.awt.*;

public class Menu extends JFrame {

	public Menu() {
		setMaximumSize(new Dimension(430, 325));
		setPreferredSize(new Dimension(430, 325));
		Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d1.width - (getSize()).width) / 2, (d1.height - (getSize()).height) / 2);
	}

}
