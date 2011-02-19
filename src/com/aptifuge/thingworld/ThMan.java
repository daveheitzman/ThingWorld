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

public class ThMan extends Thing implements Constance{
	public double facing=0;//direction in radians of direction he is facing, usually 0 or pi
	public Thing onsurfaceof;
	
	
	private Line bbline = new Line(); //the line on thMan's bottom bound
	private Line lineon = new Line(); // the line on the object the man is standing on
	private Collision col = new Collision();
	private boolean onsurface=false;
	
	
	public ThMan(ThingWorldModel tw){
		super(tw);
		setMystroke(new BasicStroke(1));
		setonsurface(false);
		onsurfaceof = null;

		this.addspoke(new ThSpoke(0,0,0,-5));
		this.addspoke(new ThSpoke(0,0,1,-5));
		this.addspoke(new ThSpoke(0,0,1,0));

		this.addspoke(new ThSpoke(0,0,5,0));
		this.addspoke(new ThSpoke(0,0,5,1));
		this.addspoke(new ThSpoke(0,0,0,1));
		
		this.addspoke(new ThSpoke(0,0,4,7));
//		this.addspoke(new ThSpoke(0,0,0,0));
		this.addspoke(new ThSpoke(0,0,2,8));
		this.addspoke(new ThSpoke(0,0,0,1));
		this.addspoke(new ThSpoke(0,0,-4,7));
//		this.addspoke(new ThSpoke(0,0,0,0));
		this.addspoke(new ThSpoke(0,0,-2,8));
		this.addspoke(new ThSpoke(0,0,0,0));
//		this.addspoke(new ThSpoke(0,0,-5,0));
//		this.addspoke(new ThSpoke(0,0,0,0));
	
		//triangle legs
/*		
		this.addspoke(new ThSpoke(0,0,5,0));
		this.addspoke(new ThSpoke(0,0,0,0));
		this.addspoke(new ThSpoke(0,0,0,-5));
		this.addspoke(new ThSpoke(0,0,0,0));
		this.addspoke(new ThSpoke(0,0,4,8));
		this.addspoke(new ThSpoke(0,0,-4,8));
		this.addspoke(new ThSpoke(0,0,0,0));
*/		
//		this.addspoke(new ThSpoke(0,0,-5,0));
//		this.addspoke(new ThSpoke(0,0,0,0));
		
		
		//square box guy
/*		this.addspoke(new ThSpoke(0,0,-4,-4));
		this.addspoke(new ThSpoke(0,0,4,-4));
		this.addspoke(new ThSpoke(0,0,4,4));
		this.addspoke(new ThSpoke(0,0,-4,4));
*/
		this.putatpos(winx/2, winy-80);
		statusdump=true;
		this.twM =tw;
		gravityfactor=4;
	}
	
//	@Override
//	public void drawself(Graphics2D g2d){
//		g2d.drawOval(xi-3-twM.dispx1, yi-8-twM.dispy1, 8, 6);
//		super.drawself(g2d);
		//g2d.drawOval(xi-3, yi-8, 6, 5);
//	
//	}
	@Override
	public void update(ThingWorldModel tw){
//		System.out.println("manticks "+ticks);
		//keyboard
		dokeys();
		double ticks = tickspassed();
		if ( getonsurface() ){
			ya=0.0;yt=0.0;
		}
		super.update(tw);
		friction(ticks);
		limitspeed();
		updatesurfacelines();
		if ( getonsurfaceof()!=null ){
			//our man is standing on some line segment. 
			Point p = col.linesintersectpoint(lineon, bbline);
			if ( p != null) {			
				this.putatpos( p.x, p.y-(int)((this.gettb()-this.getbb())/2) ) ;
				} 
			else {
				this.setonsurfaceof(null);
			}
		} else {
			//recalculate the angle of the edge of the thing I am on.
			//do sliding, depending on the steepness of the surface 
			
			
		}
	} 
	
	@Override 
	public void notifycollision(Thing thThing, double newangle, double newvelocity)
	{
		//check the velocity and location of theMan and 
		//the object he collided with.
		//if (this.getrealdirection() )
		System.out.println("Man/Object collision");
		//System.exit(0);
		//hitbot();
		updatesurfacelines();
		setonsurfaceof(thThing);
		
	}
	
	@Override
	public void hitbot(){
		//if ( getonsurface() ) return;
		System.out.println("MAN: yt = "+yt);
		//dont' mess with this stuff. it makes the guy bounce a certain way upon hitting 
		//the ground. it takes a lot of effort to get it to look decent. 
		if (Math.abs(yt) < 1.10) {
			this.putatpos(ox, this.twM.worldbord.getbb() - (5+ this.getbb() - this.gettb())/2 );
			ya=0.0; yt=0;
			setonsurfaceof(this.twM.worldbord);
		} else {
			yt = -0.4*Math.abs(yt);
			this.putatpos(ox, this.twM.worldbord.getbb() - (5+ this.getbb() - this.gettb())/2 );
//			setonsurfaceof(null);
		}
	}

