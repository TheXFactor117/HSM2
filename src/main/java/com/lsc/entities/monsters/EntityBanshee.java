package com.lsc.entities.monsters;

import java.util.Iterator;
import java.util.List;

import com.lsc.entities.EntityMonster;
import com.lsc.entities.ai.EntityAIIdleInvisible;
import com.lsc.entities.ai.EntityAINearestAttackableTargetInvisible;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EntityBanshee extends EntityMonster
{	
	private boolean canScream;
	private int screamCooldown;
	private int avgScreamCooldown = 20 * 20;
	
	public EntityBanshee(World world)
	{
		super(world);
		this.setSize(1.0F, 2.0F);
		this.canScream = false;
		this.screamCooldown = (int) (Math.random() * avgScreamCooldown) + 15;
	}
	
	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAIIdleInvisible(this));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTargetInvisible<EntityPlayer>(this, EntityPlayer.class, false));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(12);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
		return !this.isPotionActive(MobEffects.INVISIBILITY) && super.attackEntityFrom(source, amount);
    }
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (!this.world.isRemote)
		{
			if (this.getAttackTarget() != null && !this.getAttackTarget().isDead)
			{
				if (!this.canScream)
				{
					this.screamCooldown--;
					
					if (this.screamCooldown <= 0)
					{
						this.canScream = true;
						this.screamCooldown = (int) (Math.random() * avgScreamCooldown) + 15;
					}
				}
				else
				{
					int radius = 16;
					double damage = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() / 4;
					
					List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius));
					Iterator<EntityLivingBase> iterator = entityList.iterator();
					
					while (iterator.hasNext())
					{
		                Entity entity = (Entity) iterator.next();
		                
		                if (entity instanceof EntityPlayer)
		                {
		                	EntityPlayer player = (EntityPlayer) entity;
		                	
		                	if (player.attackEntityFrom(DamageSource.causeMobDamage(this), (float) damage))
		                	{
		                		this.playSound(SoundEvents.ENTITY_SPIDER_HURT, 1.0F, 1.0F);
		                		player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * 2, 3));
		                		player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20 * 5, 1));
		                	}
		                }
					}
					
					this.canScream = false;
				}
			}
		}
	}
}
