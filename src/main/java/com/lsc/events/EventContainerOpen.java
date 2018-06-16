package com.lsc.events;

import com.lsc.capabilities.cap.CapabilityPlayerInformation;
import com.lsc.capabilities.implementation.PlayerInformation;
import com.lsc.items.base.ItemBauble;
import com.lsc.items.base.ItemMagical;
import com.lsc.loot.NameGenerator;
import com.lsc.loot.Rarity;
import com.lsc.loot.generation.ItemGenerator;
import com.lsc.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EventContainerOpen 
{
	@SubscribeEvent
	public void onContainerOpen(PlayerContainerEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();

		if (player != null && !world.isRemote);
		{
			for (ItemStack stack : event.getContainer().getInventory())
			{
				if (stack != null && (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemBow 
						|| stack.getItem() instanceof ItemMagical || stack.getItem() instanceof ItemBauble))
				{
					NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);
					stack.setTagCompound(nbt);

					if (nbt != null)
					{
						if (nbt.getInteger("Level") == 0)
						{
							if (nbt.hasKey("TagLevel"))
							{
								generate(stack, nbt, player.world, nbt.getInteger("TagLevel"));
							}
							else
							{
								PlayerInformation playerInfo = (PlayerInformation) player.getCapability(CapabilityPlayerInformation.PLAYER_INFORMATION, null);
								nbt.setInteger("TagLevel", playerInfo.getPlayerLevel());
								
								generate(stack, nbt, player.world, nbt.getInteger("TagLevel"));
							}
						}
					}
				}
			}
		}
	}
	
	public void generate(ItemStack stack, NBTTagCompound nbt, World world, int level)
	{
		if (Rarity.getRarity(nbt) == Rarity.DEFAULT)
		{
			Rarity.setRarity(nbt, Rarity.getRandomRarity(nbt, world.rand));
		}
		
		ItemGenerator.create(stack, nbt, world, level);

		stack.setTagCompound(nbt);
		NameGenerator.generateName(stack, nbt);
	}
}
