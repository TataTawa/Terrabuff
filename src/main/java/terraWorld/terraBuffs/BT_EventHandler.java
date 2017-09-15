package terraWorld.terraBuffs;

import DummyCore.Events.DummyEvent_OnClientGUIButtonPress;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BT_EventHandler {
	
	@SubscribeEvent
	public void onClientPacketRecieved(DummyEvent_OnClientGUIButtonPress event)
	{
		if(event.client_ParentClassPath.equalsIgnoreCase("terraWorld.terraBuffs.BT_GuiAnvil") && !event.presser.worldObj.isRemote)
		{
			TileEntity tile = event.presser.worldObj.getTileEntity(event.x, event.y, event.z);
			int cost = Integer.parseInt(event.additionalData[0].fieldValue);
			int goldCount=0;
			for(ItemStack itemStack : event.presser.inventory.mainInventory)
			{
				if(itemStack != null && itemStack.getItem().equals(Item.getItemById(266))) goldCount+=itemStack.stackSize;
			}
			if(tile != null && tile instanceof BT_TileAnvil && goldCount >= cost/3.0)
			{


				BT_TileAnvil anvil = (BT_TileAnvil) tile;
				ItemStack stk = anvil.getStackInSlot(0).copy();
				BT_Utils.addRandomEffects(stk);
				anvil.setInventorySlotContents(0, null);
				anvil.setInventorySlotContents(1, stk);
				anvil.markDirty();

				if(!event.presser.capabilities.isCreativeMode)
				{
					event.presser.experienceLevel -= cost;
					for(int i=0;i<cost/3.0;i++) event.presser.inventory.consumeInventoryItem(Item.getItemById(266));
				}
			}
		}
	}

}
