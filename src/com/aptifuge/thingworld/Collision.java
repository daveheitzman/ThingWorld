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

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;


public class Collision implements Constance{
	public boolean simplecollision=false; //just measures distance of max radiuses 
	
	ThingWorldModel tw;
	public class CollidedThingSet {
		//these variables can just be passed to doCollision(..) as is
		//two Things and some related variables. 
		//adds these Things to the queue so that collisions can be performed on them
		//t1,t1 are the two Things colliding
		//d1 and d2 are the respective distances between the center of the thing and the point on the line that connects
		//the ends of the two spokes that are facing the collision where that line intersects the line that 
		//intersects the centers of both objects .
		//dist is the distance between the two objects' centers. 
		public CollidedThingSet(Thing t1, Thing t2,double d1, double d2, double dist){
			this.t1=t1;this.t2=t2;this.d1=d1;this.d2=d2;this.dist=dist;
		}
		public Thing t1,t2;
		public double d1,d2,dist;
		
	}
	
	public ArrayList<CollidedThingSet> collisionqueue = new ArrayList<CollidedThingSet>(0);
	
	public Collision(ThingWorldModel tw){
		this.tw=tw;
	}
	
	public Collision () {}
	
	public Point linesintersectpoint(Line l1,Line l2) {
		Point p = new Point();
		// Given two lines, it returns the point at which they meet. If this point is 
		//beyond the extent of either line, it p is set to null;

		double ymeet,xmeet,m1,m2,b1,b2,slp1 = l1.rot, slp2=l2.rot;

		m1 = Math.tan(slp1);
		m2 = Math.tan(slp2);
		
		b1=l1.y1-m1*l1.x1;
		b2=l2.y1-m2*l2.x1;
		xmeet=(b2-b1)/(m1-m2);
		ymeet = m1*xmeet+b1;
		if (  (xmeet > l1.lbound)&&(xmeet < l1.rbound)
			&&(xmeet > l2.lbound)&&(xmeet < l2.rbound)
			&&(ymeet > l1.topbound)&&(ymeet < l1.botbound)
			&&(ymeet > l2.topbound)&&(ymeet < l2.botbound)) 
		{
			p.x =(int)xmeet; p.y=(int)ymeet;
		} else 
		{
			p=null;
		}
		return p;
	}
	
	public boolean linesintersect(Line l1, Line l2){
		//determines if two lines intersect. returns 0 if not. returns the x value of the crossover point if so.
		//watch out for vertical and horizontal lines! 
		boolean collisiondetected=false;
		double ymeet,xmeet,m1,m2,b1,b2,slp1 = l1.rot, slp2=l2.rot;

		m1 = Math.tan(slp1);
		m2 = Math.tan(slp2);
		
//lines with similar slopes are slipping through
//		if (Math.rint((slp1%pi)*10)==Math.rint((slp2%pi)*10)) return true;
		
		b1=l1.y1-m1*l1.x1;
		b2=l2.y1-m2*l2.x1;
		xmeet=(b2-b1)/(m1-m2);
		ymeet = m1*xmeet+b1;
		if (  (xmeet > l1.lbound)&&(xmeet < l1.rbound)
			&&(xmeet > l2.lbound)&&(xmeet < l2.rbound)
			&&(ymeet > l1.topbound)&&(ymeet < l1.botbound)
			&&(ymeet > l2.topbound)&&(ymeet < l2.botbound)) {

			collisiondetected=true;
//			tw.af.pauseresume();
//			System.out.println("Collision!");
//			System.out.println("x:"+xmeet+" y:"+ymeet);
//			System.out.println(Math.PI+" "+Math.PI/2);
			
			/*			System.out.println("m1 "+m1);
			System.out.println("m2 "+m2);			
			System.out.println("b1 "+b1);
			System.out.println("b2 "+b2);
			System.out.println("lbound "+l1.lbound);
			System.out.println("rbound "+l1.rbound);
			System.out.println(xmeet);
*/
//			tw.af.pauseresume();
//			System.out.println("collision: "+xmeet+","+ymeet);
		}
		return collisiondetected;
	}

