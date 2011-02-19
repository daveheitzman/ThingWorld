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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.MenuItem;
import javax.swing.*;

public class PopupMenuHandler extends JPopupMenu implements ActionListener, MouseListener,PopupMenuListener {
	public ThingWorldController twC;
	public ThingWorldView twV;
	public ThingWorldModel twM;
	private int numberofmenuitems = 7;
	private JMenuItem popupmenuitems[] = new JMenuItem[numberofmenuitems];
	
		public PopupMenuHandler(ThingWorldController twC, ThingWorldView twV,ThingWorldModel twM) { 
			this.twC = twC;
			this.twV = twV;
			this.twM = twM;
			this.setOpaque(true);
			for (int c4 = 0;c4<numberofmenuitems;c4++){
				popupmenuitems[c4] = new JCheckBoxMenuItem();
				popupmenuitems[c4].addActionListener(this);
			}
			popupmenuitems[0].setText("Numbers On");
			popupmenuitems[0].setActionCommand("numbers");
			popupmenuitems[0].setSelected(twV.isNumberson());
			popupmenuitems[1].setText("Direction Flags On");
			popupmenuitems[1].setActionCommand("directionflags");
			popupmenuitems[1].setSelected(twV.isDirectionflags());
			popupmenuitems[2].setText("Surround Box On");
			popupmenuitems[2].setActionCommand("surroundbox");
			popupmenuitems[2].setSelected(twV.isSurroundbox());
			popupmenuitems[3].setText("Show Collision Span");
			popupmenuitems[3].setActionCommand("showcollisionspan");
			popupmenuitems[3].setSelected(twV.isShowcollisionspan());
			popupmenuitems[4].setText("Status Dump");
			popupmenuitems[4].setActionCommand("statusdump");
			popupmenuitems[4].setSelected(twV.isStatusdump());
			popupmenuitems[5].setText("Collisions On");
			popupmenuitems[5].setActionCommand("collisions");			
			popupmenuitems[5].setSelected(twM.isCollisionson());
					
			popupmenuitems[6] = new JMenuItem();
			popupmenuitems[6].setText("Follow This Thing");
			popupmenuitems[6].setActionCommand("followthis");			
			popupmenuitems[6].addActionListener(this);

			for (int c4 = 0;c4<numberofmenuitems;c4++){
				this.add(popupmenuitems[c4]);
			}

			this.setInvoker(twC);
			this.pack();
			this.revalidate();
		}


		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("Popupmenu clicked; Action command == "+arg0.getActionCommand());
			if(arg0.getActionCommand() == "numbers") {
				twV.setNumberson(!twV.isNumberson() );				
			} else
			if(arg0.getActionCommand() == "directionflags") {
				twV.setDirectionflags(!twV.isDirectionflags() );
			} else
			if(arg0.getActionCommand() == "surroundbox") {
				twV.setSurroundbox(!twV.isSurroundbox() );
			} else
			if(arg0.getActionCommand() == "showcollisionspan") {
				twV.setShowcollisionspan(!twV.isShowcollisionspan() );
			} else
			if(arg0.getActionCommand() == "statusdump") {
				twV.setStatusdump(!twV.isStatusdump() );
			} else
			if(arg0.getActionCommand() == "collisions") {
				twM.setCollisionson(!twM.isCollisionson() );
			} else
			if (arg0.getActionCommand() == "followthis"){
				if (twC.getlastclickedthing() != null) {
					twV.viewsquare.follownew(twC.getlastclickedthing());
					twC.setlastclickedthing(null);
					twC.pauseresume();
				}
				System.out.println("clicked followthis");
			}
			
		}


		@Override
		public void mouseClicked(MouseEvent e) {
			popupmenuitems[0].setSelected(twV.isNumberson());
			popupmenuitems[1].setSelected(twV.isDirectionflags());
			popupmenuitems[2].setSelected(twV.isSurroundbox());
			popupmenuitems[3].setSelected(twV.isShowcollisionspan());
			popupmenuitems[4].setSelected(twV.isStatusdump());
			popupmenuitems[5].setSelected(twM.isCollisionson());

			if (e.getButton()==MouseEvent.BUTTON3){
				this.show(e.getComponent(), e.getX(), e.getY());	
				System.out.println("popup e.getcomponent() "+e.getComponent().toString());
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}



}