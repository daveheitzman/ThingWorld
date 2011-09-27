package com.aptifuge.thingworld;

//import org.json.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ThingzReader implements Constance{
	public final String THING_FILENAME = "thingz.json";
	private java.io.FileReader fr;
	
	public 	ArrayList<Thing> getFromJSON(ThingWorldModel twm, String filename){
		ArrayList<Thing> thingz = new ArrayList<Thing>();
//		if filename is blank or filename doesn't exist, return a default shape, and write that shape to
// 		JSON file
		if (filename == null) {
			filename = THING_FILENAME;
		}  
		
		try {
			fr = new java.io.FileReader(filename);
			String outs = new String();
			JSONObject jo = new JSONObject();	
			try {
				while ( fr.ready() ) {
					outs += (String.valueOf( fr.read() ) );
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				jo.getJSONObject(outs);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("WARNING: shape file "+filename+" not found -- creating default shape. ");
			for (int c2=0;c2<2;c2++){
				Thing t = new Thing(twm,Thing.SQUARE);
				t.putatpos(rand.nextDouble()*WORLDSIZEX-50, rand.nextDouble()*WORLDSIZEY-50);
				t.yt=rand.nextDouble();
				t.xt=rand.nextDouble();
				thingz.add(t);	
				thingz.get(c2).shapenum=c2;
			}
		}

		return thingz;
	}
}
