/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.inventory.Container
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.world.World
 */
package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.item.LOTRItemPouch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class LOTRPacketRestockPouches
implements IMessage {
    public void toBytes(ByteBuf data) {
    }

    public void fromBytes(ByteBuf data) {
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketRestockPouches, IMessage> {
        public IMessage onMessage(LOTRPacketRestockPouches packet, MessageContext context) {
            EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
            if (LOTRItemPouch.restockPouches((EntityPlayer)entityplayer)) {
                entityplayer.openContainer.detectAndSendChanges();
                entityplayer.worldObj.playSoundAtEntity((Entity)entityplayer, "mob.horse.leather", 0.5f, 1.0f);
            }
            return null;
        }
    }

}

