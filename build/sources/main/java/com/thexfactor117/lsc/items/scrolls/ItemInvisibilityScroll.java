package com.thexfactor117.lsc.items.scrolls;

import java.util.List;

import com.thexfactor117.lsc.capabilities.implementation.LSCPlayerCapability;
import com.thexfactor117.lsc.init.ModItems;
import com.thexfactor117.lsc.init.ModTabs;
import com.thexfactor117.lsc.items.base.ItemBase;
import com.thexfactor117.lsc.loot.Rarity;
import com.thexfactor117.lsc.util.PlayerUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author TheXFactor117
 *
 */
public class ItemInvisibilityScroll extends ItemBase
{
	private Rarity rarity;
	
	public ItemInvisibilityScroll(String name, Rarity rarity)
	{
		super(name, ModTabs.lscTab);
		this.rarity = rarity;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{		
		ItemStack stack = player.inventory.getCurrentItem();
		LSCPlayerCapability cap = PlayerUtil.getLSCPlayer(player);
		
		if (player.inventory.getCurrentItem().getItem() == ModItems.INVISIBILITY_SCROLL && cap != null)
		{
			if (!world.isRemote && cap.getMana() >= 10)
			{
				player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 20*10, 1, false, false));
				stack.shrink(1);
				cap.decreaseMana(10);
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
    {		
		tooltip.add(rarity.getColor() + rarity.getName());
    }
}
