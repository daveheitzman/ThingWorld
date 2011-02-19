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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import ec.util.*;
public class Thing implements Constance {
	//this is the interface for all "things" in the world. 
	/*
	 * 1. 	have a "per tick" modifier for x and y (position), and r, rotation (in radians: positive = counterclockwise)
	 * 2. 	have a per tick modifier for the modifiers
	 * 3. 	double: ox,oy (position) -- this is the "center" of the object.
	 * 4. 	double: xt,yt (per tick modifier) (speed)
	 * 5. 	double: xa,ya (per tick modifier for the modifier) (acceleration)
	 * 	  	double: r = current rot, rt = rot modifier, ra = modifies the modifier 	
	 * 6. 	int:    xi,yi -- integer screen position variable -- updated each call to update(). 
	 * 
	 * 
	 * Therefore: 
	 * 7. 	Each call to "update" results in a check to system.nanotime()
	 * 8. 	the number of "ticks" is calculated, but capped at tickspersecond (typically 30). 
	 * 9. 	xp = xp + xt*ticks, etc.
	 * 10. 	xt = xt + xa*ticks..
	 * 11. 	xa (acceleration) can be altered by weather or other factors. Be mindful to set to zero if not 
	 * 		needed for something
	 * 12. 	lbound,rbound,tbound,bbound: left, right,top,bottom - these are methods, calculated when needed,
	 * 		not each update.
	 * 13. each object has a "span" from center ls = left span, the distance from center (xp,yp) to its leftmost bound.
	 *
	 * 14. all angles are in RADIANS, from 0 - 2pi. no negative pi radians - screws up calculations. 
	 *
	 * */
	public Polygon polygon = new Polygon(); //used for drawing in colored
	private Color polyout = new Color(0,0,0);
	private Color polyin = new Color(0,0,0);
	private Point centerpoint = new Point(0,0);

	//rendering things
	private BasicStroke mystroke = new BasicStroke(1);

	
	public ThingWorldModel twM;
	
	public double ox, oy, xt, yt, xa, ya, r, rt, ra, radmin=Double.MAX_VALUE, radmax=Double.MIN_VALUE;
	
	public double lastox=0,lastoy=0;
	
	public double realdirection = 0;
	
	public int xi=0,yi=0, shapenum=-99;
	public double lastupdate;
	public ThSpoke colspoke0=new ThSpoke(0,0,0,0), colspoke1= new ThSpoke(0,0,0,0); 
	// the two "collision" spokes updated by calls to closest()
	public int numspokes=0;
	public ThSpoke[] spokes = new ThSpoke[maxspokes];
	public double width, height;
	private double maxxseen, minxseen,maxyseen,minyseen;
	public int hiatus=0; //if hiatus > 0 the collision system will not collide this. This is useful
						// for making sure recently collided objects do not "recollide" before they
						// have a chance to move far enough away from each other. this value is 
						// decremented each call to update()
	
	protected int spawnlocx,spawnlocy;
	
	
	//helpful output and markups
	public boolean numberson=true;
	public boolean surroundbox =false;
	public boolean identnumberson=false;
	public boolean statusdump=false;
	public boolean directionflags=false;
	public boolean showcollisionspan=false;
	
	private boolean translatetowindow = true;

	//physics

	protected double gravityfactor=1.0;
	
	//these are immutable, and cause the acceleration to automatically decay to 0 at this rate
	private final double xad=xacceldecay,yad=yacceldecay;	
	
	//time to live
	protected int ttl = 0x7fffffff;
	
	//display
	private boolean iamvisible = true;

	//interacts with world
	private boolean icollide = true;
	private boolean iamgravitational = true;
	private boolean iamscenery = false;

	double highestValueEver = Double.MAX_VALUE-1; // I subtract 1 in case there is something f'd about their edge-case handling

	
	public Thing(ThingWorldModel tw,int n){
		this.twM=tw;	
		init(n);		
	}
	
	public Thing(ThingWorldModel tw){
		this.twM=tw;
		//init();
	}
	
