package terraWorld.terraBuffs;


import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.EnumRarityColor;
import DummyCore.Utils.MiscUtils;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISpecialArmor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BT_Utils {

	public static String[] armorEffects = {
		"experience",
			"eatheal1",
			"xpheal1",
			"entitydamagereduce",
			"entitydamagereducewhenlowhealth",
			"entitydamagereducewhenstrongenemy",
			"savebonemeal",
			"speedwhenhited",
			"resistancewhenhited",
			"absorptionwhenhited",
			"poisonenemywhenhited",
			"slowenemywhenhited",
			"weaknessenemywhenhited",
			"witherenemywhenhited",
			"magicFind",
			"inherit"
	};

	public static void addRandomEffects(ItemStack stk, double magicfind)
	{
		Random random = new Random();
		int type = getISType(stk);
		if(type != -1)
		{
			if(type == 0)
			{
				MiscUtils.createNBTTag(stk);
				NBTTagCompound itemTag = stk.getTagCompound();
				NBTTagCompound buffsTag = new NBTTagCompound();
				String originalName = null;
				if(itemTag.hasKey("BT_TagList"))
				{
					originalName = itemTag.getString("BT_OriginalName");
					itemTag.removeTag("BT_TagList");
				}
				BT_Effect effect = BT_EffectsLib.getRandomEffect(type);
				List<DummyData> l = effect.getEffects();
				List<DummyData> l2 = new LinkedList<DummyData>();
				for(DummyData dummyData : l) l2.add(dummyData);

				if(random.nextDouble()<0.15 * (1 + magicfind/5) ) {
					double dr = random.nextDouble();
					double a = 0;
					double th = 0.5;
					while (dr < th) {
						a += 0.01;
						th /= 2;
					}
					if (a != 0) l2.add(new DummyData("stealth", a));
				}

				double g;
				if(random.nextDouble()< 0.15 * (1 + magicfind/5) )
				{
					g = random.nextGaussian()/4;
					while (g>1 || g<-1 || Math.abs(g)<0.01)  g = random.nextGaussian()/4;
					l2.add(new DummyData("magicFind",g));
				}

				if(random.nextDouble()<0.07 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (16 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("poison",g));
				}

				if(random.nextDouble()<0.07 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (12 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("slow",g));
				}

				if(random.nextDouble()<0.07 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (13 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("weakness",g));
				}

				if(random.nextDouble()<0.15 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (4 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("experience",g));
				}

				if(random.nextDouble()<0.07 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (30 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("damageboost",g));
				}

				if(random.nextDouble()<0.07 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (16 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("damagetoburn",g));
				}

				if(random.nextDouble()<0.2 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (1 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("inherit",g));
				}

				for(int i = 0; i < l2.size(); ++i)
				{
					DummyData d = l2.get(i);
					DataStorage.addDataToString(d);
				}
				String data = DataStorage.getDataString();
				buffsTag.setString("BT_Buffs", data);
				buffsTag.setString("BT_UUID", UUID.randomUUID().toString());
				if(originalName == null || originalName.isEmpty())
					itemTag.setString("BT_OriginalName", stk.getDisplayName());
				NBTTagCompound display = new NBTTagCompound();
				if(itemTag.hasKey("display"))
				{
					display = itemTag.getCompoundTag("display");
				}
				if(originalName == null || originalName.isEmpty())
				{
					display.setString("Name", effect.getColor()+effect.getName()+""+stk.getDisplayName());
				}
				else
				{
					display.setString("Name", effect.getColor()+effect.getName()+""+originalName);
				}
				itemTag.setTag("display", display);
				itemTag.setTag("BT_TagList", buffsTag);
				stk.setTagCompound(itemTag);
			}
			else if(type == 1)
			{
				MiscUtils.createNBTTag(stk);
				NBTTagCompound itemTag = stk.getTagCompound();
				NBTTagCompound buffsTag = new NBTTagCompound();

				//BT_Effect effect = BT_EffectsLib.getRandomEffect(type);
				//List<DummyData> l = effect.getEffects();
				List<DummyData> l2 = new LinkedList<DummyData>();
				//for(DummyData dummyData : l) l2.add(dummyData);

				double g;

				if(random.nextDouble()<0.25 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (1 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("inherit",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (4 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("experience",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("speedwhenhited",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("resistancewhenhited",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("absorptionwhenhited",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("poisonenemywhenhited",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("slowenemywhenhited",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("weaknessenemywhenhited",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (6 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("posionresist",g));
				}
				if(random.nextDouble()<0.05 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (6 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("posionresistmax",g));
				}
				if(random.nextDouble()<0.04 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (40 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("witherenemywhenhited",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (50 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("entitydamagereduce",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (50 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("entitydamagereducewhenlowhealth",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (50 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("entitydamagereducewhenstrongenemy",g));
				}
				if(random.nextDouble()<0.13 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (2 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("eatheal1",g));
				}
				if(random.nextDouble()<0.13 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (2 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("xpheal1",g));
				}
				if(random.nextDouble()<0.08 * (1 + magicfind/5) )
				{
					while ((g = Math.abs(random.nextGaussian() / (5 * (1 + magicfind/20)) )) > 1 || g < 0.01) {}
					l2.add(new DummyData("savebonemeal",g));
				}
				if(random.nextDouble()< 0.25 * (1 + magicfind/5) )
				{
					g = random.nextGaussian()/4;
					while (g>1 || g<-1 || Math.abs(g)<0.01)  g = random.nextGaussian()/4;
					l2.add(new DummyData("magicFind",g));
				}

				for(int i = 0; i < l2.size(); ++i)
				{
					DummyData d = l2.get(i);
					DataStorage.addDataToString(d);
				}
				String data = DataStorage.getDataString();
				buffsTag.setString("BT_Buffs", data);
				buffsTag.setString("BT_UUID", UUID.randomUUID().toString());

				BT_Effect effect = BT_EffectsLib.getRandomEffect(type);
				String color = "";
				if (l2.size() == 0) {
					color = EnumRarityColor.BROKEN.getRarityColor();
				} else if (l2.size() == 1) {
					color = EnumRarityColor.COMMON.getRarityColor();
				} else if (l2.size() == 2) {
					color = EnumRarityColor.GOOD.getRarityColor();
				} else if (l2.size() == 3) {
					color = EnumRarityColor.UNCOMMON.getRarityColor();
				} else if (l2.size() == 4) {
					color = EnumRarityColor.RARE.getRarityColor();
				} else if (l2.size() == 5) {
					color = EnumRarityColor.UNIQUE.getRarityColor();
				} else if (l2.size() == 6) {
					color = EnumRarityColor.EPIC.getRarityColor();
				} else if (l2.size() >= 7) {
					color = EnumRarityColor.LEGENDARY.getRarityColor();
				}


				String originalName = null;
				if(itemTag.hasKey("BT_TagList"))
				{
					originalName = itemTag.getString("BT_OriginalName");
					itemTag.removeTag("BT_TagList");
				}
				if(originalName == null || originalName.isEmpty())
					itemTag.setString("BT_OriginalName", stk.getDisplayName());
				NBTTagCompound display = new NBTTagCompound();
				if(itemTag.hasKey("display"))
				{
					display = itemTag.getCompoundTag("display");
				}
				if(originalName == null || originalName.isEmpty())
				{
					display.setString("Name", color + effect.getOnlyName()+""+stk.getDisplayName());
				}
				else
				{
					display.setString("Name", color + effect.getOnlyName()+""+originalName);
				}
				itemTag.setTag("display", display);


				itemTag.setTag("BT_TagList", buffsTag);
				stk.setTagCompound(itemTag);
			}
		}
	}
	
	public static void addRandomEffects(ItemStack stk)
	{
		addRandomEffects(stk, 0.0);
	}
	
	public static int getISType(ItemStack stk)
	{
		if(isItemBuffable(stk))
		{
			if(stk.getItem() instanceof ItemArmor || stk.getItem() instanceof ISpecialArmor)
				return 1;
			return 0;
		}
		return -1;
	}
	
	public static boolean isItemBuffable(ItemStack stk)
	{
		boolean enable = false;
		if(!enable)
			enable = isTConstructTool(stk);
		return (stk != null && stk.getItem() != null && !(stk.getItem() instanceof ItemBlock) && stk.getItem().isItemTool(stk)) || enable;
	}
	
	public static boolean itemHasEffect(ItemStack stack)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("BT_TagList"))
		{
			NBTTagCompound tag = (NBTTagCompound)stack.getTagCompound().getTag("BT_TagList");
			if(tag.hasKey("BT_Buffs"))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTConstructTool(ItemStack stk)
	{
		if(stk == null || stk.getItem() == null)return false;
		try
		{
			Class clazz = Class.forName("tconstruct.library.tools.ToolCore");
			Class toolClazz = stk.getItem().getClass();
			return clazz.isAssignableFrom(toolClazz);
		}catch(Exception e)
		{
			return false;
		}
	}


}
