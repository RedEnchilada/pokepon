//: gui/animation/Fade.java

package pokepon.gui.animation;

import pokepon.gui.animation.*;
import pokepon.util.Debug;
import pokepon.gui.*;
import static pokepon.util.MessageManager.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.util.*;

/** Animation used to fade in/out a sprite from a point A to B;
 * the given TransparentLabel will transition from initialOpacity
 * (default: 1) to finalOpacity (default: 0), which can be explicitly
 * set, or implicitly by giving the fadeOut option.
 * 
 * @author Giacomo Parolini
 */
public class Fade extends BasicAnimation {

	private boolean forward;
	private Point initialPoint, finalPoint;
	private int vx, vy;
	private TransparentLabel tSprite;
	private float initialDistance;
	private boolean accelerated;
	private float perc, prevPerc;
	private float initialOpacity = 1f, finalOpacity;

	@SuppressWarnings("unchecked")
	/** @param opts Opts: fadeOut, initialPoint, finalPoint, finalOpacity, rewind */
	public Fade(final JComponent panel,Map<String,Object> opts) {
		super(panel,opts);
		if(delay == -1) delay = 25;
		for(Map.Entry<String,Object> entry : opts.entrySet()) {
			if(entry.getKey().equals("fadeOut")) {
				if((Boolean)entry.getValue()) {
					finalOpacity = 0f;
					initialOpacity = 1f;
				} else {
					finalOpacity = 1f;
					initialOpacity = 0f;
				}

			} else if(entry.getKey().equals("accelerated")) {
				accelerated = (Boolean)entry.getValue();

			} else if(entry.getKey().equals("initialPoint")) {
				if(entry.getValue() instanceof String) 
					initialPoint = parseShift((String)entry.getValue());
				else 
					initialPoint = (Point)entry.getValue();
				
			} else if(entry.getKey().equals("finalPoint")) {
				if(entry.getValue() instanceof String) 
					finalPoint = parseShift((String)entry.getValue());
				else 
					finalPoint = (Point)entry.getValue();
				
			} else if(entry.getKey().equals("finalOpacity")) {
				finalOpacity = (Float)entry.getValue();

			} else if(entry.getKey().equals("initialOpacity")) {
				initialOpacity = (Float)entry.getValue();
			}
		}
		
		if(initialPoint == null)
			initialPoint = sprite.getLocation();
			
		if(initialPoint == null || finalPoint == null)
			throw new RuntimeException("[Fade] initialPoint or finalPoint not set!");
		if(!(sprite instanceof TransparentLabel))
			throw new RuntimeException("[Fade] sprite should be a TransparentLabel!");

		tSprite = (TransparentLabel)sprite;

		if(Debug.on) printDebug("initialPoint = "+initialPoint+"\nfinalPoint = "+finalPoint);
		tSprite.setBounds((int)initialPoint.getX(),(int)initialPoint.getY(),
			sprite.getWidth(),sprite.getHeight());

		tSprite.setOpacity(initialOpacity);

		vx = (int)((float)(finalPoint.getX() - initialPoint.getX())/numIterations);
		vy = (int)((float)(finalPoint.getY() - initialPoint.getY())/numIterations);
		initialDistance = (float)distance((int)initialPoint.getX(),(int)initialPoint.getY(),finalPoint);
		perc = 1f;
	}

	private double distance(int x,int y,Point p) {
		return Math.sqrt(Math.pow(x - p.getX(),2) + Math.pow(y - p.getY(),2)); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int x = tSprite.getX();
		int y = tSprite.getY();
		prevPerc = perc;
		perc = (float)distance(x,y,finalPoint) / initialDistance;
		if(accelerated) {
			vx *= 1.4;
			vy *= 1.4;
		}
		
		x += vx;
		y += vy;
		// update sprite location
		tSprite.setLocation(x,y); 
		tSprite.setOpacity(finalOpacity + perc * (initialOpacity - finalOpacity));
		panel.validate();
		panel.repaint();

		if(Debug.pedantic) {
			printDebug("Location: "+tSprite.getLocation());
			printDebug("perc: "+perc);
		}

		panel.repaint();

		if(prevPerc < perc) {
			if(!persistent) {
				panel.remove(tSprite);
				sprite.setLocation(finalPoint);
			} else {
				if(rewind) {
					if(Debug.on) printDebug("[Fade] rewinding.");
					tSprite.setOpacity(initialOpacity);
					tSprite.setLocation(initialPoint);
				} else if(rewindTo != null) {
					if(Debug.on) printDebug("[Fade] rewinding to "+rewindTo+".");
					tSprite.setOpacity(initialOpacity);
					tSprite.setLocation(rewindTo);
				} else {
					tSprite.setOpacity(finalOpacity);
					tSprite.setLocation(finalPoint);
				}
			}
			if(Debug.on) printDebug("[Fade] terminating; opacity = "+tSprite.getOpacity());
			terminate(e);
		}
	}
}

