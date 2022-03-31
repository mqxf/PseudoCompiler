package me.Maxim.Compiler.GUI;

import me.Maxim.Compiler.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {

	private JButton compile;
	private JButton exitb;
	private JTextArea compileText;
	private JTextArea output;
	private JLabel instructions;
	private JPanel topPanel;
	private JPanel mainPanel;
	private JScrollPane out;
	private JScrollPane in;

	public Menu() {
		initiate();
	}

	private void compile() {
		String compiled = Main.compile(compileText.getText());
		//output.setText("<html>" + compiled.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>").replaceAll("\t", "&emsp;") + "</html>");
		output.setText(compiled.replaceAll("\t","        "));
	}

	private void exit() {
		dispose();
		System.exit(0);
	}

	private void initiate() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMaximumSize(new Dimension(1920, 1080));
		setMinimumSize(new Dimension(1280, 720));
		setPreferredSize(new Dimension(1440, 900));
		Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d1.width - (getSize()).width) / 2, (d1.height - (getSize()).height) / 2);

		topPanel = new JPanel();
		topPanel.setMaximumSize(new Dimension(1920, 100));
		topPanel.setMinimumSize(new Dimension(1280, 100));
		topPanel.setPreferredSize(new Dimension(1440, 100));
		topPanel.setOpaque(false);

		mainPanel = new JPanel();
		mainPanel.setMaximumSize(new Dimension(1920, 980));
		mainPanel.setMinimumSize(new Dimension(1280, 620));
		mainPanel.setPreferredSize(new Dimension(1440, 800));
		mainPanel.setOpaque(false);

		instructions = new JLabel();
		instructions.setText("Use the text box below to enter pseudocode. Don't use more than 4 spaces for a tab. Then click compile to compile the code to java.");
		instructions.setSize(400, 150);
		instructions.setVisible(true);
		topPanel.add(instructions);

		compileText = new JTextArea(35, 40);
		compileText.setSize(800, 400);
		compileText.setVisible(true);
		in = new JScrollPane(compileText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(in);

		compile = new JButton();
		compile.setText("Compile");
		compile.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				compile();
			}
		});
		compile.setSize(100, 50);
		compile.setVisible(true);
		mainPanel.add(compile);

		output = new JTextArea(35, 40);
		output.setEditable(false);
		output.setBackground(null);
		output.setBorder(null);
		output.setVisible(true);
		output.setText("Output will he here.");
		output.setSize(800, 400);

		exitb = new JButton();
		exitb.setText("Exit");
		exitb.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e) {
				exit();
			}
		});
		exitb.setSize(100, 50);
		exitb.setVisible(true);
		mainPanel.add(exitb);
		out = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(out);

		add(topPanel);
		topPanel.setBounds(0, 0, 1920, 100);
		add(mainPanel);
		mainPanel.setBounds(0, 500, 1920, 980);
	}

}