	public void init(int n){
		//make a random shape
		lastupdate=System.nanoTime();
		setMystroke(new BasicStroke(2));
		if (n == RANDOM) { n = rand.nextInt(5)+1;}
		switch(n){
		case RANDOM:
			break;

		case POLYGON:{
			polyout = polout;
			//polyin = polin;
			polyin = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			int lenmax=20;
			this.xt = rand.nextDouble()*2.7-1.35;
			this.yt = rand.nextDouble()*2.9-1.45;
			this.rt = rand.nextDouble()*0.017-0.0085;//.4-0.2;
			ThSpoke thspoke;// = new ThSpoke(375,250,Math.random()*lenmax,0);
			double deg = 0, numspokes=rand.nextDouble()*8+5;
			double spokesadded=0;
			while ((deg < 2*pi) && (spokesadded <= numspokes)){
				deg = deg + (2*pi/numspokes) + (rand.nextDouble()*pi/10-pi/20);
				thspoke = new ThSpoke(375,250,lenmax*2+rand.nextDouble()*4-2,deg);
				addspoke(thspoke);
				spokesadded++;
				}
			this.putatpos(0,0);
			this.xt=0;this.yt=0;
			
			}
			break;

		case SQUARE:{
			polyout = squout;
//			polyin = squin;
			polyin = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			int lenmax=50;
			this.yt = rand.nextDouble()*3-1.5;
			this.rt = rand.nextDouble()*0.015-0.0075;//0.4-0.2;
			this.r=0;
			this.ra=0;
			//this.rt=0;
			ThSpoke thspoke;// = new ThSpoke(375,250,Math.random()*lenmax,0);
			double deg = 0, numspokes=2,len;
			len = rand.nextDouble()*lenmax+3;
			thspoke = new ThSpoke(250,250,len,0);
			addspoke(thspoke);
			thspoke = new ThSpoke(250,250,len,pi/2.0);
			addspoke(thspoke);
			thspoke = new ThSpoke(250,250,len,pi);
			addspoke(thspoke);
			thspoke = new ThSpoke(250,250,len,3*pi/2.0);
			addspoke(thspoke);
			} break;
		case RECTANGLE:{
			polyout = squout;
//			polyin = squin;
			polyin = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
//			polyin = new Color(rand.nextInt(55),rand.nextInt(55),255);
			int lenmax=50;
			this.yt = rand.nextDouble()*3-1.5;
			this.rt = rand.nextDouble()*0.015-0.0075;//0.4-0.2;
			this.r=0;
			this.ra=0;

			double deg1 = pi- rand.nextDouble()*(0.8*pi);
//			deg1 = pi/2;
			double len = rand.nextDouble()*lenmax+8;
			addspoke(new ThSpoke(250,250,len,0));
			addspoke(new ThSpoke(250,250,len,deg1));
			addspoke(new ThSpoke(250,250,len,pi));
			addspoke(new ThSpoke(250,250,len,pi+deg1));
			
		} break;
		case TRIANGLE:{
			polyout = squout;
			polyin = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			int lenmax=50;
			this.yt = rand.nextDouble()*3-1.5;
			this.rt = rand.nextDouble()*0.015-0.0075;//0.4-0.2;
			this.r=0;
			this.ra=0;

			double deg1 = pi- rand.nextDouble()*(0.7*pi);; 
			double deg2 = pi- rand.nextDouble()*(0.7*pi);;
			
			double len = rand.nextDouble()*lenmax+3;
			addspoke(new ThSpoke(250,250,len,0));
			len = rand.nextDouble()*lenmax+3;
			addspoke(new ThSpoke(250,250,len,deg1));
			len = rand.nextDouble()*lenmax+3;
			addspoke(new ThSpoke(250,250,len,deg1+deg2));
			
		} break;
		case SPIKY:{
			polyout = polout;
			//polyin = polin;
			polyin = new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			int lenmin=10;
			this.xt = rand.nextDouble()*2.7-1.35;
			this.yt = rand.nextDouble()*2.9-1.45;
			this.rt = rand.nextDouble()*0.217-0.0085;//.4-0.2;
			ThSpoke thspoke;// = new ThSpoke(375,250,Math.random()*lenmax,0);
			double deg = 0, numspokes=rand.nextDouble()*8+5;
			double spokesadded=0;
			while ((deg < 2*pi) && (spokesadded <= numspokes)){
				deg = deg + (2*pi/numspokes) + (rand.nextDouble()*pi/10-pi/20);
				thspoke = new ThSpoke(375,250,lenmin+rand.nextDouble()*20,deg);
				addspoke(thspoke);
				spokesadded++;
				}
			this.putatpos(0,0);
			this.xt=0;this.yt=0;
		} break;
		} //end switch
	};
	public int getrb(){
		maxxseen=-highestValueEver;
		for (int cw=0;cw<numspokes;cw++){
			maxxseen = Math.max(spokes[cw].x2,maxxseen);
//			System.out.println(spokes[cw].getx2()+" "+spokes[cw].gety2());
		}
		return (int)Math.round( maxxseen);};//rightbound
	public int getlb(){
		minxseen=highestValueEver;
		for (int cw=0;cw<numspokes;cw++){
			minxseen = Math.min(spokes[cw].x2,minxseen);
		}
		return (int)Math.round(minxseen);
	};//leftbound
	public int gettb(){
		minyseen=highestValueEver;
		for (int cw=0;cw<numspokes;cw++){
			minyseen = Math.min(spokes[cw].y2,minyseen);
		}
		return (int)Math.round(minyseen);
	};//topbound
	public int getbb(){
		maxyseen=-highestValueEver;
		for (int cw=0;cw<numspokes;cw++){
			maxyseen = Math.max(spokes[cw].y2,maxyseen);
		}
		return  (int)Math.round(maxyseen);
	};//bottombound

