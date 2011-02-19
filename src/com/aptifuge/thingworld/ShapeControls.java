/*
 *	 	The MIT License 
 * 
 *		Copyright (c) <2010,2011> <David R. Heitzman > 
 *
 * 		Permission is hereby granted, free of charge, to any person obtaining a copy
 * 		of this software and associated documentation files (the "Software"), to deal
 * 		in the Software without restriction, including without limitation the rights
 * 		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 		copies of the Software, and to permit persons to whom the Software is
 * 		furnished to do so, subject to the following conditions:
 * 		
 * 		Permission is hereby granted, free of charge, to any person obtaining a copy
 * 		of this software and associated documentation files (the "Software"), to deal
 * 		in the Software without restriction, including without limitation the rights
 * 		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 		copies of the Software, and to permit persons to whom the Software is
 * 		furnished to do so, subject to the following conditions:
 * 		
 * 		The above copyright notice and this permission notice shall be included in
 * 		all copies or substantial portions of the Software.
 * 		
 * 		THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 		IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * 		FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * 		AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * 		LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * 		OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * 		THE SOFTWARE.
 * 		
 */

package com.aptifuge.thingworld;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.event.*;

public class ShapeControls extends JPanel implements  Constance, KeyListener {
	ThingWorldController twC;
	ThingWorldView twV;
	JPanel jp1 = new JPanel();
	JButton button1 = new JButton(" Pause ");
	JButton button2 = new JButton("Add Line");
	JButton button3 = new JButton("Add Thing");
	JButton button4 = new JButton("Reset");
	JButton button5 = new JButton("Control Panel");

	public ShapeControls(ThingWorldController af, ThingWorldView sarea){
		//constructor
		twC = af;
		this.twV=sarea;
		this.setLayout(new GridLayout());
		this.add(jp1);
		jp1.setBackground(new Color(30,50,30));
		if (twC.getpaused() ) {
			button1.setText("Resume");
		} else {
			button1.setText(" Pause ");
		}

		jp1.add(button1);
		jp1.add(button2);
		jp1.add(button3);
		jp1.add(button4);
		jp1.add(button5);
		this.setBackground(Color.green);
		this.setVisible(true);

		addKeyListener(twC);

		button1.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent me){
				pauseresumeclick();
				
				if (button1.getText()==" Pause ") {button1.setText("Resume");} 
				else {button1.setText(" Pause ");}
				twC.requestFocus();
			}
		});

		button2.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent me){
				addshape();
				twC.requestFocus();
			}
		});

		button3.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent me){
				twC.addshape();
				twC.requestFocus();
			}
		});
		button4.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent me){
				twC.resetself();
				twC.requestFocus();
			}
		});
		button5.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent me){
				ThingWorldControlPanel twcp= new ThingWorldControlPanel(twC,twV,twC.twModel);
				twcp.setVisible(true);
			}
		});
	}
	
	public void pauseresumeclick(){
		
		twC.pauseresume();
	}
	public void addshape(){
		twV.addshape();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
