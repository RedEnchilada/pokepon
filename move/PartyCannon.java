//: move/PartyCannon.java

package pokepon.move;

import pokepon.enums.*;
import pokepon.pony.Pony;

/**
 * Pinkie Pie's signature move;
 * Special move which inflicts good damage;
 * May confuse the enemy.
 *
 * @author Giacomo Parolini
 */

public class PartyCannon extends Move {
	
	public PartyCannon() {
		super("Party Cannon");
		
		type = Type.LAUGHTER;
		moveType = Move.MoveType.SPECIAL;
		maxpp = pp = 10;
		baseDamage = 90;
		accuracy = 100;
		priority = 0;
		description = "Time for a PARTY!!! Inflict big damage with a chance to confuse the foe.";
		briefDesc = "30% to confuse the target.";
		
		targetConfusion = 0.3f;	//30% to confuse
	}

	public PartyCannon(Pony p) {
		this();
		pony = p;
	}
}
