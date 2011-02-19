package com.aptifuge.thingworld;

import java.awt.Point;
import java.awt.Polygon;



public class ViewSquare implements Constance {
	
	//these are the coordinates in the world matrix that the view maps onto.	
	private int vx1,vy1,vx2,vy2,xoffcenter=0,yoffcenter=winy/2 - 110;
	
	//the upperleft of the viewsquare is alwasy 0,0, and the lowerrightx, y 
	// are xsize - 1 and ysize -1
//	private int xsize,ysize,lowerrightx,lowerrighty;
	
	private Thing centerThing;
	private Point centerPoint = new Point(WORLDSIZEX / 2,WORLDSIZEY / 2);
	private Polygon temppolygon = new Polygon();
	//private Point temppoint = new Point (0,0);

	public ViewSquare () 
	{
		vx1 = 1; vy1 = 1; vx2 = winx; vy2 = winy;
	}
	
	public ViewSquare (Thing cthing) 
	{
		centerThing = cthing;
		realign();
	}

	
	public Polygon txworld2screen(Polygon p)
	{
		//takes a Polygon in world coordinates and returns a polygon in 
		//viewSquare coordinates
		//realign(); -- not every time. 
		temppolygon.reset();
		for (int d2 = 0;d2<p.npoints;d2++){
			temppolygon.addPoint(p.xpoints[d2]-vx1, p.ypoints[d2]-vy1);
		}
		return temppolygon;
	}
	
	public Point txworld2screen(Point p){
		return new Point(p.x-vx1,p.y-vy1);
	}

	
	public Polygon txscreen2world(Polygon p)
	{
		//takes a Polygon in screen coordinates and returns a polygon in 
		//world coordinates
		//realign(); -- not every time. 
		temppolygon.reset();
		for (int d2 = 0;d2<p.npoints;d2++){
			temppolygon.addPoint(p.xpoints[d2]+vx1-framegutterx, p.ypoints[d2]+vy1-frameguttery);
		}
		return temppolygon;
	}
	
	public Point txscreen2world(Point p){
		return new Point(p.x+vx1-framegutterx,p.y+vy1-frameguttery);
	}
	
	
	
	public void realign() 
	{
		//resets all the coordinates in case the Thing moved
		if (centerThing != null) {
			centerPoint = centerThing.getCenter();
		} 
		else {
			centerPoint = new Point(WORLDSIZEX / 2,WORLDSIZEY / 2);
		}
		vx1 = centerPoint.x - (winx/2) - xoffcenter;
		vy1 = centerPoint.y - (winy/2) - yoffcenter;
		vx2 = centerPoint.x + (winx/2) ;
		vy2 = centerPoint.y + (winy/2) ;
	}
	
	public void follownew(Thing th)
	{
		if ( th != null )
		{
			centerThing = th;
			realign();
		}
	}
	
	public void follownew(Point p)
	{
		if ( p != null )
		{
			centerPoint = p;
			centerThing = null;
		}	
	}
	
}
