package com.aptifuge.thingworld;

public class Trig implements Constance {


	
	public static final float[] tantable = new float[piint+1];
	public static final float[] costable = new float[piint+1];
	public static final float[] sintable = new float[piint+1];
	public static final float[] atantable = new float[piint+1];

	public Trig(){
		for (int c3 = 0;c3<(piint+1);c3++){
			tantable[c3]=(float)Math.tan((double)c3/pifac);
			costable[c3]=(float)Math.cos((double)c3/pifac);
			sintable[c3]=(float)Math.sin((double)c3/pifac);
//			atantable[c3]=(float)Math.atan((double)c3/pifac);
			//			System.out.println("c3 = "+c3);
//			System.out.println("c3/pifac = "+(double)c3/10000);
//			System.out.println("Tangent table "+c3+" "+tantable[c3]);
		}

	}
	static public double norm(double theta){
		// input: an angle theta in radians.
		// returns that angle expressed as: 0 - 2pi
		return theta<0? twopi+( theta % (twopi)): theta%twopi;
	}
	static public double atan(double x1,double y1,double x2,double y2){
		double theta = 0;
		if (x2 > x1){
			if (y2 > y1)
			{theta = Math.atan2(y2-y1,x2-x1);} else
			{theta = twopi + Math.atan2(y2-y1,x2-x1); }
		}else{
			if (y2 > y1)
			{theta = Math.atan2(y2-y1,x2-x1);} else
			{theta = Math.atan2(y2-y1,x2-x1) + pi; }			
		}
		System.out.println("Theta Calc:"+Math.atan2(y2-y1, x2-x1));
		return theta;
	}
	static public double distanceclockwise(double a1, double a2){
		//answers the question: how far do you have to go clockwise to get from a1 to a2? 
		double r1=Trig.norm(a1), r2=Trig.norm(a2);
		double ndeg=0;
		//case 1: a2 == a1: return 0; 
		if(r1==r2) 	{ndeg=0;} 		else 
		//case 2: a2 > a1 
		if(r2>r1)	{ndeg = r2-r1;} else
		//case 3: 0/2pi is between a1 and a2
		if(r1>r2)	{ndeg = Trig.norm(twopi-(r1-r2));}
//		System.out.println("distanceclockwise: "+a1+","+a2+"."+ndeg);
		return ndeg;
	}
}
