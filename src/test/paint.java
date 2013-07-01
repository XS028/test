package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class paint {

	public JFrame viewForm;

	public static BufferedImage ind_ok = null;{
		try {
		ind_ok = ImageIO.read(new File("C:/test/ind_ok.png"));
	}catch(IOException e){
		exw.sh("Error reading icon image:C:/test/ind_ok.png");io.empty();
		}
	}

	public static BufferedImage ind_err = null;{
	try {
		ind_err = ImageIO.read(new File("C:/test/ind_err.png"));
	}catch(IOException e){
		exw.sh("Error reading icon image:C:/test/ind_err.png");io.empty();
		}
	}

	public static BufferedImage ind_wait = null;{
	try {
		ind_wait = ImageIO.read(new File("C:/test/ind_wait.png"));
	}catch(IOException e){
		exw.sh("Error reading icon image:C:/test/ind_wait.png");io.empty();
		}
	}

	public JTextArea text1 = new JTextArea(){
		private static final long serialVersionUID = 110765;
	};
	
	public JTextField text2 = new JTextField(){
		private static final long serialVersionUID = 346512;
	};

	public JTextArea text4 = new JTextArea(){
		private static final long serialVersionUID = 356192;
	};
	
	public JTextField text5 = new JTextField(){
		private static final long serialVersionUID = 953214;
	};
	
	public JTextField text7 = new JTextField(){
		private static final long serialVersionUID = 312465;
	};

	public static JButton button1 = new JButton("Close"){
		private static final long serialVersionUID = 356946;
	};

	public static JButton button2 = new JButton("Set buy"){
		private static final long serialVersionUID = 365489;
	};

	public static JButton button3 = new JButton("Read ff, MAU"){
		private static final long serialVersionUID = 136425;
	};

	public static JButton button4 = new JButton("Cancel all"){
		private static final long serialVersionUID = 963254;
	};

	public static Color green = new Color (209,240,117);
	public static Color white = new Color(255,255,255);
	public static Color red = new Color(248,163,167);
	public static Color gray = new Color(242,242,242);
	public static Color yellow = new Color(237,237,137);

	public paint() {
		viewForm = new JFrame("BTC-e helper");
		viewForm.setSize(425, 300);
		viewForm.setLocation(test.ww/2-viewForm.getSize().width/2,test.wh/2-viewForm.getSize().height/2);
		viewForm.setMinimumSize(viewForm.getSize());
		viewForm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		viewForm.setResizable(false);
		viewForm.getContentPane().setBackground(white);
		viewForm.setIconImage(ind_ok);

		////text1 pairs/prices
		text1.setVisible(true);
		text1.setLocation(180,10);
		text1.setSize(118,130);
		text1.setEditable(false);
		text1.setFont(new Font("Arial", 12, 12));
		text1.setRows(10);
		//text1.setBackground(green);

		//text2 - min price
		text2.setVisible(true);
		text2.setLocation(340,66);
		text2.setSize(70,22);
		text2.setHorizontalAlignment(JTextField.CENTER);
		text2.setFont(new Font("Arial", 14, 14));
		text2.setBackground(gray);
		//text2.setBackground(red);

		JTextField text3 = new JTextField("min:");
		text3.setVisible(true);
		text3.setLocation(300,66);
		text3.setSize(39,22);
		text3.setEditable(false);
		text3.setBackground(white);
		text3.setBorder(null);
		text3.setHorizontalAlignment(JTextField.CENTER);
		text3.setFont(new Font("Arial", 12, 12));
		//text3.setBackground(red);

		//text4 - operations info
		text4.setVisible(true);
		text4.setLocation(10,10);
		text4.setSize(160,250);
		text4.setEditable(false);
		text4.setFont(new Font("Arial", 12, 12));
		text4.setRows(10);
		//text4.setBackground(red);

		//text5 - steps
		text5.setVisible(true);
		text5.setLocation(340,98);
		text5.setSize(70,22);
		text5.setHorizontalAlignment(JTextField.CENTER);
		text5.setFont(new Font("Arial", 14, 14));
		text5.setBackground(gray);

		//text6 - steps note
		JTextField text6 = new JTextField("steps:");
		text6.setVisible(true);
		text6.setLocation(300,98);
		text6.setSize(39,22);
		text6.setEditable(false);
		text6.setBackground(white);
		text6.setBorder(null);
		text6.setHorizontalAlignment(JTextField.CENTER);
		text6.setFont(new Font("Arial", 12, 12));
		//text6.setBackground(red);
		
		//text7 - max price
		text7.setVisible(true);
		text7.setLocation(340,34);
		text7.setSize(70,22);
		text7.setHorizontalAlignment(JTextField.CENTER);
		text7.setFont(new Font("Arial", 14, 14));
		text7.setBackground(gray);
		//text7.setBackground(red);
		
		//text8 - max price note
		JTextField text8 = new JTextField("max:");
		text8.setVisible(true);
		text8.setLocation(300,34);
		text8.setSize(39,22);
		text8.setEditable(false);
		text8.setBackground(white);
		text8.setBorder(null);
		text8.setHorizontalAlignment(JTextField.CENTER);
		text8.setFont(new Font("Arial", 12, 12));
		//text8.setBackground(red);

		//button2 - Set buy
		button2.setVisible(true);
		button2.setLocation(180,150);
		button2.setSize(110, 50);
		button2.setBackground(white);
		button2.setFocusable(false);

		//button1 - CLOSE
		button1.setVisible(true);
		button1.setLocation(button2.getLocation().x+120,button2.getLocation().y+60);
		button1.setSize(110, 50);
		button1.setBackground(white);
		button1.setFocusable(false);

		//button3 - Read ff/MAU        
		button3.setVisible(true);
		button3.setLocation(button2.getLocation().x+120,button2.getLocation().y);
		button3.setSize(110, 50);
		button3.setBackground(white);
		button3.setFocusable(false);

		//button4 - Cancel all     
		button4.setVisible(true);
		button4.setLocation(button2.getLocation().x,button2.getLocation().y+60);
		button4.setSize(110, 50);
		button4.setBackground(white);
		button4.setFocusable(false);

		////OnClick Listeners
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(test.canclosenow==1){
					io.t();
				}else{
					test.fifo=4;
					viewForm.setVisible(false);
				}
			}  
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test.fifo==0&test.initializedonce==1){
					if (test.auen==0&minam.check()==1&(!(text2.getText().equals(""))&(!(text5.getText().equals("")))&(!(text7.getText().equals(""))))){
						int where = 1;
						try {
							test.min = Double.parseDouble(text2.getText());
							where = 2;
							test.steps = Integer.parseInt(text5.getText());
							where = 3;
							test.max = Double.parseDouble(text7.getText());
							text2.setBackground(gray);
							text5.setBackground(gray);
							text7.setBackground(gray);
							button2.setBackground(green);
							test.fifo=1;
						}catch (Exception e2){
							switch(where){
							case 1:{text2.setBackground(red);break;}
							case 2:{text5.setBackground(red);break;}
							case 3:{text7.setBackground(red);break;}
							}
						}
					}else if (test.auen == 1){button2.setBackground(red);}else{
						if(text2.getText().equals("")){text2.setBackground(red);}
						if(text5.getText().equals("")){text5.setBackground(red);}
						if(text7.getText().equals("")){text7.setBackground(red);}
					}
				}
			}
		});

		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test.fifo==0&test.initializedonce==1){
					if (test.auen==0){
						button2.setBackground(white);
						button3.setBackground(green);
						text2.setBackground(gray);
						text5.setBackground(gray);
						text7.setBackground(gray);
						test.fifo=2;
					}else{button3.setBackground(red);}
				}
			}
		});

		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test.fifo==0&test.initializedonce==1){
					button4.setBackground(yellow);
					test.fifo=3;
				}
			}
		});

		viewForm.getContentPane().add(text1);
		viewForm.getContentPane().add(text2);
		viewForm.getContentPane().add(text3);
		viewForm.getContentPane().add(text4);
		viewForm.getContentPane().add(text5);
		viewForm.getContentPane().add(text6);
		viewForm.getContentPane().add(text7);
		viewForm.getContentPane().add(text8);
		viewForm.getContentPane().add(button1);
		viewForm.getContentPane().add(button2);
		viewForm.getContentPane().add(button3);
		viewForm.getContentPane().add(button4);
		viewForm.getContentPane().add(new JLabel());
		viewForm.setVisible(true);
	}

}//