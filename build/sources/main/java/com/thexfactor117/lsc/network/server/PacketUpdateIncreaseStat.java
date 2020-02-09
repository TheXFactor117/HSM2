package com.thexfactor117.lsc.network.server;

import com.thexfactor117.lsc.capabilities.implementation.LSCPlayerCapability;
import com.thexfactor117.lsc.util.PlayerUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateIncreaseStat implements IMessage
{
	private int stat;

	public PacketUpdateIncreaseStat()
	{
	}

	public PacketUpdateIncreaseStat(int stat)
	{
		this.stat = stat;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		stat = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(stat);
	}

	public static class Handler implements IMessageHandler<PacketUpdateIncreaseStat, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketUpdateIncreaseStat message, final MessageContext ctx)
		{
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getEntityWorld();
			mainThread.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					EntityPlayer player = ctx.getServerHandler().player;
					LSCPlayerCapability cap = PlayerUtil.getLSCPlayer(player);

					if (cap != null && !player.getEntityWorld().isRemote)
					{
						if (message.stat == 1)
						{
							cap.setStrengthStat(cap.getStrengthStat() + 1);
							PlayerUtil.updateStrengthStat(player);
						}
						else if (message.stat == 2)
						{
							cap.setAgilityStat(cap.getAgilityStat() + 1);
							PlayerUtil.updateAgilityStat(player);
						}
						else if (message.stat == 3)
						{
							cap.setDexterityStat(cap.getDexterityStat() + 1);
							PlayerUtil.updateDexterityStat(player);
						}
						else if (message.stat == 4)
						{
							cap.setIntelligenceStat(cap.getIntelligenceStat() + 1);
							PlayerUtil.updateIntelligenceStat(player);
						}
						else if (message.stat == 5)
						{
							cap.setWisdomStat(cap.getWisdomStat() + 1);
							PlayerUtil.updateWisdomStat(player);
						}
						else if (message.stat == 6)
						{
							cap.setFortitudeStat(cap.getFortitudeStat() + 1);
							PlayerUtil.updateFortitudeStat(player);
						}

						cap.updatePlayerPower();
						cap.setSkillPoints(cap.getSkillPoints() - 1);
					}
				}
			});

			return null;
		}
	}
}
