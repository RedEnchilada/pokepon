//: move/Lullaby.java

package pokepon.move;

import pokepon.enums.*;
import pokepon.battle.*;
import pokepon.pony.Pony;

/**
 * Status move which may put the enemy to sleep.
 *
 * @author Giacomo Parolini
 */

public class Lullaby extends Move {
	
	public Lullaby() {
		super("Lullaby");
		
		type = Type.MUSIC;
		moveType = Move.MoveType.STATUS;
		maxpp = pp = 10;
		accuracy = 70;
		priority = 0;
		description = "Sing with melodious voice to soothe listeners' heart.";
		briefDesc = "Sets the target Asleep.";

		targetSleep = 1f;
	}

	public Lullaby(Pony p) {
		this();
		pony = p;
	}

	@Override
	public boolean validConditions(final BattleEngine be) {
		return !be.getDefender().hasNegativeCondition();
	}
}
