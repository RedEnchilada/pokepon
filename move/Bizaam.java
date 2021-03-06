//: move/Bizaam.java

package pokepon.move;

import pokepon.enums.*;
import pokepon.battle.*;
import pokepon.pony.Pony;

/**
 * Canni's signature move; sort of like Stored Power (except better)
 *
 * @author Tommaso Parolini
 */

public class Bizaam extends Move {
	
	public Bizaam() {
		super("Bizaam");
		
		type = Type.PASSION;
		moveType = Move.MoveType.SPECIAL;
		maxpp = pp = 12;
		baseDamage = 40;
		accuracy = 100;
		priority = 0;
		description = "Channel the hype! Gets more powerful when you feel better.";
		briefDesc = "+20/-20 for each positive/negative modifier (min. 20).<br>+10% to confuse for each 40 extra damage.";
	}

	public Bizaam(Pony p) {
		this();
		pony = p;
	}
	
	@Override
	public boolean validConditions(final BattleEngine be) {
		if(pony == null)
			throw new NullPointerException("Pony is null for Bizaam!");

		baseDamage = 40 + 20*pony.atkMod()
				+ 20*pony.defMod()
				+ 20*pony.spatkMod()
				+ 20*pony.spdefMod()
				+ 20*pony.speedMod()
				+ 20*pony.evasionMod()
				+ 20*pony.accuracyMod();
		if(baseDamage < 20)
			baseDamage = 20;
		if(baseDamage >= 80)
			targetConfusion = ((baseDamage - 40)/40)*.1f;
		else
			targetConfusion = 0f;

		return true;
	}
}
