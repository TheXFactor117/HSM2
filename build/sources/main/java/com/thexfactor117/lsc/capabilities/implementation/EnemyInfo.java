package com.thexfactor117.lsc.capabilities.implementation;

import javax.annotation.Nullable;

import com.thexfactor117.lsc.capabilities.api.IEnemyInfo;

import net.minecraft.entity.Entity;

public class EnemyInfo implements IEnemyInfo
{
	private int level;
	private int tier;
	
	@SuppressWarnings("unused")
	private final Entity entity;
	
	public EnemyInfo(@Nullable Entity entity)
	{
		this.entity = entity;
	}
	
	public int getRandomEnemyLevel(int min, int max)
	{
		int level = (int) (Math.random() * (max - min) + min);
		
		return level > 0 ? level : 1;
	}

	@Override
	public int getEnemyLevel() 
	{
		return level;
	}

	@Override
	public void setEnemyLevel(int enemyLevel) 
	{
		level = enemyLevel;
	}

	@Override
	public int getEnemyTier() 
	{
		return tier;
	}

	@Override
	public void setEnemyTier(int enemyTier) 
	{
		tier = enemyTier;
	}
}
