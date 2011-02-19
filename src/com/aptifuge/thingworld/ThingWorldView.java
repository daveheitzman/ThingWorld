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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import com.sun.corba.se.impl.protocol.giopmsgheaders.KeyAddr;

public class ThingWorldView extends JPanel implements Constance, ActionListener, MouseInputListener {

	private static final long serialVersionUID = -2164771695292814844L;

	long displaytime1=0,displaytime2=0,eetime1=0,eetime2=0;;
	double dtimeavg=0.0,eetimeavg=0.0;

	private Thing tt = null;
	
	double numseen=0,numchecked=0;
	ThingWorldModel twM;
	Trig trig=new Trig();
	ThingWorldController twC;
	Rectangle borderbox = new Rectangle();
	Line line1;
	ArrayList<Line> allshapes = new ArrayList<Line>();
	Color tempcolor = new Color (0,0,0);
	Thing tempthing;
	Polygon outlinestroke = new Polygon();
	Polygon p = new Polygon();
	Graphics2D g2d;
	public ViewSquare viewsquare = new ViewSquare();
	private BasicStroke stroke = new BasicStroke(2);	

	int keystates[];
	int numshapes, maxshapes=5000;
	float oldovalx,oldovaly,ovalx,ovaly;
	float oxa=1.7f,oya=1.5f;
	int linelength = 25, lineposx = 5, linepoy=5, lineangle;
	boolean collisionson=false;
	boolean bounce = true;
	
	//debugging on-screen info
	private boolean numberson = false;
	private boolean identnumberson=false;
	private boolean directionflags=false;
	private boolean showcollisionspan=false;
	private boolean statusdump=false;
	private boolean surroundbox=false;
	
	public ThingWorldView(ThingWorldModel twM, ThingWorldController twc){
		//constructor
		this.twM = twM;
		this.twC = twc;
		ovalx=(int)(winx/2);
		ovaly=(int)(winy/2);
		this.setEnabled(true);
		this.setSize(worldpanelx,worldpanely);
		
		viewsquare = new ViewSquare(twM.thman);
		tempthing = new Thing(twM);
		eetime1 = System.nanoTime(); eetime2 = System.nanoTime();
	};

	public void quickredraw(){
		Rectangle r = new Rectangle();
		this.repaint(r);
	}
	
	public void paint(Graphics g){
		eetime2=System.nanoTime();
		//eetime is "everything else" time
		eetimeavg =  eetimeavg*0.99+0.01*(eetime2-eetime1) ;
		displaytime1=System.nanoTime();
		
		g2d = (Graphics2D)g;
		g2d.setBackground(colorbackground);
		g2d.clearRect(0, 0, worldpanelx,worldpanely);
		oldovalx = ovalx; oldovaly = ovaly;
		ovalx += oxa; ovaly += oya;
		borderbox.x = 2;borderbox.y = 2;borderbox.width = this.getWidth()-3;borderbox.height=this.getHeight()-3;		
		//draw ovals
		if (bounce){
			if ((ovalx+ovalwidth)>=(borderbox.x+borderbox.width)){oxa = -oxa;};
			if ((ovalx)<=(borderbox.x+1)){oxa = -oxa;};
			if ((ovaly+ovalheight)>=(borderbox.y+borderbox.height)){oya=-oya;}
			if ((ovaly)<=(borderbox.y+1)){oya=-oya;}
			
//System.out.println(allshapes.size());
			for (int cp = 0;cp<allshapes.size();cp++){
				line1 = allshapes.get(cp);
//System.out.println("line1 = x1"+line1.x1+" x2"+line1.x2+" y1"+line1.y1+" y2"+line1.y2+" height"+borderbox.y);
				if      (line1.x1 > getWidth()-1)  	{line1.rotinc = -line1.rotinc;line1.xinc = -line1.xinc;}
				else if (line1.x1 < borderbox.x+1) 	{line1.rotinc = -line1.rotinc;line1.xinc = -line1.xinc;}
				else if (line1.x2 > getWidth()-1)  	{line1.rotinc = -line1.rotinc;line1.xinc = -line1.xinc;}
				else if (line1.x2 < borderbox.x+1) 	{line1.rotinc = -line1.rotinc;line1.xinc = -line1.xinc;}

				if      (line1.y1 > getHeight()-1) 	{line1.rotinc = -line1.rotinc;line1.yinc = -line1.yinc;}
				else if (line1.y1 < borderbox.y+1)	{line1.rotinc = -line1.rotinc;line1.yinc = -line1.yinc;}
				else if (line1.y2 > getHeight()-1) 	{line1.rotinc = -line1.rotinc;line1.yinc = -line1.yinc;}
				else if (line1.y2 < borderbox.y+1)	{line1.rotinc = -line1.rotinc;line1.yinc = -line1.yinc;}
			}
		} 
		else 
		{ 
			if (ovalx < 1) 						{ovalx = this.getWidth()-1;}
			if (ovalx > this.getWidth()-1) 		{ovalx = 1;}
			if (ovaly < 1) 						{ovaly = this.getHeight()-1;}
			if (ovaly > this.getHeight()-1) 	{ovaly = 1;}
			if (line1.posx < 1) 				{line1.posx = this.getWidth()-1;}
			if (line1.posx > this.getWidth()-1) {line1.posx = 1;}
			if (line1.posy < 1) 				{line1.posy = this.getHeight()-1;}
			if (line1.posy > this.getHeight()-1){line1.posy = 1;}
		}
		g2d.setColor(new Color(177,55,214));
		g2d.drawOval((int)ovalx,(int) ovaly, ovalwidth, ovalheight);
		g2d.fillOval((int)ovalx,(int) ovaly, ovalwidth, ovalheight);
		g2d.setColor(new Color(0,0,0));

		this.drawallthings(g2d);
		
		for(int cs7=0;cs7<allshapes.size();cs7++){
			line1=allshapes.get(cs7);
			line1.update();
			g2d.drawLine(line1.x1, line1.y1, line1.x2, line1.y2);
		}
		displaytime2=System.nanoTime();
		dtimeavg =   dtimeavg*0.99+0.01*(displaytime2-displaytime1) ;
//System.out.println("eetimeavg: (ms)          " + Math.round(eetimeavg/1000000) );
//System.out.println("last eetime: (ms)        " + Math.round((eetime2 - eetime1)/1000000) );
//System.out.println("displaytimeaverage: (ms) "+Math.round(dtimeavg/1000000) );
		eetime1=System.nanoTime();

	}
	
