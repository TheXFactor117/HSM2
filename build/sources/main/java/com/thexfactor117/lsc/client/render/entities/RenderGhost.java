package com.thexfactor117.lsc.client.render.entities;

import com.thexfactor117.lsc.client.models.entities.ModelGhost;
import com.thexfactor117.lsc.entities.monsters.EntityGhost;
import com.thexfactor117.lsc.util.misc.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author TheXFactor117
 *
 */
public class RenderGhost extends RenderLiving<EntityGhost>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":textures/models/ghost.png"); 
	
	public RenderGhost(RenderManager manager) 
	{
		super(manager, new ModelGhost(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGhost entity) 
	{
		return TEXTURE;
	}
}