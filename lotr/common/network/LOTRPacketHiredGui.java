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
 *  net.minecraft.world.World
 */
package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRUnitTradeEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LOTRPacketHiredGui
implements IMessage {
    private int entityID;
    private boolean openGui;
    public boolean isActive;
    public boolean canMove;
    public boolean teleportAutomatically;
    public int mobKills;
    public int xp;
    public float alignmentRequired;
    public LOTRUnitTradeEntry.PledgeType pledgeType;
    public boolean inCombat;
    public boolean guardMode;
    public int guardRange;

    public LOTRPacketHiredGui() {
    }

    public LOTRPacketHiredGui(int i, boolean flag) {
        this.entityID = i;
        this.openGui = flag;
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.entityID);
        data.writeBoolean(this.openGui);
        data.writeBoolean(this.isActive);
        data.writeBoolean(this.canMove);
        data.writeBoolean(this.teleportAutomatically);
        data.writeInt(this.mobKills);
        data.writeInt(this.xp);
        data.writeFloat(this.alignmentRequired);
        data.writeByte(this.pledgeType.typeID);
        data.writeBoolean(this.inCombat);
        data.writeBoolean(this.guardMode);
        data.writeInt(this.guardRange);
    }

    public void fromBytes(ByteBuf data) {
        this.entityID = data.readInt();
        this.openGui = data.readBoolean();
        this.isActive = data.readBoolean();
        this.canMove = data.readBoolean();
        this.teleportAutomatically = data.readBoolean();
        this.mobKills = data.readInt();
        this.xp = data.readInt();
        this.alignmentRequired = data.readFloat();
        this.pledgeType = LOTRUnitTradeEntry.PledgeType.forID(data.readByte());
        this.inCombat = data.readBoolean();
        this.guardMode = data.readBoolean();
        this.guardRange = data.readInt();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketHiredGui, IMessage> {
        public IMessage onMessage(LOTRPacketHiredGui packet, MessageContext context) {
            EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
            World world = LOTRMod.proxy.getClientWorld();
            Entity entity = world.getEntityByID(packet.entityID);
            if (entity instanceof LOTREntityNPC) {
                LOTREntityNPC npc = (LOTREntityNPC)entity;
                if (npc.hiredNPCInfo.getHiringPlayer() == entityplayer) {
                    npc.hiredNPCInfo.receiveClientPacket(packet);
                    if (packet.openGui) {
                        LOTRMod.proxy.openHiredNPCGui(npc);
                    }
                }
            }
            return null;
        }
    }

}