	public void collisions(ArrayList<Thing> thingz, ThingWorldModel tw){
		Thing t1,t2;
		double t1t2ang,t2t1ang,mindist,dist;
		
		
		//go through and analyze the Thingz for collisions
		//the queue will become filled up with collided objects. 
		if (thingz.size()>1) {
			for (int f4=0;f4 < (thingz.size()-1);f4++){
				for (int f6=f4+1;f6<thingz.size();f6++){
					t1=thingz.get(f4);
					t2=thingz.get(f6);
					mindist = t1.radmax + t2.radmax;
					dist = Math.hypot(t1.ox-t2.ox, t1.oy-t2.oy);
					t1t2ang = Trig.norm(Math.atan2(t2.oy-t1.oy, t2.ox-t1.ox));
					t2t1ang = Trig.norm(Math.atan2(t1.oy-t2.oy, t1.ox-t2.ox));
					
					if( (t1.hiatus <=0) && (t2.hiatus <= 0) && (mindist>dist*1.1) ){
//					if( (mindist>dist*1.1) ){
						this.assessexactcollision(t1, t2);
						
						double d1=getradiusbetweenspokes(t1,t1t2ang);
						double d2=getradiusbetweenspokes(t2,t2t1ang);
						if((d1+d2)>dist){
							queuecollision(t1,t2,d1,d2,dist);
							//docollision(t1,t2,d1,d2,dist);
							System.out.println("collision level 1");
							System.out.println("t1-t2: "+d1+", t2-t1: "+d2+
							". Distance from centers: "+dist);
						}else {
									assessexactcollision(t1,t2);
									
									Line l1 = new Line(	t1.colspoke0.getx2(),
														t1.colspoke0.gety2(),
														t1.colspoke1.getx2(),
														t1.colspoke1.gety2());
									Line l2 = new Line(	t2.colspoke0.getx2(),
														t2.colspoke0.gety2(),
														t2.colspoke1.getx2(),
														t2.colspoke1.gety2());
							
									if 	(linesintersect(l1,l2)) 
										{
											queuecollision(t1,t2,d1,d2,dist);
											//docollision(t1,t2,d1,d2,dist);
											System.out.println("collision level 3");
										} 
						} 
					} //end are both objects not on "hiatus" 		
				} //end inner for loop
			}// end main for loop
		}//	end	if (thingz.size()>1) 

		//the collisionqueue will be completely emptied by the following routine
		//First process collision queue -- these are objects already collided that need to 
		//have their travel angle and velocity adjusted 
		while (collisionqueue.size() > 0) {
			CollidedThingSet ctp = collisionqueue.remove(collisionqueue.size()-1);
			docollision(ctp.t1,ctp.t2,ctp.d1,ctp.d2,ctp.dist);
			
		}

	}//end collisions

	public void queuecollision(Thing t1, Thing t2, double d1, double d2, double dist){
		//adds these Things to the queue so that collisions can be performed on them
		//t1,t1 are the two Things colliding
		//d1 and d2 are the respective distances between the center of the thing and the point on the line that connects
		//the ends of the two spokes that are facing the collision where that line intersects the line that 
		//intersects the centers of both objects .
		//dist is the distance between the two objects' centers. 
	
		collisionqueue.add(new CollidedThingSet(t1,t2,d1,d2,dist));
		
	}
	
	public void assessexactcollision(Thing t1, Thing t2){
		//note: this method will change t1 and t2 so that their fields colspoke0 and colspoke1
		//will be set to contain the end points (x2,y2) of the two spokes that are the closest
		//to the line connecting the centers of t1 and t2. 
		
		double t1t2ang; //angle from the center of t1 to the center of t2
		double t2t1ang; //angle from center of t2 to the center of t1
		t1t2ang = Math.atan2(t2.oy-t1.oy, t2.ox-t1.ox);
		t2t1ang = Math.atan2(t1.oy-t2.oy, t1.ox-t2.ox);

		t1.closest(t1t2ang); //this resets colspoke0 and colspoke1
		t2.closest(t2t1ang); //this resets colspoke0 and colspoke1
		}
	
	public void docollision(Thing t1, Thing t2, double d1, double d2, double dist){
		double t1t2ang, t2t1ang,t1newang,t2newang,t1newvelocity,t2newvelocity;
		double temp = t1.getrealdirection();
		double midang = temp + ( temp-t2.getrealdirection() / 2 );
		int t1newposx,t1newposy,t2newposx,t2newposy;
		t1t2ang = Math.atan2(t2.oy-t1.oy, t2.ox-t1.ox);
		t2t1ang = Math.atan2(t1.oy-t2.oy, t1.ox-t2.ox);

		t1newang = (t1t2ang+pi)-(t1.getrealdirection()-t1t2ang);
		t2newang = (t2t1ang+pi)-(t2.getrealdirection()-t2t1ang);
		
		
		
		t1newvelocity=t1.getvelocity();
		t2newvelocity=t2.getvelocity();
		
		t1.notifycollision(t2,t1newang,t1newvelocity);
		t2.notifycollision(t1,t2newang,t2newvelocity);
	}

	public double getradiusbetweenspokes(Thing t1, double midang){
		double rval, raddist,lendif,dc01,dc10;
		double eda,fer,fed,red,nr,er,ed,fd,nd,rd,fde,fdr,rde,rda,ef;
		ed=t1.colspoke0.len;
		fd=t1.colspoke1.len;
		ef=Math.hypot(t1.colspoke0.x2-t1.colspoke1.x2, t1.colspoke0.y2-t1.colspoke1.y2);
		fed=Math.acos((fd*fd-ed*ed-ef*ef)/(-2*ed*ef));
		fde = Trig.distanceclockwise(t1.colspoke0.ang, t1.colspoke1.ang);
		
		if (fde < pidiv2)
		{
		//this is when the angle between the two spokes is < pi/2 (45 deg)
		eda=Trig.distanceclockwise(t1.colspoke0.ang,midang)%(pi);
		er=Math.abs(Math.sin(eda)*ed);
		red=(pidiv2)-(eda%(pidiv2));
		fer = Math.abs(fed-red);
		nr=Math.abs(Math.tan(fer)*er);
		rd=Math.abs(er/Math.tan(eda));
		nd=rd+nr;
		} 
		else
		{
		//this is when the angle between the two spokes is > 2pi
		eda=Trig.distanceclockwise(t1.colspoke0.ang,midang)%(pi);
		rd = Math.abs(ed*Math.sin(fed));
		rde = pidiv2-fed;
		rda = rde-eda;
		nd = Math.abs(rd/Math.cos(rda));
		}
		rval = nd;
//		System.out.println("radius dist: shapenum "+t1.shapenum+",rad = "+eda+". Dist= "+nd+" t1.radmax="+t1.radmax);
		return rval;
	}
	
}
