package com.thexfactor117.lsc.util.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author TheXFactor117
 *
 */
public class NBTHelper 
{
	public static NBTTagCompound loadStackNBT(ItemStack stack)
	{
		return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
	}

	public static void saveStackNBT(ItemStack stack, NBTTagCompound nbt)
	{
		if (!stack.hasTagCompound() && !nbt.hasNoTags())
		{
			stack.setTagCompound(nbt);
		}
	}
}
