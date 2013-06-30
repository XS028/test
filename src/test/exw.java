package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class exw{
	
	public JFrame viewForm;
	public static Color white = new Color(255,255,255);
	public static Color red = new Color(237,28,36);
	public static Color green = new Color(34,177,76);
	public static JTextArea text = new JTextArea();

	public exw() {
		viewForm = new JFrame("Error!");
		viewForm.setSize(400, 130);
		viewForm.setLocation(test.ww/2-viewForm.getSize().width/2,test.wh/2-viewForm.getSize().height/2);
		viewForm.setMinimumSize(viewForm.getSize());
		viewForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		viewForm.setResizable(false);
		viewForm.getContentPane().setBackground(white);
		viewForm.setIconImage(paint.ind_err);

		text.setVisible(true);
		text.setLocation(10,10);
		text.setSize(viewForm.getSize().width-30,viewForm.getSize().height-90);
		text.setEditable(false);
		text.setFont(new Font("Arial", 12, 12));
		text.setRows(10);
		text.setForeground(red);
		//text.setBackground(red);

		JButton button = new JButton("OK");
		button.setVisible(true);
		button.setLocation(viewForm.getSize().width-95,viewForm.getSize().height-75);
		button.setSize(70, 30);
		button.setBackground(white);
		button.setFocusable(false);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				io.t();
			}  
		});

		viewForm.getContentPane().add(text);
		viewForm.getContentPane().add(button);
		viewForm.getContentPane().add(new JLabel());
		viewForm.setVisible(false);
		}

		public static void sh(String what){
			test.ggh.viewForm.setVisible(false);
			text.append(what);
			test.ex_w.viewForm.setVisible(true);
		}
	
}