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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Random;

import com.aptifuge.thingworld.Trig;

import sun.java2d.loops.DrawLine;

/*
 * thingworld -- a place where all these things live. 
 * 
 * */

public class ThingWorldModel implements Constance{
	Collision collision;
	ThingWorldView twView;
	Trig trig=new Trig();
	ThingWorldController af;
	ArrayList<Line> allshapes = new ArrayList<Line>();	
	ArrayList<Thing> thingz = new ArrayList<Thing>();
	ArrayList<Thing> fixedz = new ArrayList<Thing>();
	ArrayList<Thing> sceneryz = new ArrayList<Thing>();
	
	Thing midline = new Thing(this);
	Thing bord = new Thing(this);
	Thing worldbord = new Thing(this);
	Thing triangle = new Thing(this);
	Thing square = new Thing(this,Thing.SQUARE);
	Thing poly = new Thing(this,Thing.POLYGON);
	ThMan thman = new ThMan(this);
//	int dispx1,dispy1,dispx2,dispy2;//upper left, lower right corners of the "display" window
//	int dispmidx;//middle of the display window, expressed in the coordinates of the full "world" area
	
	int keystates[];
//	Random r = new Random();

	private boolean collisionson=true;
	private boolean bounce = true;
	private boolean worldboxon=true;
	Graphics2D g2d;

	private double missilewait=0;
	private double gravity_current = gravity;
	
	public boolean getbounce(){return bounce;}
	
	public ThingWorldView getthingdisplay(){
		return this.twView;
	}

	public void addshape(){
		Thing t = new Thing(this,Thing.RANDOM);
		t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
		t.yt=rand.nextDouble()*5 - 2.5;
		t.xt=rand.nextDouble()*5 - 2.5;
		thingz.add(t);
	}
	
