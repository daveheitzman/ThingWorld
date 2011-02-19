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

import java.awt.Color;
import java.awt.*;
import ec.util.*;

public interface Constance {
	public static final int tickspersecond = 30;
	public static final int maxspokes =20;
	public static final double nanospertick = 1000000000/tickspersecond;
	
	public static final int winx=1200,winy=800; //size of display window for game
	public static final int framegutterx=3,frameguttery=27; //size of title bar and other unusable parts of the JFrame
	
	public static final int WORLDSIZEX=1100,WORLDSIZEY=600; //size of the "world" of the game
	public static final int worldpanelx=winx,worldpanely=winy;
	public static final String mainwintitle = "Shapefun" ;
	public static final int ovalwidth=10,ovalheight = 10;
	public static final int KEY_UP=1, KEY_DOWN=3;
	public static final int KEY_LEFT=2, KEY_RIGHT=4;
	public static final int KEY_JUMP=5, KEY_ACT=6, KEY_HOOK=7;

	//COLORS: 
	public static final Color colorbackground= new Color (0,40,64);
	public static final Color polin = new Color (154,130,55);
	public static final Color polout = new Color (15,15,15);
	public static final Color triin = new Color (167,90,90);
	public static final Color triout = new Color (230,230,230);
	public static final Color squin = new Color (155,244,88);
	public static final Color squout = new Color (230,230,230);
	public static final Color missin = new Color (200,188,99);
	public static final Color missout = new Color (230,230,230);
	public static final Color worldbordout = new Color (0,0,0);
	public static final Color worldbordin = new Color (188,210,205);

	
	public static final double accelcap=2;
	public static final double speedcap=2;
	public static final double keylowthresh=60000000;//60 ms
	public static final double keyhighthresh=1500000000;//1500 ms
	
	public static final double horzlimit = 4;
	public static final double vertuplimit = -8;
	public static final double vertdownlimit = 12;
	public static final double friction = 0.95; //this is multiplied directly to xt -- the x incrementer
	public static final double yacceldecay=0.08;
	public static final double xacceldecay=0.03;
	public static final double gravity=0.02;//0.3;		//this gets multiplied by acceleration every cycle
	public static final double defaultmissilespeed=6;

	//shape templates:
	public static final int RANDOM=0;
	public static final int POLYGON=1;
	public static final int TRIANGLE = 2;
	public static final int SQUARE = 3;
	public static final int RECTANGLE = 4;
	public static final int SPIKY = 5;
	public static final int SHAPE_MISSILE = 32001;
	public static final int missiletimetolive = 100;
	
	
	//trigonometry stuff
	public static final int piint = 3141;
	public static final int pifac = 1000;
	public static final double pi =  3.141592653589793;
	public static final double twopi =  2.0*3.141592653589793;
	public static final double pidiv2 = 1.5707963267948966;

	public static final MersenneTwisterFast rand = new MersenneTwisterFast();

}
