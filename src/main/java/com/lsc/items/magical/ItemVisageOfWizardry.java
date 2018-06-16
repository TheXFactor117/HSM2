package com.lsc.items.magical;

import com.lsc.capabilities.api.IChunkLevel;
import com.lsc.capabilities.api.IChunkLevelHolder;
import com.lsc.capabilities.cap.CapabilityChunkLevel;
import com.lsc.items.base.ISpecial;
import com.lsc.items.base.ItemMagical;
import com.lsc.loot.Rarity;
import com.lsc.loot.WeaponAttribute;
import com.lsc.loot.generation.ItemGeneratorHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

/**
 * 
 * @author TheXFactor117
 *
 */
public class ItemVisageOfWizardry extends ItemMagical implements ISpecial
{
	public ItemVisageOfWizardry(String name, boolean isStaff, double baseDamage, double attackSpeed, int manaPerUse, int durability) 
	{
		super(name, isStaff, baseDamage, attackSpeed, manaPerUse, durability);
	}

	@Override
	public void createSpecial(ItemStack stack, NBTTagCompound nbt, World world, ChunkPos pos) 
	{
		IChunkLevelHolder chunkLevelHolder = world.getCapability(CapabilityChunkLevel.CHUNK_LEVEL, null);
		IChunkLevel chunkLevel = chunkLevelHolder.getChunkLevel(pos);
		int level = chunkLevel.getChunkLevel();
		
		nbt.setBoolean("IsSpecial", true);
		Rarity.setRarity(nbt, Rarity.EPIC);
		nbt.setInteger("Level", level);
		ItemGeneratorHelper.setRune(nbt);
		
		// Attributes
		WeaponAttribute.INTELLIGENCE.addAttribute(nbt, 10);
		WeaponAttribute.WISDOM.addAttribute(nbt, 10);
		WeaponAttribute.MANA_STEAL.addAttribute(nbt, 0.2);
		
		// Damage and Attack Speed
		double baseDamage = this.getBaseDamage();
		double baseAttackSpeed = this.getBaseAttackSpeed();
		double weightedDamage = ItemGeneratorHelper.getWeightedDamage(nbt, Rarity.getRarity(nbt), baseDamage);
		double weightedAttackSpeed = ItemGeneratorHelper.getWeightedAttackSpeed(Rarity.getRarity(nbt), baseAttackSpeed);
		
		ItemGeneratorHelper.setMinMaxDamage(nbt, weightedDamage);
		nbt.setDouble("AttackSpeed", weightedAttackSpeed);
	}
}