	public ThingWorldModel(ThingWorldController af){
//System.out.println("constructed thingworld");
		this.af=af;
		collision = new Collision(this);
		twView = new ThingWorldView(this,af);


		//collidable objects that are moving and interacting:
		//add squares with random position and velocity
		
		for (int c2=0;c2<2;c2++){
			Thing t = new Thing(this,Thing.SQUARE);
			t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
			t.yt=rand.nextDouble();
			t.xt=rand.nextDouble();
			thingz.add(t);	
			thingz.get(c2).shapenum=c2;
		}

		//add polygons with random position and velocity
		for (int c2=0;c2<2;c2++){
			Thing t = new Thing(this,Thing.POLYGON);
			t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
			t.putatpos(rand.nextInt(WORLDSIZEX) , rand.nextInt(WORLDSIZEY) );
			t.yt=rand.nextDouble();
			t.xt=rand.nextDouble();
			thingz.add(t);				
			thingz.get(c2).shapenum=c2;
		}
		//add rectangles
		for (int c2=0;c2<2;c2++){
			Thing t = new Thing(this,Thing.RECTANGLE);
			t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
			t.putatpos(rand.nextInt(WORLDSIZEX) , rand.nextInt(WORLDSIZEY) );
			t.yt=rand.nextDouble();
			t.xt=rand.nextDouble();
			thingz.add(t);				
			thingz.get(c2).shapenum=c2;
		}
		//add triangles
		for (int c2=0;c2<2;c2++){
			Thing t = new Thing(this,Thing.TRIANGLE);
			t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
			t.putatpos(rand.nextInt(WORLDSIZEX) , rand.nextInt(WORLDSIZEY) );
			t.yt=rand.nextDouble();
			t.xt=rand.nextDouble();
			thingz.add(t);				
			thingz.get(c2).shapenum=c2;
		}
		
		//scenery objects that move slowly or sit there
		for (int f4=0;f4<0;f4++){
			Thing t = new Thing(this,POLYGON);
			t.xt=0;t.yt=0;
			t.setrot(Trig.norm(rand.nextDouble()*1000%pi));
			t.putatpos(rand.nextInt(WORLDSIZEX),rand.nextInt(WORLDSIZEY) );
			t.update(this);
			t.setiamscenery(true);
			sceneryz.add(t);
			}

		thman.putatpos(WORLDSIZEX/2,50);
		thman.setonsurface(false);
		thingz.add(thman);
		
		worldbord.addspoke(new ThSpoke(110,110,0,0));
		worldbord.addspoke(new ThSpoke(110,110,WORLDSIZEX,0));
		worldbord.addspoke(new ThSpoke(110,110,WORLDSIZEX,WORLDSIZEY));
		worldbord.addspoke(new ThSpoke(110,110,0,WORLDSIZEY));
		worldbord.colspoke0 = worldbord.spokes[2];
		worldbord.colspoke1 = worldbord.spokes[3];
		worldbord.setPolyin(worldbordin);
		worldbord.setPolyout(worldbordout);
		
		//some experimental shapes to add:: 
		
		ThSpoke thspoke = new ThSpoke(50,80,40,60);
		triangle.setpolyin  (new Color(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256)) );
		triangle.addspoke(thspoke);
		thspoke = new ThSpoke(50,80,60,60);
		triangle.addspoke(thspoke);
		thspoke = new ThSpoke(50,80,50,100);
		triangle.addspoke(thspoke);
		triangle.xt=0;
		triangle.yt=0.5;
		triangle.rt=0;//-0.02;//Math.random()*0.2-0.1;
		triangle.putatpos(330, 40);
		triangle.setrot(pi);
		thingz.add(triangle);
	}
	
	public void update(){
		//go through all of your objects and cause them to update.
		Thing t1;
		for (int c9 = 0;c9<thingz.size();c9++) {
			t1=thingz.get(c9);
			t1.update(this);
			if (t1.ttl < 0){
				thingz.remove(c9);
			}
		}
		for (int c9 = 0;c9<sceneryz.size();c9++) {
			t1=sceneryz.get(c9);
			t1.update(this);
			if (t1.ttl < 0){
				sceneryz.remove(c9);
			}
		}

		if (collisionson)
			{collision.collisions(thingz, this);}
		
		missilewait--;

		
		if (worldboxon){
			//update all the "thingz" - objects that move, collide, including the man
			for (int c9 = 0;c9<thingz.size();c9++) 
			{
				if(thingz.get(c9).getrb()>=worldbord.getrb()){
					thingz.get(c9).hitrt();
				}else
				if(thingz.get(c9).getlb()<=worldbord.getlb()){
					thingz.get(c9).hitlf();				
				}else
				if(thingz.get(c9).gettb()<=worldbord.gettb()){
					thingz.get(c9).hittop();				
				}else
				if(thingz.get(c9).getbb()>=worldbord.getbb()){
					thingz.get(c9).hitbot();
				}
			}			
			
			//update all the sceneryz - do not collide 
			for (int c5 = 0;c5<sceneryz.size();c5++) 
			{
				if(sceneryz.get(c5).getrb()>=worldbord.getrb()){
					sceneryz.get(c5).hitrt();
				}else
				if(sceneryz.get(c5).getlb()<=worldbord.getlb()){
					sceneryz.get(c5).hitlf();				
				}else
				if(sceneryz.get(c5).gettb()<=worldbord.gettb()){
					sceneryz.get(c5).hittop();				
				}else
				if(sceneryz.get(c5).getbb()>=worldbord.getbb()){
					sceneryz.get(c5).hitbot();
				}
			}			
		}
	}

	public void collisionsset(Boolean b){
		collisionson = b;
	}

	public void addmissile(int x,int y, double direction){
		if (missilewait < 0){
			ThMissile m = new ThMissile(this,x,y);
			m.setdirection(direction);
			thingz.add(m);
			missilewait = 5;
		}
	};
	
	public void destroything(Thing t1, Thing t2){
		if ((t1.shapenum==SHAPE_MISSILE)&&(t2.shapenum==SHAPE_MISSILE)){}
		else{
			if (t1 != null) {thingz.remove(t1);}
			if (t2 != null) {thingz.remove(t2);}
		}
	}
	
	public void spawnshapes(){
		//put some new shapes on the board! 
		
		if (thingz.size()<5){
			Thing t = new Thing(this,POLYGON);
			thingz.add(t);
			t = new Thing(this,POLYGON);
			thingz.add(t);
			t = new Thing(this,POLYGON);
			thingz.add(t);
		}
	}

	public boolean isCollisionson() {
		// TODO Auto-generated method stub
		return collisionson;
	}

	public void setCollisionson(boolean b) {
		// TODO Auto-generated method stub
		collisionson=b;
	}

	public double getGravity_current() {
		return gravity_current;
	}

	public void setGravity_current(double gravityCurrent) {
		//accepts a new factor to multiply the default gravity by
		//should be in the range 0 < gravityCurrent <= 1 
		//where 0 is no gravity and 1 is the max gravity the system can implement
		gravity_current = gravityCurrent * gravity;
	}

}	

