/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package lotr.common.network;

import java.util.Random;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.client.fx.LOTREntitySwordCommandMarker;
import lotr.common.LOTRMod;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTRPacketLocationFX
implements IMessage {
    private Type type;
    private double posX;
    private double posY;
    private double posZ;

    public LOTRPacketLocationFX() {
    }

    public LOTRPacketLocationFX(Type t, double x, double y, double z) {
        this.type = t;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void toBytes(ByteBuf data) {
        data.writeByte(this.type.ordinal());
        data.writeDouble(this.posX);
        data.writeDouble(this.posY);
        data.writeDouble(this.posZ);
    }

    public void fromBytes(ByteBuf data) {
        byte typeID = data.readByte();
        this.type = Type.values()[typeID];
        this.posX = data.readDouble();
        this.posY = data.readDouble();
        this.posZ = data.readDouble();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketLocationFX, IMessage> {
        public IMessage onMessage(LOTRPacketLocationFX packet, MessageContext context) {
            World world = LOTRMod.proxy.getClientWorld();
            double x = packet.posX;
            double y = packet.posY;
            double z = packet.posZ;
            Random rand = world.rand;
            if (packet.type == Type.SWORD_COMMAND) {
                LOTREntitySwordCommandMarker marker = new LOTREntitySwordCommandMarker(world, x, y + 6.0, z);
                world.spawnEntityInWorld((Entity)marker);
            }
            return null;
        }
    }

    public static enum Type {
        SWORD_COMMAND;

    }

}

