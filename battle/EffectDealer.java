//: battle/EffectDealer.java

package pokepon.battle;

import pokepon.enums.*;
import pokepon.pony.*;
import pokepon.move.Move;
import pokepon.battle.*;
import pokepon.util.Debug;
import static pokepon.util.MessageManager.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.HashMap;
 
/** Base class for Moves, Items and everything that
 * can apply an effect to a Pony. 
 *
 * @author Giacomo Parolini
 */
 
public class EffectDealer implements Comparable<EffectDealer> {
	
	///////////// PUBLIC METHODS / FIELDS ////////////////
	
	public EffectDealer() {}

	public EffectDealer(String name) {
		this();
		this.name = name;
		if(Debug.pedantic) printDebug("[EffectDealer] created "+name);
	}

	public EffectDealer(String name,Pony p) {
		this(name);
		pony = p;
	}

	public String getName() { return name; }

	public int compareTo(EffectDealer other) {
		return name.compareTo(other.name);
	}

	public void reset() {}

	// probability of side effects (on target or on user)
	public float getCritical() { return critical; }
	public float getTargetParalysis() { return targetParalysis; }
	public float getTargetBurn() { return targetBurn; }
	public float getTargetPetrify() { return targetPetrify; }	// = freeze
	public float getTargetSleep() { return targetSleep; }
	public float getTargetConfusion() { return targetConfusion; }
	public float getTargetPoison() { return targetPoison; }
	public float getTargetToxic() { return targetToxic; }
	public float getTargetFlinch() { return targetFlinch; }	
	public float getUserParalysis() { return userParalysis; }
	public float getUserBurn() { return userBurn; }
	public float getUserPetrify() { return userPetrify; }	
	public float getUserSleep() { return userSleep; }
	public float getUserConfusion() { return userConfusion; }
	public float getUserPoison() { return userPoison; }
	public float getUserToxic() { return userToxic; }
	public float getUserFlinch() { return userFlinch; }
	
	// stats modifiers (SimpleEntry<modifier,probability>)
	public SimpleEntry<Integer,Float> boostUserAtk() { return userAtk; }
	public SimpleEntry<Integer,Float> boostUserDef() { return userDef; }
	public SimpleEntry<Integer,Float> boostUserSpatk() { return userSpatk; }
	public SimpleEntry<Integer,Float> boostUserSpdef() { return userSpdef; }
	public SimpleEntry<Integer,Float> boostUserSpeed() { return userSpeed; }
	public SimpleEntry<Integer,Float> boostUserAccuracy() { return userAccuracy; }
	public SimpleEntry<Integer,Float> boostUserEvasion() { return userEvasion; }
	public SimpleEntry<Integer,Float> boostTargetAtk() { return targetAtk; }
	public SimpleEntry<Integer,Float> boostTargetDef() { return targetDef; }
	public SimpleEntry<Integer,Float> boostTargetSpatk() { return targetSpatk; }
	public SimpleEntry<Integer,Float> boostTargetSpdef() { return targetSpdef; }
	public SimpleEntry<Integer,Float> boostTargetSpeed() { return targetSpeed; }
	public SimpleEntry<Integer,Float> boostTargetAccuracy() { return targetAccuracy; }
	public SimpleEntry<Integer,Float> boostTargetEvasion() { return targetEvasion; } 
	
	// healing effects
	public float healUser() { return healUser; }
	public float healAllTeamStatus() { return healAllTeamStatus; }
	public float removeUserNegativeStatModifiers() { return removeUserNegativeStatModifiers; }
	public float removeUserPositiveStatModifiers() { return removeUserPositiveStatModifiers; }
	public float removeTargetNegativeStatModifiers() { return removeTargetNegativeStatModifiers; }
	public float removeTargetPositiveStatModifiers() { return removeTargetPositiveStatModifiers; }
	public float healUserStatus() { return healUserStatus; }
	public float healTargetStatus() { return healTargetStatus; }
	
