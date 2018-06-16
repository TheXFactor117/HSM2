package com.lsc.entities.projectiles;

import com.lsc.capabilities.cap.CapabilityPlayerStats;
import com.lsc.capabilities.implementation.Stats;
import com.lsc.player.WeaponHelper;
import com.lsc.util.NBTHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * 
 * @author TheXFactor117
 *
 */
public abstract class EntityProjectileBase extends EntityThrowable
{
	protected EntityPlayer player; // shooter
	protected ItemStack stack; // item that shot projectile
	
	public EntityProjectileBase(World world)
	{
		super(world);
	}
	
	/** Only use this constructor if a player shoots the projectile with the correct item. */
	public EntityProjectileBase(World world, double x, double y, double z, float velocity, float inaccuracy, EntityPlayer player, ItemStack stack)
	{
		super(world, x, y, z);
		this.shoot(x, y, z, velocity, inaccuracy);
		this.player = player;
		this.stack = stack;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.getEntityWorld().isRemote)
		{
			if (!this.inGround && !this.isDead)
			{
				this.getEntityWorld().spawnParticle(EnumParticleTypes.SPELL_INSTANT, this.posX, this.posY, this.posZ, 0F, 0F, 0F, new int[0]);
			}
		}
	}
	
	@Override
	public void onImpact(RayTraceResult result)
	{
		if (!this.getEntityWorld().isRemote && player != null)
		{
			Stats stats = (Stats) player.getCapability(CapabilityPlayerStats.STATS, null);
			
			if (result.entityHit != null && result.entityHit instanceof EntityLivingBase && stats != null)
			{
				NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);
				double magicalPower = stats.getMagicalPower();
				double damage = (Math.random() * (nbt.getInteger("MaxDamage") - nbt.getInteger("MinDamage"))) + (nbt.getInteger("MinDamage") + (int) magicalPower);
				
				// set the true amount of damage.
				double trueDamage = damage;
				
				if (stats.getCriticalChance() > 0)
				{
					if (Math.random() < stats.getCriticalChance())
					{
						trueDamage = (stats.getCriticalDamage() * damage) + damage;
					}
				}
				
				// apply damage
				result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) trueDamage);
				result.entityHit.hurtResistantTime = 0; // set hurt resistant time to zero because other calculations might be added.
				
				// apply attributes
				WeaponHelper.useWeaponAttributes((float) damage, player, (EntityLivingBase) result.entityHit, stack, NBTHelper.loadStackNBT(stack));
			}
		}
	}
	
	@Override
	protected float getGravityVelocity()
	{
		return 0F;
	}
	
	public EntityPlayer getShooter()
	{
		return player;
	}
}