	public void update(ThingWorldModel tw){
		double ticks= tickspassed(); 

		ttl--; //time to live is decremented
		//1: basic movement
		lastox = ox;
		lastoy = oy;
		ox=ox+(xt*ticks);
		oy=oy+(yt*ticks);
		yt=yt+(ya*ticks);
		xt=xt+(xa*ticks);

		xi = (int)ox;
		yi = (int)oy;

		for (int cw=0;cw<numspokes;cw++){
			spokes[cw].incang(rt*ticks);
			spokes[cw].ox=ox;
			spokes[cw].oy=oy;
		}

		//2: "physics"
		if (!iamscenery) {
			acceldecay(ticks);
			gravity(ticks);
		}
		lastupdate=System.nanoTime();
		hiatus=0;
	};
	

	
	public double tickspassed(){
		double t = ( (System.nanoTime()-lastupdate)/(nanospertick) );
		if (t > tickspersecond) {t = tickspersecond;}
		if (t > 5 ){t=1;}
		return t;
	} 
	
	public void addspoke(ThSpoke spk){
		if (numspokes<maxspokes){
			spokes[numspokes]=spk;
			numspokes++;
			if (spk.len > radmax) {radmax = spk.len;}
			if (spk.len < radmin) {radmin = spk.len;}
			ox = spk.ox;
			oy = spk.oy;
		}
	}
	
	public void setrot(double r){
		for (int cw=0;cw<numspokes;cw++){
			spokes[cw].ang=Trig.norm(r+spokes[cw].ang);
		}	
	}
	
	public void adjustpos(double xadj, double yadj){
		this.ox = this.ox+xadj;
		this.oy = this.oy+yadj;
		for (int cw=0;cw<numspokes;cw++){
			spokes[cw].incox(xadj);
			spokes[cw].incoy(yadj);
		}
	}

	public void adjustposrads(double theta, double distance){
		double th = Trig.norm(theta);
		double xadj,yadj;
		xadj = Math.cos(th)*distance;
		yadj = Math.sin(th)*distance;
		adjustpos(xadj,yadj);
	}

	public void putatpos(double x,double y){
		this.ox = x;
		this.oy = y;
		for (int cw=0;cw<numspokes;cw++){
			spokes[cw].ox=x;
			spokes[cw].oy=y;
			//	System.out.println(spokes[cw].getx2()+" "+spokes[cw].gety2());
		}		
	}

	public void hitrt() {
		this.xt=-(Math.abs(xt));		
		adjustpos(-2.0,0.0);
	}

	public void hitlf() {
		//hit something to the left
		this.xt=Math.abs(xt);
		adjustpos(2.0,0.0);
	}

	public void hittop() {
		this.yt=(Math.abs(yt));
		adjustpos(0.0,2.0);
	}

	public void hitbot() {
		this.yt=-(Math.abs(yt));
		adjustpos(0.0,-2.0);
	}	