	// additional effects
	public float getRecoil() { return recoil; }
	public int damageUser() { return damageUser; }
	public int damageTarget() { return damageTarget; }
	public float damageUserPerc() { return damageUserPerc; }
	public float damageTargetPerc() { return damageTargetPerc; }
	public boolean invertWeaknessAndResistance() { return invertWeaknessAndResistance; }
	public boolean ignoreWeaknesses() { return ignoreWeaknesses; }
	public boolean ignoreResistances() { return ignoreResistances; }
	public boolean ignoreImmunities() { return ignoreImmunities; }
	public boolean ignoreBoosts(String what) { return false; }
	public boolean ignoreNegativeBoosts(String what) { return false; }
	public WeatherHolder changeWeather() { return changeWeather; }
	public boolean transformsUser() { return transformsUser; }
	public boolean transformsTarget() { return transformsTarget; }
	public Pony transformInto(final BattleEngine be) { return null; }
	public boolean ignoreProtection() { return ignoreProtection; }
	public boolean maximizeHits() { return maximizeHits; }
	public boolean protectUser() { return protectUser; }
	public boolean tauntTarget() { return tauntTarget; }
	/** 0: false, 1: to chosen pony, 2: to random pony,
	 * 3: to chosen pony on damage, 4: to random pony on damage 
	 */
	public byte forceUserSwitch() { return forceUserSwitch; }
	public byte forceTargetSwitch() { return forceTargetSwitch; }
	public float changeDamageDealtBy(Type t) { return 1f; }
	public float changeDamageDealtBy(Move.MoveType mt) { return 1f; }
	//public SimpleEntry<String,PersistentEffect> spawnPersistentEffect() { return null; }
	/** Optional: string to be displayed for certain events, like damaging or
	 * healing a pony.
	 */
	public String getPhrase() { return null; }
	/* These two should be implemented only if a move transforms both user and
	 * target into 2 different ponies. Else, transformInto() will be used for both.
	 */
	public Pony transformUserInto(final BattleEngine be) { return null; }
	public Pony transformTargetInto(final BattleEngine be) { return null; }
	public boolean removesAllyHazards() { return removesAllyHazards; }
	public boolean removesEnemyHazards() { return removesEnemyHazards; }
	public boolean preventsUserOHKO() { return preventsUserOHKO; }
	public boolean preventsTargetOHKO() { return preventsTargetOHKO; }
	public boolean spawnSubstitute() { return spawnSubstitute; }
	public boolean ignoreStatusDrop() { return ignoreStatusDrop; }
	public boolean ignoreStatusChange() { return ignoreStatusChange; }
	public boolean preventsCriticalHits() { return preventsCriticalHits; }
	public boolean negateSecondaryDamage() { return negateSecondaryDamage; }
	
	// protective effects
	/** Multiplies damage coming from type t by its return value */
	public float changeDamageTakenFrom(Type t) { return 1f; }
	/** Multiplies damage coming from MoveType mt by its return value */
	public float changeDamageTakenFrom(Move.MoveType mt) { return 1f; }

	// animations
	/** @return map { "name": name, [opts] } */
	public Map<String,Object> getAnimation() { return animation; }

	public String getDescription() { return description == null ? briefDesc : description; }
	public String getBriefDescription() { return briefDesc == null ? description : briefDesc; }
	
	// SET METHODS //
	/** Sets the pony who possesses this effect dealer. */
	public void setPony(Pony p) {
		pony = p;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//////////////// PROTECTED METHODS / FIELDS ////////////////////
	
	protected Pony pony;
	protected String name;
	protected String description;
	protected String briefDesc;

	/* Multiplicators (prob. of effects without modifiers) */
	protected float critical = 1;
	protected float targetParalysis;
	protected float targetPetrify;
	protected float targetSleep;
	protected float targetBurn;
	protected float targetConfusion;
	protected float targetPoison;
	protected float targetToxic;
	protected float targetFlinch;
	protected float userParalysis;
	protected float userPetrify;
	protected float userSleep;
	protected float userBurn;
	protected float userConfusion;
	protected float userPoison;
	protected float userToxic;
	protected float userFlinch;
	
	/* Modifiers (SimpleEntry<modifier,probability>) 
	 * (setting to 0,0 to prevent NullPointerExceptions in BattleEngine)
	 */
	protected SimpleEntry<Integer,Float> userAtk = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userDef = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userSpatk = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userSpdef = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userSpeed = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userAccuracy = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> userEvasion = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetAtk = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetDef = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetSpatk = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetSpdef = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetSpeed = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetAccuracy = addEntry(0,0f);
	protected SimpleEntry<Integer,Float> targetEvasion = addEntry(0,0f);
	
	/* Additional Effects */
	protected float recoil;
	protected int damageUser;
	protected int damageTarget;
	protected float damageUserPerc;
	protected float damageTargetPerc;
	protected boolean invertWeaknessAndResistance;
	protected boolean ignoreWeaknesses;
	protected boolean ignoreResistances;
	protected boolean ignoreImmunities; 
	protected boolean transformsUser;
	protected boolean transformsTarget;
	protected boolean ignoreProtection;
	protected boolean removesAllyHazards;
	protected boolean removesEnemyHazards;
	protected boolean maximizeHits;
	protected boolean protectUser;
	protected boolean tauntTarget;
	protected WeatherHolder changeWeather;
	protected byte forceUserSwitch;
	protected byte forceTargetSwitch;
	protected boolean preventsUserOHKO;
	protected boolean preventsTargetOHKO;
	protected boolean spawnSubstitute;
	protected boolean ignoreStatusDrop;
	protected boolean ignoreStatusChange;
	protected boolean preventsCriticalHits;
	protected boolean negateSecondaryDamage;

	
	/* Healing effects */
	protected float healUser;	//user regains HP
	protected float healUserStatus;	//user loses negative statuses.
	protected float healTargetStatus;	
	protected float healAllTeamStatus;
	protected float removeUserNegativeStatModifiers;
	protected float removeUserPositiveStatModifiers;
	protected float removeTargetNegativeStatModifiers;
	protected float removeTargetPositiveStatModifiers;

	/* Animations */
	protected Map<String,Object> animation = new HashMap<String,Object>();

	/* Utility */
	protected static <A,B> java.util.AbstractMap.SimpleEntry<A,B> addEntry(A a,B b) {
		return new java.util.AbstractMap.SimpleEntry<A,B>(a,b);
	}
}

