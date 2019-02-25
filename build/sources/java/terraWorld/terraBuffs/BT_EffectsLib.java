package terraWorld.terraBuffs;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class BT_EffectsLib {
	public static Random rand = new Random();
	
	public static Hashtable<String,BT_Effect> effects = new Hashtable();
	
	public static List<BT_Effect> effects_list = new ArrayList();
	
	public static List<BT_Effect> tools_effects_list = new ArrayList();
	
	public static List<BT_Effect> armor_effects_list = new ArrayList();
	
	public static BT_Effect getRandomEffect()
	{
		return effects_list.get(rand.nextInt(effects_list.size()));
	}
	
	public static BT_Effect getRandomEffect(int type)
	{
		BT_Effect effect = effects_list.get(rand.nextInt(effects_list.size()));
		if(effect.getName().contains("传说") || effect.getName().contains("史诗") || effect.getName().contains("传奇")) {
			if(rand.nextDouble()<0.3) return effect;
			else return effects_list.get(rand.nextInt(effects_list.size()));
		}
		else return effect;

	}

}