	public void dokeys(){
		//keyboard
		//System.out.println("dokeys()");
		double kh;
		if (KB.keydown[KEY_UP]){
			facing = pi*1.5;			
		}  

		if(KB.keydown[KEY_LEFT]){
			if (getonsurface()){
				this.xa=-0.2;
			} else
			{	if (xt > 0) this.xa=-0.07;}
		facing = pi;
		} else 
		if(KB.keydown[KEY_RIGHT]){
			if (getonsurface()){
				this.xa = 0.2;
			} else 
			{	if (xt < 0 ) this.xa = 0.07;}
		facing = 0;
		} else 
		{
		this.xa = 0;
		}
		
		if ((kh=KB.getkeytime(KEY_DOWN))>keylowthresh) {
			//this.ya=0.2;
		}
		if (KB.keydown[KEY_ACT]){
			double dir = facing < (pi/2)?1.75*pi:1.25*pi;
			if (facing == 1.5*pi) dir = 1.5*pi;
			twM.addmissile(xi, yi, dir);
		}  
		//JUMP ROUTINE:
		if (getonsurface())	{
			if (KB.keydown[KEY_JUMP]){
				this.ya=-3.8;
				this.yt=-1.5;
				System.out.println("JUMP!!"+kh);
				this.adjustpos(0, -2);
				setonsurface(false);
				
			}
		} 
	} 
	public void limitspeed(){
		if (xt > horzlimit) {
			xt=horzlimit;
		} 
		if (xt < (-horzlimit)){
			xt = -horzlimit;
		}
		if (yt < vertuplimit){
			yt=vertuplimit;
		}  
		if (yt > vertdownlimit){
			yt= vertdownlimit;
		}
	}
	public void friction(double ticks){
		if (getonsurface()){
			if (Math.abs(xt)>0.001) {xt=xt*Math.pow(friction, ticks);}
			else 					{xt = 0.0;}
		}
	}
	
	public void updatesurfacelines() {
		//updates the line at thman's bottom edge, and the line of the 
		// thing he is standing on. 
		Thing th = getonsurfaceof();
		if (th != null )
		{
			this.bbline.x1 = this.getlb();
			this.bbline.x2=this.getrb();
			this.bbline.y1 = this.getbb();
			this.bbline.y2 = this.bbline.y1;
			
			this.lineon.x1 = th.colspoke0.getx2();
			this.lineon.y1 = th.colspoke0.gety2();
			this.lineon.x2 = th.colspoke1.getx2();
			this.lineon.y2 = th.colspoke1.gety2();
		}
	}
	
	public Thing getonsurfaceof(){
		return onsurfaceof;
	}
	public void setonsurface(boolean b){onsurface = b;}
	public boolean getonsurface () {return onsurface;}

	public void setonsurfaceof(Thing th){
		setonsurface(th != null );
		onsurfaceof = th;
	}

	@Override
	public void gravity(double ticks){
		Thing th = getonsurfaceof();
		double ang;
	  	if (!onsurface){
			//ya=ya+(gravityfactor*gravity);
			//ya=ya+Math.pow(gravityfactor*gravity,ticks);
			ya=ya*(1+Math.pow((gravityfactor*gravity),ticks));
			ya=ya+(gravityfactor*gravity);
		} else {
// new code
			th = getonsurfaceof();
//			ang = this.lineon.rot%pi;
//			this.setdirection(this.lineon.rot%pi);

// old code 
//			ya=0;
		}
		if (getonsurface()) {
			ya = th.ya;
			xa = th.xa;					
		}
	}

}

//JUMP ROUTINE THAT DOESN't quite work 
/*
if (onsurface)	{
	kh=KB.getkeytime(KEY_JUMP);
	if (kh > keyhighthresh){ 
		kh = keyhighthresh+100;
	}
	if (!KB.jumpexpired){
			if (KB.keydown[KEY_JUMP]){
				if(kh>=keyhighthresh){
					KB.makejumpexpired();
					//do jump:
					this.ya=-1.3-0.5*(kh/keyhighthresh);
					System.out.println("JUMP!!"+kh);
					onsurface=false;
				}
			} else {
				if (kh>keylowthresh)
				{
					//do jump:
					KB.makejumpexpired();
					this.ya=-1.3-0.5*(kh/keyhighthresh);
					System.out.println("JUMP!!"+kh);
					onsurface=false;
				}
			}
	}
} 
*/
