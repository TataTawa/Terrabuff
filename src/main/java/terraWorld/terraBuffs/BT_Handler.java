package terraWorld.terraBuffs;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.EnumRarityColor;
import DummyCore.Utils.MiscUtils;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class BT_Handler{

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onCrafting(ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		ItemStack item = event.crafting;
		if(!player.worldObj.isRemote && item != null && player.worldObj.rand.nextDouble() < 0.5)
		{
			BT_Utils.addRandomEffects(item);
		}
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent event) {
		if (event.entity.worldObj.isRemote) return;
		if (event.entityLiving instanceof EntityPlayer) return;
		DamageSource dms = event.source;
		if(dms instanceof EntityDamageSource) {
			EntityDamageSource edms = (EntityDamageSource) dms;
			if (edms.damageType.contains("player") && edms.getSourceOfDamage() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) edms.getSourceOfDamage();
				double magicfind = 0.0;
				if(player.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(player.getCurrentEquippedItem())) {
					ItemStack stack = player.getCurrentEquippedItem();
					String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
					DummyData[] d = DataStorage.parseData(dummyDataString);
					for (int i1 = 0; i1 < d.length; ++i1) {
						DummyData data = d[i1];
						String name = data.fieldName;
						double value = Double.parseDouble(data.fieldValue);
						if (name.equals("magicfind")) {
							magicfind += value;
						}
					}
				}
				for(int i=0;i<4;i++) {
					if (player.getCurrentArmor(i) != null && BT_Utils.itemHasEffect(player.getCurrentArmor(i))) {
						ItemStack stack = player.getCurrentArmor(i);
						String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
						DummyData[] d = DataStorage.parseData(dummyDataString);
						for (int i1 = 0; i1 < d.length; ++i1) {
							DummyData data = d[i1];
							String name = data.fieldName;
							double value = Double.parseDouble(data.fieldValue);
							if (name.equals("magicfind")) {
								magicfind += value;
							}
						}
					}
				}
				//if(event.entity.worldObj.rand.nextDouble() < 0.25 * (2-magicfind)) return;
				for(EntityItem entityItem : event.drops)
				{
					if(entityItem.getEntityItem().getTagCompound().getBoolean("BT_Tossed")) {
						entityItem.getEntityItem().getTagCompound().removeTag("BT_Tossed");
						continue;
					}
					if(event.entity.worldObj.rand.nextDouble() < 0.25 * (2-magicfind)) continue;
					if(BT_Utils.isItemBuffable(entityItem.getEntityItem()) && !BT_Utils.itemHasEffect(entityItem.getEntityItem()))
						BT_Utils.addRandomEffects(entityItem.getEntityItem(), magicfind);
				}
			}
		} else {
			for (EntityItem entityItem : event.drops) {
				if (event.entity.worldObj.rand.nextDouble() > 0.5) continue;
				if (BT_Utils.isItemBuffable(entityItem.getEntityItem()) && !BT_Utils.itemHasEffect(entityItem.getEntityItem()))
					BT_Utils.addRandomEffects(entityItem.getEntityItem());
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void getXP(PlayerPickupXpEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		if(player.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(player.getCurrentEquippedItem())) {
			ItemStack stack = player.getCurrentEquippedItem();
			String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
			DummyData[] d = DataStorage.parseData(dummyDataString);
			for (int i1 = 0; i1 < d.length; ++i1) {
				DummyData data = d[i1];
				String name = data.fieldName;
				double value = Double.parseDouble(data.fieldValue);
				if (name.equals("experience")) {
					event.orb.xpValue *= (1 + value);
				}
			}
		}
		for(int i=0;i<4;i++) {
			if (player.getCurrentArmor(i) != null && BT_Utils.itemHasEffect(player.getCurrentArmor(i))) {
				ItemStack stack = player.getCurrentArmor(i);
				String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
				DummyData[] d = DataStorage.parseData(dummyDataString);
				for (int i1 = 0; i1 < d.length; ++i1) {
					DummyData data = d[i1];
					String name = data.fieldName;
					double value = Double.parseDouble(data.fieldValue);
					if (name.equals("experience")) {
						event.orb.xpValue *= (1 + value);
					}
					else if (name.equals("xpheal1")) {
						if(player.worldObj.rand.nextDouble() <= value)
						{
							event.orb.xpValue -= 1;
							player.heal(1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void event_ItemTooltipEvent(ItemTooltipEvent event)
	{
		ItemStack stack = event.itemStack;
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("BT_TagList"))
		{
			NBTTagCompound tag = (NBTTagCompound)stack.getTagCompound().getTag("BT_TagList");
			if(tag.hasKey("BT_Buffs"))
			{
				String s = tag.getString("BT_Buffs");
				DummyData[] d = DataStorage.parseData(s);
				for(int i = 0; i < d.length; ++i)
				{
					String mainName = String.valueOf(d[i].fieldName.charAt(0)).toUpperCase()+d[i].fieldName.substring(1, d[i].fieldName.length());
					double da = Double.parseDouble(d[i].fieldValue) * 100;
					if(d[i].fieldName.equals("weakness"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"+"+(int)da+"%几率使敌人虚弱");
					}
					else if(d[i].fieldName.equals("poison"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"+"+(int)da+"%几率使敌人中毒");
					}
					else if(d[i].fieldName.equals("slow"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"+"+(int)da+"%几率使敌人缓慢");
					}
					else if(d[i].fieldName.equals("stealth"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"偷取生命 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("crit"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"会心一击概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("experience"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"获得经验 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("eatheal1"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"进食恢复生命概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("xpheal1"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"经验球恢复生命概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("entitydamagereduce"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"所受攻击伤害 "+"-"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("entitydamagereducewhenlowhealth"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"低生命时所受攻击伤害 "+"-"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("entitydamagereducewhenstrongenemy"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"所受强敌攻击伤害 "+"-"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("savebonemeal"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"使用骨粉时不消耗骨粉概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("speedwhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"被攻击时疾行概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("resistancewhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"被攻击时抗性提升概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("absorptionwhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"被攻击时获得临时护盾概率 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("poisonenemywhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击者中毒 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("slowenemywhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击者减速 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("weaknessenemywhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击者虚弱 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("witherenemywhenhited"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击者凋零 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("damagetoburn"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"对燃烧的敌人伤害 "+"+"+(int)da + "%");
					}
					else if(d[i].fieldName.equals("damageboost"))
					{
						event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"击中敌人"+(int)da + "%几率暂时强化力量");
					}
					else if(d[i].fieldName.equals("magicFind"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"+"+(int)da + "%" + "更佳机会取得魔法装备");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+(int)da + "%" + "更佳机会取得魔法装备");
					}
					else if(d[i].fieldName.equals("knock"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"击退敌人 "+"+"+(int)da + "%");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+"击退敌人 "+(int)da + "%");
					}
					else if(d[i].fieldName.equals("speed"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击速度 "+"+"+(int)da + "%");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+"攻击速度 "+(int)da + "%");
					}
					else if(d[i].fieldName.equals("damage"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"攻击伤害 "+"+"+(int)da + "%");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+"攻击伤害 "+(int)da + "%");
					}
					else if(d[i].fieldName.equals("durability"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.GOOD.getRarityColor()+"耐久 "+"+"+(int)da + "%");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+"耐久 "+(int)da + "%");
					}
					else if(d[i].fieldName.equals("inherit"))
					{
						if (da > 0)event.toolTip.add(EnumRarityColor.LEGENDARY.getRarityColor()+"传承概率: "+(int)da + "%");
						else event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor()+"传承概率: "+(int)da + "%");
					}
					else {
						if (da > 0)
							event.toolTip.add(EnumRarityColor.GOOD.getRarityColor() + "+" + (int) da + "% " + mainName);
						else
							event.toolTip.add(EnumRarityColor.ULTIMATE.getRarityColor() + (int) da + "% " + mainName);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void event_AttackEntityEvent(AttackEntityEvent event)
	{
		EntityPlayer p = event.entityPlayer;
		if(p.capabilities.isCreativeMode) return;
		World w = p.worldObj;
		if(p.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(p.getCurrentEquippedItem()) && !w.isRemote)
		{
			ItemStack stack = p.getCurrentEquippedItem();
			String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
			DummyData[] d = DataStorage.parseData(dummyDataString);
			for(int i1 = 0; i1 < d.length; ++i1)
			{
				DummyData data = d[i1];
				String name = data.fieldName;
				double value = Double.parseDouble(data.fieldValue);
				if(name.equals("durability"))
				{
					if(value > 0 && w.rand.nextDouble() < value && stack.getItemDamage() > 0)
					{
						stack.setItemDamage(stack.getItemDamage()-1);
					}
					if(value < 0 && w.rand.nextDouble() < -value)
					{
						stack.setItemDamage(stack.getItemDamage()+1);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void event_HarvestCheck(BreakEvent event)
	{
		EntityPlayer p = event.getPlayer();
		if(p.capabilities.isCreativeMode) return;
		World w = p.worldObj;
		if(p != null && p.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(p.getCurrentEquippedItem()) && !w.isRemote)
		{
			ItemStack stack = p.getCurrentEquippedItem();
			String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
			DummyData[] d = DataStorage.parseData(dummyDataString);
			for(int i1 = 0; i1 < d.length; ++i1)
			{
				DummyData data = d[i1];
				String name = data.fieldName;
				double value = Double.parseDouble(data.fieldValue);
				if(name.equals("durability"))
				{
					if(value > 0 && w.rand.nextDouble() < value && stack.getItemDamage() > 0)
					{
						stack.setItemDamage(stack.getItemDamage()-1);
					}
					if(value < 0 && w.rand.nextDouble() < -value)
					{
						stack.setItemDamage(stack.getItemDamage()+1);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGH)
	public void event_LivingHurtEvent(LivingHurtEvent event)
	{
		DamageSource dms = event.source;
		if(dms instanceof EntityDamageSource)
		{
			EntityDamageSource edms = (EntityDamageSource)dms;
			if(edms.damageType.contains("player") && edms.getSourceOfDamage() instanceof EntityPlayer)
			{
				EntityPlayer p = (EntityPlayer) edms.getSourceOfDamage();
				event.entityLiving.hurtResistantTime = 30;
				if(event.ammount>2) event.ammount-=1;
				double damageincrease = 0;
				if(p.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(p.getCurrentEquippedItem()))
				{
					ItemStack stack = p.getCurrentEquippedItem();
					String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
					DummyData[] d = DataStorage.parseData(dummyDataString);
					for(int i1 = 0; i1 < d.length; ++i1)
					{
						DummyData data = d[i1];
						String name = data.fieldName;
						double value = Double.parseDouble(data.fieldValue);
						if(name.equals("damage"))
						{
							damageincrease += value;
						}
						else if(name.equals("crit"))
						{
							if(p.worldObj.rand.nextDouble() <= value) {
								event.ammount *= 2;
							}
						}
						else if(name.equals("damagetoburn"))
						{
							if(event.entityLiving.isBurning()) {
								damageincrease += value;
							}
						}
						else if(name.equals("damageboost"))
						{
							if(p.worldObj.rand.nextDouble() <= value)
							{
								p.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(),2));
							}
						}
						else if(name.equals("stealth"))
						{
							p.heal((float) (event.ammount * value));
						}
						else if(name.equals("poison"))
						{
							if(p.worldObj.rand.nextDouble() <= value)
							{
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.poison.getId(),(int)(value * 100)));
							}
						}
						else if(name.equals("weakness"))
						{
							if(p.worldObj.rand.nextDouble() <= value)
							{
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.getId(),(int)(value * 100)));
							}
						}
						else if(name.equals("slow"))
						{
							if(p.worldObj.rand.nextDouble() <= value)
							{
								event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(),(int)(value * 100),2));
							}
						}
						else if(name.equals("knock"))
						{
							event.entityLiving.addVelocity((double)(-MathHelper.sin(p.rotationYaw * (float)Math.PI / 180.0F) * (float)value * 0.5F)
									, 0, (double)(MathHelper.cos(p.rotationYaw * (float)Math.PI / 180.0F) * (float)value * 0.5F));
							p.motionX *= 0.6;
							p.motionZ *= 0.6;
						}
					}
					if(damageincrease > 0.5) damageincrease = 0.5;
					event.ammount *= 1+damageincrease;
				}
			}
			if(event.entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.entity;
				double damageruduce = 0;
				for(int i=0;i<4;i++) {
					if (player.getCurrentArmor(i) != null && BT_Utils.itemHasEffect(player.getCurrentArmor(i))) {
						ItemStack stack = player.getCurrentArmor(i);
						String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
						DummyData[] d = DataStorage.parseData(dummyDataString);
						for (int i1 = 0; i1 < d.length; ++i1) {
							DummyData data = d[i1];
							String name = data.fieldName;
							double value = Double.parseDouble(data.fieldValue);
							if (name.equals("entitydamagereduce")) {
								damageruduce += value;
							}
							if (name.equals("entitydamagereducewhenlowhealth")) {
								if(player.getHealth() <= 8) damageruduce += value;
							}
							if (name.equals("entitydamagereducewhenstrongenemy")) {
								if(event.source.getEntity() instanceof EntityLiving) {
									if(((EntityLiving) event.source.getEntity()).getHealth()>80) damageruduce += value;
								}
							}
							if (name.equals("speedwhenhited")) {
								if(player.worldObj.rand.nextDouble() < value) {
									player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(),3));
								}
							}
							if (name.equals("resistancewhenhited")) {
								if(player.worldObj.rand.nextDouble() < value) {
									player.addPotionEffect(new PotionEffect(Potion.resistance.getId(),3));
								}
							}
							if (name.equals("absorptionwhenhited")) {
								if(player.worldObj.rand.nextDouble() < value) {
									player.addPotionEffect(new PotionEffect(Potion.field_76444_x.getId(),3));
								}
							}
							if (name.equals("poisonenemywhenhited")) {
								if(event.source.getEntity() instanceof EntityLiving) {
									if(player.worldObj.rand.nextDouble() < value) {
										((EntityLiving) event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.poison.getId(),3));
									}
								}
							}
							if (name.equals("slowenemywhenhited")) {
								if(event.source.getEntity() instanceof EntityLiving) {
									if(player.worldObj.rand.nextDouble() < value) {
										((EntityLiving) event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(),3,2));
									}
								}
							}
							if (name.equals("weaknessenemywhenhited")) {
								if(event.source.getEntity() instanceof EntityLiving) {
									if(player.worldObj.rand.nextDouble() < value) {
										((EntityLiving) event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.weakness.getId(),3));
									}
								}
							}
							if (name.equals("witherenemywhenhited")) {
								if(event.source.getEntity() instanceof EntityLiving) {
									if(player.worldObj.rand.nextDouble() < value) {
										((EntityLiving) event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.wither.getId(),3));
									}
								}
							}
						}
					}
				}
				if(damageruduce > 0.5) damageruduce = 0.5;
				event.ammount *= 1-damageruduce;
			}
		}
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void event_LivingHurtEvent2(LivingHurtEvent event)
	{
		DamageSource dms = event.source;
		if(dms instanceof EntityDamageSource)
		{
			EntityDamageSource edms = (EntityDamageSource)dms;
			if(edms.damageType.contains("player") && edms.getSourceOfDamage() instanceof EntityPlayer)
			{
				EntityPlayer p = (EntityPlayer) edms.getSourceOfDamage();
				if(p.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(p.getCurrentEquippedItem()))
				{
					ItemStack stack = p.getCurrentEquippedItem();
					String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
					DummyData[] d = DataStorage.parseData(dummyDataString);
					for(int i1 = 0; i1 < d.length; ++i1)
					{
						DummyData data = d[i1];
						String name = data.fieldName;
						double value = Double.parseDouble(data.fieldValue);
						if(name.equals("speed"))
						{
							MiscUtils.damageEntityIgnoreEvent(event.entityLiving, edms, event.ammount);
							int damageResistance = 30;
							damageResistance -= value * 0.85 * 30;
							event.entityLiving.hurtResistantTime = damageResistance;
							event.entityLiving.hurtTime = damageResistance;
							//p.swingProgress -= value*30;
							//p.swingProgressInt -= value*30;
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}


	@SubscribeEvent
	public void event_BreakSpeed(BreakSpeed event)
	{
		EntityPlayer p = event.entityPlayer;
		World w = p.worldObj;
		Block b = event.block;
		if(p.getCurrentEquippedItem() != null && BT_Utils.itemHasEffect(p.getCurrentEquippedItem()))
		{
			ItemStack stack = p.getCurrentEquippedItem();
			String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
			DummyData[] d = DataStorage.parseData(dummyDataString);
			for(int i1 = 0; i1 < d.length; ++i1)
			{
				DummyData data = d[i1];
				String name = data.fieldName;
				double value = Double.parseDouble(data.fieldValue);
				if(name.equals("speed"))
				{
					event.newSpeed = (float)(event.originalSpeed*(1+value/3));
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerUsedItem(PlayerUseItemEvent.Finish event) {
		if(!event.entityPlayer.worldObj.isRemote && event.item.getItem() instanceof ItemFood) {
			EntityPlayer player = event.entityPlayer;
			for(int i=0;i<4;i++) {
				if (player.getCurrentArmor(i) != null && BT_Utils.itemHasEffect(player.getCurrentArmor(i))) {
					ItemStack stack = player.getCurrentArmor(i);
					String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
					DummyData[] d = DataStorage.parseData(dummyDataString);
					for (int i1 = 0; i1 < d.length; ++i1) {
						DummyData data = d[i1];
						String name = data.fieldName;
						double value = Double.parseDouble(data.fieldValue);
						if (name.equals("eatheal1")) {
							if(player.worldObj.rand.nextDouble() < value) player.heal(1);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onBonemeal(BonemealEvent event) {
		if(!event.entityPlayer.worldObj.isRemote &&
				Item.getIdFromItem(event.entityPlayer.getCurrentEquippedItem().getItem()) == Item.getIdFromItem(new ItemDye())
				&& event.entityPlayer.getCurrentEquippedItem().getItemDamage() == 15) {
			EntityPlayer player = event.entityPlayer;
			double chanceToSaveBonemeal = 0;
			for(int i=0;i<4;i++) {
				if (player.getCurrentArmor(i) != null && BT_Utils.itemHasEffect(player.getCurrentArmor(i))) {
					ItemStack stack = player.getCurrentArmor(i);
					String dummyDataString = stack.getTagCompound().getCompoundTag("BT_TagList").getString("BT_Buffs");
					DummyData[] d = DataStorage.parseData(dummyDataString);
					for (int i1 = 0; i1 < d.length; ++i1) {
						DummyData data = d[i1];
						String name = data.fieldName;
						double value = Double.parseDouble(data.fieldValue);
						if (name.equals("savebonemeal")) {
							chanceToSaveBonemeal += value;
						}
					}
				}
			}
			if(chanceToSaveBonemeal > 0.5) chanceToSaveBonemeal = 0.5;
			if(player.worldObj.rand.nextDouble() < chanceToSaveBonemeal) {
				player.getCurrentEquippedItem().stackSize += 1;
				if(player instanceof EntityPlayerMP) {
					((EntityPlayerMP) player).sendContainerToPlayer((player).inventoryContainer);
				}
			}
		}
	}

	@SubscribeEvent
	public void onToss(ItemTossEvent event) {
		if(!event.player.worldObj.isRemote && event.entity instanceof EntityItem) {
			ItemStack stack = ((EntityItem) event.entity).getEntityItem();
			if(BT_Utils.isItemBuffable(stack) && !BT_Utils.itemHasEffect(stack)) {
				stack.getTagCompound().setBoolean("BT_Tossed",true);
			}
		}
	}

	@SubscribeEvent
	public void onPickupItem(EntityItemPickupEvent event) {
		if(!event.entityPlayer.worldObj.isRemote) {
			ItemStack stack = (event.item).getEntityItem();
			stack.getTagCompound().removeTag("BT_Tossed");
		}
	}
}
