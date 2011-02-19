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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class ThingWorldController extends JFrame implements Constance, Runnable, MouseInputListener, KeyListener {

	public JPanel helppanel;
	public JComponent cp;	
	public ThingWorldModel twModel;
	public ThingWorldView twView;
	private ShapeControls shapecontrols1;
	private boolean running = true, resetnow=false;
	private PopupMenuHandler popupmenu;
	private ThingWorldControlPanel twcp; 

	private Thing lastclickedthing = null;
	//private JMenu menudropdown = new JMenu();

	public ThingWorldController(){
		resetnow = true;

		while (true)
		{		
			if (resetnow) {
				Thread.yield();
				resetnow = false;
				this.resetWorld();
				new Thread(this).start() ;
			}
			try {
				Thread.sleep(900);
			} catch(Exception e) {}
		}
	}

	public void init(){
		twModel = new ThingWorldModel(this);
		twView = new ThingWorldView(twModel,this);
		shapecontrols1  = new ShapeControls(this,twView); 
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(winx,winy);
		this.setTitle(mainwintitle);
		this.setResizable(true);
		this.requestFocusInWindow(true);
		this.setVisible(true);
		this.setEnabled(true);
		
		helppanel = new JPanel();
		helppanel.setLocation(5,5);
		helppanel.add(new JLabel("arrows = move, CTRL = jump, x = projectile. Right click for menu. Left click shape to change color.  "));
		helppanel.validate();
		
		cp = (JComponent) this.getContentPane();
		cp.removeAll();
		cp.setBackground(colorbackground);
		cp.addKeyListener(this);
		cp.setOpaque(true);
		cp.setLayout(new BorderLayout());
		cp.add(twView);
		cp.add(shapecontrols1,BorderLayout.SOUTH);
		shapecontrols1.requestFocus();

		cp.add(helppanel,BorderLayout.NORTH);
		
		cp.setComponentZOrder(twView,0);
		cp.setComponentZOrder(shapecontrols1, 1);
		
		popupmenu = new PopupMenuHandler(this, twView, twModel);
		cp.validate();
		this.addMouseListener(popupmenu);
		this.add(popupmenu);
		cp.setComponentZOrder(popupmenu, 2);
		cp.validate();
		this.validate();
		
		/*
		 * This following kluge (I need to popupmenu.show(this,1,1); the popupmenu as called by the ThingWorldController) 
		 * is necessary, 
		 * or otherwise the popup assumes the size of the entire twView area. After a right click on the shapecontrols
		 * region the popup is suddenly the correct size, etc. This is so bogus I can't stand it. There is an explicit
		 * reference inside the popupmenuhandler back to the calling component (this), which it says is invalid.
		 * however, I validate the entire thing before the call.. 
		 */
		popupmenu.show(this,1,1);
		popupmenu.setVisible(false);
		//popupmenu.setOpaque(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		twcp = new ThingWorldControlPanel(this, twView, twModel);
		twcp.setLocation(new Point((int) this.getLocation().getX()+600,(int)this.getLocation().getY()+700)  );
		twcp.setVisible(false);
		twcp.setSize(new Dimension(500,500));
		setFocusable(true);
		requestFocus();
	}
	
	public ThingWorldModel getThingWorld(){return twModel;};
	
	private void resetWorld()
	{
		System.out.println("resetWorld() has been called ... ");

		this.init();
	}


	
	@Override
	public void run() 
	{
		long starttime = System.nanoTime();
		long fps = 30; //"ticks" per second
		long elapsed = 0;
		long nanosperframe =(long) (1000000000/fps);

		//running = true;
		resetnow = false;
	
		while (!resetnow)
		{
			if (running)
			{
				elapsed =  (System.nanoTime() - starttime);
				if (elapsed > nanosperframe) 
					{
					starttime = System.nanoTime();
					//System.out.println("running == true");

					twView.repaint();
					Thread.yield();
					twModel.update();
					}
				try {
					//Thread.yield();
					//Thread.sleep(5) looks unnecessary, but it is. the sim runs much jerkier without it. 
					//must be the other threads need some time, otherwise the O/S has to preempt
					//the VM in order to do system tasks, which probably isn't as smooth as 
					//voluntarily allowing this to happen. Just a theory. 
					Thread.sleep(10);
					}
				catch(Exception e ){}	
	
			} else {
				try{
					Thread.sleep(300);
				} catch(Exception e){}
				
			}


		} 
		System.out.println("Running Thread is stopping now ... ");
	}

	public void pauseresume(){
		running = !running;
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = twView.viewsquare.txscreen2world( new Point (e.getX(),e.getY()) );
		this.setTitle("Mouse Screen: "+e.getX()+","+e.getY()+" World: "+p.x+","+p.y);
//		System.out.println("mouse click -- "+e.getX()+","+e.getY());
		lastclickedthing = this.twView.getThingAt(e.getX(),e.getY());
		if (e.getButton()== MouseEvent.BUTTON1) 
		{
			if ( lastclickedthing != null ) 
			{
				Color cc = lastclickedthing.getPolyin();
				lastclickedthing.setPolyin(new Color (rand.nextInt(256),rand.nextInt(256),rand.nextInt(256) ) );
				//this.repaint();
				try{Thread.sleep(5);} catch(Exception e2){} finally {}
				lastclickedthing =null;
			} 
			
		} else
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			if (lastclickedthing !=null )
			{
				System.out.println("ThingWorldController gets a button3 click, and lastclickedthing is not null");
				this.pauseresume();
				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouse released");
		if (e.getButton() == MouseEvent.BUTTON3){
			System.out.println("right mouse button released");
		//	this.popupmenu.show(this, e.getX(), e.getY());
		//	this.validate();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("mouse drag in progress "+arg0.getX()+","+arg0.getY());
		Thing t = this.twView.getThingAt(arg0.getX(),arg0.getY());
		if ( t != null ) 
		{
			Point p = twView.viewsquare.txscreen2world(new Point( arg0.getX(),arg0.getY() ) );
			
			t.putatpos(p.x,p.y );
		} 
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		//twView.getGraphics().setColor(Color.WHITE);
		//twView.getGraphics().drawString(" "+arg0.getX()+","+arg0.getY(), arg0.getX(),arg0.getY());
		this.setTitle("Mouse at: "+arg0.getX()+","+arg0.getY());
	}


	public void addshape(){
		twModel.addshape();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
//		int kc=e.getKeyCode();
//		System.out.println("Key-appframe");
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			KB.keypressed(KEY_UP);
			break;
		case KeyEvent.VK_DOWN: 
			KB.keypressed(KEY_DOWN);			
			break;
		case KeyEvent.VK_LEFT: 
			KB.keypressed(KEY_LEFT);			
			break;
		case KeyEvent.VK_RIGHT: 
			KB.keypressed(KEY_RIGHT);			
			break;
		case KeyEvent.VK_CONTROL: 			
			KB.keypressed(KEY_JUMP);			
			break;
		case KeyEvent.VK_X: 
			KB.keypressed(KEY_ACT);			
			break;
		case KeyEvent.VK_C: 
			KB.keypressed(KEY_HOOK);			
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//int kc=e.getKeyCode();
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			KB.keyreleased(KEY_UP);
			break;
		case KeyEvent.VK_DOWN: 
			KB.keyreleased(KEY_DOWN);			
			break;
		case KeyEvent.VK_LEFT: 
			KB.keyreleased(KEY_LEFT);			
			break;
		case KeyEvent.VK_RIGHT: 
			KB.keyreleased(KEY_RIGHT);			
			break;
		case KeyEvent.VK_CONTROL: 
			KB.keyreleased(KEY_JUMP);			
			break;
		case KeyEvent.VK_X: 
			KB.keyreleased(KEY_ACT);			
			break;
		case KeyEvent.VK_C: 
			KB.keyreleased(KEY_HOOK);			
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Key-appframe");	
	}

	public void resetself() {
		resetnow=true;
		//this.init();
	}
	public boolean getpaused() {return !running;}
	
	public Thing getlastclickedthing () {return lastclickedthing;}

	public void setlastclickedthing(Thing t) {
		this.lastclickedthing = t;
	}
}
