package com.aptifuge.thingworld;

//ThSpoke is a simple thing: a line defined as an origin, a length, and an angle from 0-2pi radians
// x increases to the right, y increases down
public class ThSpoke implements Constance{
	
	public double ox,oy,x2,y2;
	public double ang;
	public double len;
	
	public ThSpoke(double ox, double oy, double len, double ang){
		this.ox = ox; this.oy = oy; 
		this.ang = Trig.norm(ang); 
		this.len = len;
	}
	
	public ThSpoke(int ox,int oy,int x2, int y2) {
		this.ox = ox; this.oy = oy;
		this.x2 = x2; this.y2 = y2;
		ang = Trig.norm( Math.atan2((y2-oy),(x2-ox)) );
		len = Math.sqrt(Math.pow((ox-x2),2)+Math.pow((oy-y2),2));
	}
	
	public void incox(double iox){
		this.ox += iox;
	}
	
	public void incoy(double ioy){
		this.oy += ioy;
	}
	
	public void incang(double i){
		//give me an i to increment/decrement the angle;
		ang += i;
		ang = Trig.norm(ang);
	}
	
	
	public int getox(){
		return (int)Math.round(ox);
	}
	public int getoy(){
		return (int)Math.round(oy);
	}
	public int getx2(){
		//x2=ox+(Trig.costable[(int)(ang)]*len);
		x2=this.ox+((Math.cos(ang)*len));		
		return (int)Math.round(x2);
	}
	public int gety2(){
		//y2=oy+(Trig.sintable[(int)(ang)]*len);
		y2=this.oy+((Math.sin(ang)*len));
		return (int)Math.round(y2);	
	}	
}