	public Fourint closest(double angle){
		//this returns the closest two points in x,y space to the radius from ox,oy at the angle given
		if (numspokes < 2) return (new Fourint());
		boolean done =false;
		double dif;
		angle = Trig.norm(angle) ;
		Fourint fi= new Fourint();
		for (int g2=0;g2<numspokes-1;g2++){
			if (spokes[g2].ang > spokes[g2+1].ang){
				//we are crossing over from 1.999pi to 0.01 pi
				//for comparison, we drop everyone below twopi and see how far they fall
				double a =Trig.norm(pi+spokes[g2].ang);
				double b =Trig.norm(pi+spokes[g2+1].ang);
				double between = Trig.norm(pi+angle);
				 ; 
				if ((between>a)&&(between<b)) {
					done=true;
					colspoke0 = spokes[g2];colspoke1=spokes[g2+1];
					fi.x1=spokes[g2].getx2();fi.y1=spokes[g2].gety2();
					fi.x2=spokes[g2+1].getx2();fi.y2=spokes[g2+1].gety2();
				}
			}else
			if ((angle > spokes[g2].ang)&&(angle < spokes[g2+1].ang)){
				done=true;
				colspoke0 = spokes[g2];colspoke1=spokes[g2+1];
				fi.x1=spokes[g2].getx2();fi.y1=spokes[g2].gety2();
				fi.x2=spokes[g2+1].getx2();fi.y2=spokes[g2+1].gety2();				
			}			
		}
		//this bottom part is for the situation where the two angles of interest
		//are the last elements of the array the the first: spokes[numspokes-1].ang, spokes[0].ang
		if (!done) {
			if (spokes[numspokes-1].ang > spokes[0].ang){
				double a =Trig.norm(pi+spokes[numspokes-1].ang);
				double b =Trig.norm(pi+spokes[0].ang);
				double between = Trig.norm(pi+angle);
				if ((between>a)&&(between<b)) {
					done=true;
					colspoke0 = spokes[numspokes-1];colspoke1=spokes[0];
					fi.x1=spokes[numspokes-1].getx2();fi.y1=spokes[numspokes-1].gety2();
					fi.x2=spokes[0].getx2();fi.y2=spokes[0].gety2();
				}
			} else 
			if ((angle < spokes[0].ang)&&(angle > spokes[numspokes-1].ang )){
			done=true;
			colspoke0 = spokes[numspokes-1];colspoke1=spokes[0];
			fi.x1=spokes[numspokes-1].getx2();fi.y1=spokes[numspokes-1].gety2();
			fi.x2=spokes[0].getx2();fi.y2=spokes[0].gety2();	

			//System.out.println("colspokes:"+colspoke0.ang+","+colspoke1.ang);
			}
		}
		if (!done) {fi.x1=-9999999;}
		return fi;
	}
	
	public double getvelocity(){
		return Math.hypot(xt, yt);
	}
	public double getdirection(){
		//returns the direction it is theoretically traveling, not counting on gravity or other factors 
		return Trig.norm(Math.atan2(yt,xt));
	}
	public double getrealdirection(){
		//this returns the actual direction it is traveling, based on its last position and its current position
		return Trig.norm(Math.atan2(oy-lastoy,ox-lastox));
	}
	
	public void setdirection(double theta){
		//direction goes clockwise starting from 3 o'clock which == 0, to 9 o'clock which
		//== pi, to 12:00 which is 1.5 pi, etc. 
		double speed = getvelocity();
		xt = Math.cos(theta)*speed;
		yt = Math.sin(theta)*speed;
	}

	public void acceldecay(double ticks){
		//it must be done this way using exponents in order to capture 
		//"multiplying" by a factor over a different number of frames, due to sizing
		//everything to the number of "ticks" that have clicked since last update (often < 1) 
		if (xa>0.001){xa = xa*Math.pow(xad, ticks);} else {xa=0.0;}
		if (ya>0.001){ya = ya*Math.pow(yad, ticks);} else {ya=0.0;}
		
	}
	public void gravity(double ticks){
			ya=ya*(1+Math.pow((gravityfactor*twM.getGravity_current()),ticks));
			ya=ya+(gravityfactor*twM.getGravity_current());
	}

	public Color getPolyout() {	return polyout;}

	public void setPolyout(Color polyout) {	this.polyout = polyout;	}

	public Color getPolyin() {	return polyin;	}

	public void setPolyin(Color polyin) {	this.polyin = polyin;	}

	public void setiamscenery(boolean b) {
			xa=0;xt=0;ya=0;yt=0;
			iamscenery=b;
	}

	public Polygon getPolygon() 
	{
		polygon.reset();
		for (int c4=0;c4<numspokes;c4++)
		{
			polygon.addPoint(spokes[c4].getx2(),spokes[c4].gety2());
		}			
		return polygon;
	}
	
	public Point getCenter()
	{
		centerpoint.setLocation(ox,oy);
		return centerpoint;	
	}


	
	public void setpolyin(Color color) {
		// TODO Auto-generated method stub
		if (color != null) {		this.polyin = color;}
	}

	public void setvelocity(double newvelocity){
		double currentvelocity = this.getvelocity();
		xt = xt*(newvelocity/currentvelocity);
		yt = yt*(newvelocity/currentvelocity);
	}
	
	public void notifycollision(Thing t2, double newangle, double newvelocity) {
		// TODO Auto-generated method stub
		//note that colspoke0 and colspoke1 should be set correctly as the collided spoke
		this.setdirection(newangle);
		this.setvelocity(newvelocity);
	//	this.hiatus=4;
	}
	public BasicStroke getMystroke() {
		return mystroke;
	}

	public void setMystroke(BasicStroke mystroke) {
		this.mystroke = mystroke;
	}
}

