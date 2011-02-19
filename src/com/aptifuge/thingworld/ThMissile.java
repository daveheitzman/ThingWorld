package com.aptifuge.thingworld;

public class ThMissile extends Thing implements Constance{

	public ThMissile(ThingWorldModel tw, int x, int y){
		super(tw);
		spawnlocx=x;
		spawnlocy=y;
		init();
	}

	
	public void init(){
		ttl=missiletimetolive;
		this.xt=defaultmissilespeed;this.yt=defaultmissilespeed;
		this.shapenum=SHAPE_MISSILE;
		this.rt = Math.random()*0.035-0.0075;//.4-0.2;
		ThSpoke thspoke;// = new ThSpoke(375,250,Math.random()*lenmax,0);
		double len=4;
		thspoke = new ThSpoke(0,0,len,0);
		addspoke(thspoke);
		thspoke = new ThSpoke(0,0,len,pi/2.0);
		addspoke(thspoke);
		thspoke = new ThSpoke(0,0,len,pi);
		addspoke(thspoke);
		thspoke = new ThSpoke(0,0,len,3*pi/2.0);
		addspoke(thspoke);		
		this.putatpos(spawnlocx,spawnlocy);
	}
	
	@Override
	public void hitrt() {
		// TODO Auto-generated method stub
	if (ttl < (missiletimetolive-2)){
		this.xt=-(Math.abs(xt));		
		adjustpos(-2.0,0.0);
	}
	}
	@Override
	public void hitlf() {
		//hit something to the left
		// TODO Auto-generated method stub
	if (ttl < (missiletimetolive-2)){
		this.xt=(Math.abs(xt));			
		adjustpos(-2.0,0.0);
		}
	}
/*	@Override
	public void hittop() {
		// TODO Auto-generated method stub
		//collisionchangetopbottom();
		tw.thingz.remove(this);
	}
	@Override
	public void hitbot() {
		// TODO Auto-generated method stub
		//collisionchangetopbottom();
		tw.thingz.remove(this);
	}	
*/
}