	public void keytoggle(int keycode, int state){
		
	}
	public void pauseresume(){
		
	}

	public void drawallthings(Graphics2D g2d)
	{
		viewsquare.realign();
		tempcolor = g2d.getColor();
		stroke=new BasicStroke(2);

		//draw the box around the world
		tempthing = twM.worldbord;

		drawathing(g2d,tempthing);
		p=viewsquare.txworld2screen(tempthing.getPolygon());

		int size = twM.sceneryz.size();
		for (int n1 = 0;n1<size;n1++)
		{
			tempthing = twM.sceneryz.get(n1);
			drawathing(g2d,tempthing);
			g2d.setColor(new Color(222,222,222) ) ; 
			Point pnt = new Point((int)tempthing.ox,(int)tempthing.oy);
			pnt = viewsquare.txworld2screen(pnt);
			g2d.drawString("Scenery",pnt.x,pnt.y);
		}

		//first get all things
		size = twM.thingz.size();
		for (int n1 = 0;n1<size;n1++)
		{
			tempthing = twM.thingz.get(n1);
			drawathing(g2d,tempthing);
		}
		
		g2d.setColor(tempcolor);
	}
	
	private void drawathing(Graphics2D g2d, Thing th)
	{
		
		p=th.getPolygon();
		p=viewsquare.txworld2screen(p);
		g2d.setStroke(th.getMystroke());

		g2d.setColor(th.getPolyout());
		g2d.drawPolygon( p );
		g2d.setColor(th.getPolyin());
		g2d.fillPolygon( p );

		if(numberson){
		//this will print the coordinates of the thing 
			Point p = new Point(th.xi,th.yi);
			p=viewsquare.txworld2screen(p);
			g2d.setColor(new Color(235,254,254));
			g2d.drawString(String.valueOf(th.xi)+","+String.valueOf(th.yi), p.x, p.y);
		}
		if (identnumberson) {
			Point p = new Point((int)th.ox,(int)th.oy );
			p=viewsquare.txworld2screen(p);
			g2d.drawString(String.valueOf(th.shapenum),p.x,p.y);
		}


		if (directionflags) {
		g2d.setColor(new Color(144,0,33));
		Point p = new Point(th.xi,th.yi);
		Point p2 = new Point((int)(th.xi+(Math.cos(th.getdirection())*50)),
				(int)(th.yi+(Math.sin(th.getdirection())*50)));
		Point p3 = new Point((int)(p.x+(Math.cos(th.getrealdirection())*50)),
				(int)(p.y+(Math.sin(th.getrealdirection())*50)));
		Point p4 = new Point((int)( p.x+th.radmax), p.y);
		System.out.println(p.x+","+p.y+"|"+p3.x+","+p3.y);
		p4=viewsquare.txworld2screen(p4);
		p3=viewsquare.txworld2screen(p3);
		p2=viewsquare.txworld2screen(p2);
		p=viewsquare.txworld2screen(p);
		System.out.println(p.x+","+p.y+"|"+p3.x+","+p3.y);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawString(String.valueOf(Math.rint(100*th.getrealdirection())),p.x,p.y);
		g2d.drawLine(p.x,p.y,p2.x,p2.y );
		g2d.drawLine(p.x,p.y,p3.x,p3.y );
		}
		//the boundary box, a rectangle showing the shape's extent
		if(surroundbox){
			int x11=th.getlb();int y11=th.gettb();int x44=th.getrb();int y44= th.getbb();
			Polygon pg = new Polygon();
			pg.addPoint(x11,y11);
			pg.addPoint(x44,y11);
			pg.addPoint(x44,y44);
			pg.addPoint(x11,y44);
			pg = viewsquare.txworld2screen(pg);
			
			g2d.setColor(new Color(222,222,222));
			g2d.drawPolygon(pg);
			
		}
		
		//this shows the line segment connecting the two points that are the ends of the 
		//two spokes closest to the angle of the approaching object
//		System.out.println("showcollisionspan "+showcollisionspan);

		if (showcollisionspan){
			g2d.setColor(new Color(220,111,50));
			
			Point p = new Point(th.colspoke0.getx2(),th.colspoke0.gety2());
			Point p2 = new Point(th.colspoke1.getx2(),th.colspoke1.gety2());
			p=viewsquare.txworld2screen(p); 
			p2=viewsquare.txworld2screen(p2); 
			g2d.drawLine(p.x,p.y,p2.x,p2.y);
		}
		if (statusdump){
			ThMan thm = twM.thman;
			Point p1 = viewsquare.txworld2screen( new Point((int)thm.ox,(int)thm.oy) );
			
			String s = "The Man-- ox:"+ String.valueOf(p1.x)+",oy:"+String.valueOf(p1.y)+",xt:"+String.valueOf(th.xt)+",yt:"+ 
					String.valueOf(thm.yt)+",xa:"+String.valueOf(thm.xa)+",ya:"+String.valueOf(th.ya)+",r:"+ 
					String.valueOf(thm.r)+",rt:"+ String.valueOf(thm.rt);
			g2d.setBackground(new Color(220,220,230));
			g2d.drawString(s, 2, 10);
		}

	}
	
