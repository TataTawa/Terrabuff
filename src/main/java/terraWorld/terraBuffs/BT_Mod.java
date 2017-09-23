package terraWorld.terraBuffs;

import DummyCore.Utils.DummyData;
import DummyCore.Utils.EnumRarityColor;
import DummyCore.Utils.MiscUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = "buffedtools",name = "Buffed Tools",version = "1.1.1710.1")
public class BT_Mod {
	
	static BT_Mod instance;
	File configDir;
	BT_Config config;
	static Map<String,Integer> potionMap = new HashMap<String, Integer>();

	@SidedProxy(serverSide="terraWorld.terraBuffs.BT_ServerProxy",clientSide="terraWorld.terraBuffs.BT_ClientProxy")
	static BT_ServerProxy proxy;
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		instance = this;
		configDir = event.getSuggestedConfigurationFile();
		config = new BT_Config(configDir);
		File file = new File(configDir.getAbsolutePath());
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		proxy.preload();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		MinecraftForge.EVENT_BUS.register(new BT_EventHandler());
	}
	
	public static BT_ServerProxy proxy()
	{
		return instance.proxy;
	}
	
	
	@EventHandler
	public static void onServerStarted(FMLServerStartedEvent event)
	{
		BT_EffectsLib.rand = MinecraftServer.getServer().worldServers[0].rand;
		for(Potion potion : Potion.potionTypes) {
			if(potion != null) {
				potionMap.put(potion.getName(),potion.getId());
			}
		}
	}
	

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		//GameRegistry.registerCraftingHandler(new BT_Handler());
		FMLCommonHandler.instance().bus().register(new BT_Handler());
		MinecraftForge.EVENT_BUS.register(new BT_Handler());
		//TickRegistry.registerTickHandler(new BT_TickHandler(), Side.SERVER);
		registerEffects();
		config.loadCFG();
		
		anvil = new BT_Anvil();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		if(MiscUtils.oreDictionaryContains("blockSteel"))
			GameRegistry.addRecipe(new ShapedOreRecipe(anvil,new Object[]{
					"ISI",
					"BBB",
					"III",
					'I',"ingotIron",
					'S',"blockSteel",
					'B',new ItemStack(Blocks.iron_bars,1,OreDictionary.WILDCARD_VALUE)
			}));
		else
			GameRegistry.addRecipe(new ShapedOreRecipe(anvil,new Object[]{
					"ISI",
					"BBB",
					"III",
					'I',"ingotIron",
					'S',Blocks.anvil,
					'B',new ItemStack(Blocks.iron_bars,1,OreDictionary.WILDCARD_VALUE)
			}));
	}
	
	public static BT_Anvil anvil;
	public static final String KNOCK_BACK = "knock";
	
	public void registerEffects()
	{
		BT_Effect eaa = new BT_Effect("BT.Effect.Damaged", "损坏的", EnumRarityColor.BROKEN, new DummyData("damage", -0.2D)).registerEffect();
		BT_Effect eab = new BT_Effect("BT.Effect.Dull", "平淡的", EnumRarityColor.BROKEN, new DummyData("damage", -0.20D)).registerEffect();
		BT_Effect eac = new BT_Effect("BT.Effect.Sluggish", "呆滞的", EnumRarityColor.BROKEN, new DummyData("speed", -0.69D)).registerEffect();
		BT_Effect ead = new BT_Effect("BT.Effect.Slow", "缓慢的", EnumRarityColor.BROKEN, new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect eae = new BT_Effect("BT.Effect.Lazy", "懒惰的", EnumRarityColor.BROKEN, new DummyData("speed", -0.24D)).registerEffect();
		BT_Effect eac1 = new BT_Effect("BT.Effect.Powerless3", "瘫软的", EnumRarityColor.BROKEN, new DummyData(KNOCK_BACK, -0.90D)).registerEffect();
		BT_Effect eaf = new BT_Effect("BT.Effect.Cracky", "脆弱的", EnumRarityColor.BROKEN, new DummyData("durability", -0.2D)).registerEffect();
		BT_Effect eag = new BT_Effect("BT.Effect.Broken", "破损的", EnumRarityColor.BROKEN, new DummyData("damage", -0.4D),new DummyData("speed", -0.6D)).registerEffect();
		BT_Effect eah = new BT_Effect("BT.Effect.Annoying", "恼人的", EnumRarityColor.BROKEN, new DummyData("damage", -0.2D),new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect eai = new BT_Effect("BT.Effect.Shoddy", "次品的", EnumRarityColor.BROKEN, new DummyData("damage", -0.2D),new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect eai2 = new BT_Effect("BT.Effect.Shoddy2", "弱小的", EnumRarityColor.BROKEN, new DummyData(KNOCK_BACK, -0.9D),new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect eaj = new BT_Effect("BT.Effect.Terrible", "可怕的", EnumRarityColor.BROKEN, new DummyData("damage", -0.2D),new DummyData("speed", -0.45D),new DummyData("durability", -0.08D)).registerEffect();
		BT_Effect eak = new BT_Effect("BT.Effect.Unhappy", "不悦的", EnumRarityColor.BROKEN, new DummyData("damage", -0.15D),new DummyData("speed", -0.3D),new DummyData("durability", -0.1D)).registerEffect();
		BT_Effect eak2 = new BT_Effect("BT.Effect.Unhappy2", "迷你的", EnumRarityColor.BROKEN, new DummyData("damage", -0.15D),new DummyData("speed", -0.3D),new DummyData("durability", -0.1D),new DummyData(KNOCK_BACK, -0.8D)).registerEffect();
		BT_Effect eal = new BT_Effect("BT.Effect.Heavy", "沉重的", EnumRarityColor.COMMON, new DummyData("damage", 0.15D),new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect eam = new BT_Effect("BT.Effect.Light", "轻飘的", EnumRarityColor.COMMON, new DummyData("damage", -0.15D),new DummyData("speed", 0.45D)).registerEffect();
		BT_Effect ean = new BT_Effect("BT.Effect.Ruthless", "无情的", EnumRarityColor.COMMON, new DummyData("damage", 0.2D),new DummyData("speed", -0.45D)).registerEffect();
		BT_Effect ean2 = new BT_Effect("BT.Effect.Ruthless2", "锐利的", EnumRarityColor.COMMON, new DummyData("damage", 0.2D),new DummyData(KNOCK_BACK, -0.9D)).registerEffect();
		BT_Effect ean3 = new BT_Effect("BT.Effect.Ruthless3", "趁手的", EnumRarityColor.COMMON, new DummyData(KNOCK_BACK, -0.9D),new DummyData("speed", 0.40D)).registerEffect();
		BT_Effect ean4 = new BT_Effect("BT.Effect.Ruthless4", "伙伴的", EnumRarityColor.COMMON, new DummyData(KNOCK_BACK, -0.9D),new DummyData("speed", 0.40D), new DummyData("damage", 0.15D)).registerEffect();
		BT_Effect eao = new BT_Effect("BT.Effect.Shameful", "可耻的", EnumRarityColor.COMMON, new DummyData("damage", -0.2D),new DummyData("speed", -0.6D),new DummyData("durability", 0.15D)).registerEffect();
		BT_Effect eap = new BT_Effect("BT.Effect.Bulky", "笨重的", EnumRarityColor.GOOD, new DummyData("damage", 0.05D),new DummyData("speed", -0.45D),new DummyData("durability", 0.1D)).registerEffect();
		BT_Effect eaq = new BT_Effect("BT.Effect.Nasty", "讨厌的", EnumRarityColor.GOOD, new DummyData("damage", 0.05D),new DummyData("speed", 0.03D),new DummyData("durability", 0.1D)).registerEffect();
		BT_Effect ear = new BT_Effect("BT.Effect.Sharp", "锋利的", EnumRarityColor.UNCOMMON, new DummyData("damage", 0.2D)).registerEffect();
		BT_Effect eas = new BT_Effect("BT.Effect.Pointy", "尖锐的", EnumRarityColor.UNCOMMON, new DummyData("damage", 0.1D)).registerEffect();
		BT_Effect eat = new BT_Effect("BT.Effect.Hurtful", "伤害的", EnumRarityColor.UNCOMMON, new DummyData("damage", 0.1D)).registerEffect();
		BT_Effect eat3 = new BT_Effect("BT.Effect.Hurtful3", "强击的", EnumRarityColor.UNCOMMON, new DummyData(KNOCK_BACK, 0.8D)).registerEffect();
		BT_Effect eau = new BT_Effect("BT.Effect.Strong", "强力的", EnumRarityColor.UNCOMMON, new DummyData("durability", 0.15D)).registerEffect();
		BT_Effect eav = new BT_Effect("BT.Effect.Durab", "结实的", EnumRarityColor.UNCOMMON, new DummyData("durability", 0.3D)).registerEffect();
		BT_Effect eav2 = new BT_Effect("BT.Effect.Durab2", "耐用的", EnumRarityColor.UNCOMMON, new DummyData("durability", 0.5D)).registerEffect();
		BT_Effect eaw = new BT_Effect("BT.Effect.Quick", "快速的", EnumRarityColor.UNCOMMON, new DummyData("speed", 0.3D)).registerEffect();
		BT_Effect eax = new BT_Effect("BT.Effect.Nimble", "灵活的", EnumRarityColor.UNCOMMON, new DummyData("speed", 0.15D)).registerEffect();
		BT_Effect eay = new BT_Effect("BT.Effect.Zealous", "热诚的", EnumRarityColor.UNCOMMON, new DummyData("crit", 0.02D)).registerEffect();
		BT_Effect eaz = new BT_Effect("BT.Effect.Keen", "敏锐的", EnumRarityColor.UNCOMMON, new DummyData("crit", 0.03D)).registerEffect();
		BT_Effect eba = new BT_Effect("BT.Effect.Massive", "神速的", EnumRarityColor.UNCOMMON, new DummyData("speed", 0.54D)).registerEffect();
		BT_Effect ebb = new BT_Effect("BT.Effect.Large", "大型的", EnumRarityColor.UNCOMMON, new DummyData("speed", 0.36D)).registerEffect();
		BT_Effect ebc = new BT_Effect("BT.Effect.Demonic", "恶魔的", EnumRarityColor.RARE, new DummyData("damage", 0.15D), new DummyData("crit", 0.02D)).registerEffect();
		BT_Effect ebd = new BT_Effect("BT.Effect.Agile", "敏捷的", EnumRarityColor.RARE, new DummyData("speed", 0.3D), new DummyData("crit", 0.01D)).registerEffect();
		BT_Effect ebe = new BT_Effect("BT.Effect.Deadly", "致命的", EnumRarityColor.RARE, new DummyData("damage", 0.1D), new DummyData("speed", 0.3D)).registerEffect();
		BT_Effect ebf = new BT_Effect("BT.Effect.Unpleasant", "厉害的", EnumRarityColor.RARE, new DummyData("damage", 0.05D), new DummyData("durability", 0.15D)).registerEffect();
		BT_Effect ebg = new BT_Effect("BT.Effect.Rapid", "超快的", EnumRarityColor.RARE, new DummyData("speed", 0.45D), new DummyData("durability", 0.10D)).registerEffect();
		BT_Effect ebg2 = new BT_Effect("BT.Effect.Rapid2", "超快的", EnumRarityColor.RARE, new DummyData("speed", 0.45D), new DummyData(KNOCK_BACK, 0.80D)).registerEffect();
		BT_Effect ebg3 = new BT_Effect("BT.Effect.Rapid3", "熟练的", EnumRarityColor.RARE, new DummyData(KNOCK_BACK, 0.80D), new DummyData("durability", 0.10D)).registerEffect();
		BT_Effect ebh = new BT_Effect("BT.Effect.Godly", "神之", EnumRarityColor.UNIQUE, new DummyData("damage", 0.15D), new DummyData("crit", 0.03D), new DummyData("durability", 0.15D)).registerEffect();
		BT_Effect ebj = new BT_Effect("BT.Effect.Superior", "超强的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.1D), new DummyData("crit", 0.03D), new DummyData("durability", 0.1D)).registerEffect();
		BT_Effect ebk = new BT_Effect("BT.Effect.Dangerous", "危险的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.05D), new DummyData("crit", 0.03D), new DummyData("speed", 0.15D)).registerEffect();
		BT_Effect ebl = new BT_Effect("BT.Effect.Savage", "野蛮的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.1D), new DummyData("speed", 0.3D), new DummyData("durability", 0.10D)).registerEffect();
		BT_Effect ebm = new BT_Effect("BT.Effect.Murderous", "杀手的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.07D), new DummyData("crit", 0.04D), new DummyData("speed", 0.18D)).registerEffect();
		BT_Effect ebm2 = new BT_Effect("BT.Effect.Murderous2", "刺客的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.07D), new DummyData(KNOCK_BACK, 0.8D), new DummyData("speed", 0.18D)).registerEffect();
		BT_Effect ebm3 = new BT_Effect("BT.Effect.Murderous3", "暗杀的", EnumRarityColor.UNIQUE, new DummyData("damage", 0.07D), new DummyData("crit", 0.04D), new DummyData(KNOCK_BACK, 0.8D)).registerEffect();
		BT_Effect ebn = new BT_Effect("BT.Effect.Legendary", "传说的", EnumRarityColor.LEGENDARY, new DummyData("damage", 0.15D), new DummyData("speed", 0.4D), new DummyData("crit", 0.05D), new DummyData("durability", 0.15D)).registerEffect();
		BT_Effect ebn2 = new BT_Effect("BT.Effect.Legendary2", "史诗的", EnumRarityColor.LEGENDARY, new DummyData("damage", 0.15D), new DummyData("speed", 0.4D), new DummyData("crit", 0.05D), new DummyData("durability", 0.15D), new DummyData(KNOCK_BACK, 0.8D)).registerEffect();
		BT_Effect ebn3 = new BT_Effect("BT.Effect.Legendary3", "传奇的", EnumRarityColor.LEGENDARY, new DummyData("damage", 0.15D), new DummyData("speed", 0.4D), new DummyData("crit", 0.05D), new DummyData("durability", 0.15D), new DummyData(KNOCK_BACK, -0.9D)).registerEffect();
	}
}
