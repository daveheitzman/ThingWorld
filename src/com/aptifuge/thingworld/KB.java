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

import javax.swing.*;
import java.awt.event.*;

public class KB extends KeyAdapter implements Constance{
	public static double keytime[]=new double[16];
	//keytime returns the length of the time key has been held down, or if the key is up, it stores
	//the length of the last time when it was held down. 
	public static boolean keydown[]=new boolean[16];
	public static boolean jumpexpired=false; //it's when the jump button is held down but the jump
											// has been executed already
											//or when the jump has been executed already -- reset on 
											//next call in other words, if key is not pressed.
	public static double jumpstarttime=0;

	public KB(){
	
	}
	
	public static void keypressed(int kn){
		if (kn == KEY_JUMP){
			if (keydown[kn]){
				keytime[kn]= System.nanoTime()-jumpstarttime;
			}else{
				keytime[kn]=0;
				jumpstarttime=System.nanoTime();
				jumpexpired=false;
				keydown[kn]=true;				
			}
		}else {
		if	(kn<=16){
			if (!keydown[kn]) {
				keydown[kn]=true;
				keytime[kn]=System.nanoTime();
			}
//		printkeystates();
		}
	}
//	System.out.println("Key "+kn+" keypressed.");
	}
	public static void keyreleased(int kn) {
		if (kn == KEY_JUMP){
			if (jumpexpired){
				keytime[kn]=0;
				jumpexpired=false;
			}else{
				keydown[kn]=false;
				keytime[kn]=System.nanoTime()-keytime[kn];
			}
//		printkeystates();
		}else {
		if (kn<=16) {
			keydown[kn]=false;
			keytime[kn]=System.nanoTime()-keytime[kn];
			}
//		printkeystates();
		}
		//		System.out.println(System.nanoTime()-keytime[kn]);
	}
	public static double getkeytime(int kn){
		//returns the length in nanoseconds that a key has been held down
		if (kn<=16) {
/*
			if (keydown[kn]) {
				double time =System.nanoTime()-keytime[kn]; 
				//keytime[kn]=0;
				//keydown[kn]=false;
				return time;
			}else
			{return 0.0;}
*/
			return keytime[kn];
		}
		else return 0.0;
	}
	public static void printkeystates(){
		for (int t5 = 0;t5<16; t5++){
//			System.out.println(getkeytime(t5));
		}
	}
	public static void makejumpexpired () {
		jumpexpired=true;
	}
}