	public void addshape(){
		Line l = new Line();
		allshapes.add(l);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public Thing getThingAt(int x, int y) {
		//this.t viewsquare.
		//x,y should be in coordinates relative to the frame held by ThingWorldController
		//The "thing" underneath those coordinates will be returned, or else null if nothing is found.
		for (int c1 = 0;c1 < twM.thingz.size();c1++) 
		{
			Point p1 = viewsquare.txscreen2world(new Point(x,y));
			Polygon poly1 = twM.thingz.get(c1).getPolygon();
			Thing t = twM.thingz.get(c1);
			if ( poly1.contains(p1) ) return t;
			//if ( Math.hypot(t.xi-p1.x,t.yi-p1.y) < t.radmax ) return t;
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public boolean isNumberson() {
		return numberson;
	}

	public void setNumberson(boolean numberson) {
		this.numberson = numberson;
	}

	public boolean isIdentnumberson() {
		return identnumberson;
	}

	public void setIdentnumberson(boolean identnumberson) {
		this.identnumberson = identnumberson;
	}

	public boolean isDirectionflags() {
		return directionflags;
	}

	public void setDirectionflags(boolean directionflags) {
		this.directionflags = directionflags;
	}

	public boolean isShowcollisionspan() {
		return showcollisionspan;
	}

	public void setShowcollisionspan(boolean showcollisionspan) {
		this.showcollisionspan = showcollisionspan;
	}

	public boolean isStatusdump() {
		return statusdump;
	}

	public void setStatusdump(boolean statusdump) {
		this.statusdump = statusdump;
	}

	public boolean isSurroundbox() {
		return surroundbox;
	}

	public void setSurroundbox(boolean surroundbox) {
		this.surroundbox = surroundbox;
	}


}
