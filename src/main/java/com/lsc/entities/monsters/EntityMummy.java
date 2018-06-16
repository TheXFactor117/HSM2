package com.lsc.entities.monsters;

import com.lsc.entities.EntityMonster;
import com.lsc.entities.ai.EntityAIGaze;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EntityMummy extends EntityMonster
{
	protected EntityAIGaze aiGaze;
	
	public EntityMummy(World world)
	{
		super(world);
		this.setSize(1.0F, 2.0F);
	}
	
	@Override
	protected void initEntityAI()
	{
		this.aiGaze = new EntityAIGaze(this);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, aiGaze);
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, false));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (!this.world.isRemote)
		{
			this.updateCustomTasks();
		}
	}
	
	private void updateCustomTasks()
	{
		if (this.aiGaze.gazeCooldown > 0)
		{
			this.aiGaze.gazeCooldown--;
		}
	}
}
