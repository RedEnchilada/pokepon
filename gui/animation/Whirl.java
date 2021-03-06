//: gui/animation/Whirl.java

package pokepon.gui.animation;

import pokepon.gui.animation.*;
import pokepon.util.Debug;
import static pokepon.util.MessageManager.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

/** An animation that mimics the Pok&#233mon Showdown 'shake' animation.
 * 
 * @author Giacomo Parolini
 */
public class Whirl extends BasicAnimation {

	/** Determines the formula used to compute velocity */
	private int count = 0;
	private int shakes = 1;
	private float t = 0;
	private int period = 400;

	@SuppressWarnings("unchecked")
	/** @param opts Opts: shakes */ 
	public Whirl(final JComponent panel,Map<String,Object> opts) {
		super(panel,opts);
		if(delay == -1) delay = 10;
		initialX = sprite.getX();
		initialY = sprite.getY();
		for(Map.Entry<String,Object> entry : opts.entrySet()) {
			if(entry.getKey().equals("shakes")) {
				shakes = (Integer)entry.getValue();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int radius = 30;
		double omega = 2*Math.PI/period;
		int x = sprite.getX(), y = sprite.getY();
	
		// update sprite location
		x -= radius*omega*delay*Math.cos(-omega*t);
		y += radius*omega*delay*Math.sin(-omega*t);
		t += delay;
		sprite.setLocation(x, y);

		if(Debug.pedantic) {
			printDebug("Location: "+sprite.getLocation());
			printDebug("t: "+t);
		}
		panel.repaint();
					
		if(t % period == (int)(period*.25)) 
			sprite.setLocation(initialX-radius,initialY-radius);
		if(t % period == (int)(period/2)) 
			sprite.setLocation(initialX,initialY-2*radius);
		if(t % period == (int)(period*.75)) 
			sprite.setLocation(initialX+radius,initialY-radius);
		if(t % period == 0) 
			sprite.setLocation(initialX,initialY);
		if(t >= period*shakes) {
			// put the sprite to its original coordinates
			sprite.setLocation(initialX,initialY);
			panel.repaint();
			terminate(e);
		}
	}
}

