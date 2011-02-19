/*
 *	 	The MIT License 
 * 
 *		Copyright (c) <2010,2011> <David R. Heitzman > 
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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line implements Constance {
	Trig trig=new Trig();
	public int posx,posy; //xinc and yinc are how much the line moves each frame
	public int x1,y1,x2,y2,lbound,rbound, botbound, topbound;
	public double accumx,accumy,len;
	public double rotinc=0.0;
	public double rot=0.0; // eventually, hopefully in radians/sec. Negative is counter clockwise
	public double xinc,yinc;
	public long begintime,time1, time2;

	public void update(){
		rot = Trig.norm(rot + rotinc);
		accumx=accumx+xinc;
		accumy=accumy+yinc;
		posx=(int)accumx;
		posy=(int)accumy;
//		int xi=(int)(Trig.costable[(int)(rot*pifac)]*len/2);
//		int yi=(int)(Trig.sintable[(int)(rot*pifac)]*len/2);
		int xi=(int)Math.round((Math.cos(rot)*len));
		int yi=(int)Math.round((Math.sin(rot)*len));
		x1=posx;
		y1=posy;
		x2=posx+xi;
		y2=posy+yi;

		lbound = x1 > x2 ? x2:x1;
		rbound = x1 > x2 ? x1:x2;
		botbound = y1 > y2 ? y1:y2;
		topbound = y1 > y2 ? y2:y1;
	}
	public Line(){
		//(Math.random()*0.6)-0.3
		this(100,100,(int)(Math.random()*30+20),0,(Math.random()*0.6)-0.3,(Math.random()*4)-2,(Math.random()*4)-2);
	}
	public Line(int x1, int y1, int len, double rot, double rotinc, double xinc, double yinc){
		begintime=System.nanoTime();
		accumx=posx=x1;
		accumy=posy=y1;
		this.xinc=xinc;
		this.yinc=yinc;
		this.rot = rot;
		this.len = len;
		this.rotinc=rotinc;
		this.update();
	}
	public Line(int x1,int y1,int x2, int y2){
		begintime=System.nanoTime();
		accumx=posx=x1;
		accumy=posy=y1;
		this.xinc=0;
		this.yinc=0;
		this.rot = Trig.norm(Math.atan2(y2-y1, x2-x1));
		this.len = Math.hypot(x2-x1, y2-y1);
		this.rotinc=0;
		this.update();		
	}
	
}
