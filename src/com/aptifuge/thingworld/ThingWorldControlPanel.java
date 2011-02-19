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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;


public class ThingWorldControlPanel extends JPanel implements MouseInputListener {

	JFrame twcpframe = new JFrame("ThingWorld Control Panel");
	JButton jb1 = new JButton("space button");
	JButton jbok = new JButton("Okay");
	JButton jbcancel = new JButton ("Cancel");
	JPanel jpphysics = new JPanel();
	JPanel jpokcancel = new JPanel();
	JSlider js_grav = new JSlider(JSlider.HORIZONTAL,0,100,1 ); 
	ThingWorldController twC;ThingWorldView twV; ThingWorldModel twM;
	
	
	public ThingWorldControlPanel(ThingWorldController twC,ThingWorldView twV,ThingWorldModel twM)
	{
		this.twC=twC; this.twV = twV; this.twM=twM;
		twcpframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		twcpframe.setPreferredSize(new Dimension(400,300));
		twcpframe.setResizable(false);
		twcpframe.setSize(new Dimension(400,300));
		twcpframe.setAlwaysOnTop(true);
	
		this.setOpaque(true);
		this.setLayout(new FlowLayout());
		this.setLayout(new GridLayout(0,1));
		
		jpphysics.setBorder(BorderFactory.createTitledBorder("Physics"));

		js_grav.addChangeListener(new js_gravlistener() );

		jpphysics.add(js_grav);
		
		this.add(jpphysics);
		jpokcancel.add(jbok);
		jpokcancel.add(jbcancel);
	
		this.add(jpokcancel);
		this.validate();
		twcpframe.setContentPane(this);
		this.setVisible(true);
		twcpframe.doLayout();
		twcpframe.validate();
		twcpframe.setVisible(false);
		jb1.addMouseListener(new MouseAdapter() {});
	}

	public class js_gravlistener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			twM.setGravity_current(js_grav.getValue()/js_grav.getMaximum());
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}
