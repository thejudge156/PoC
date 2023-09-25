package me.judge.poc.networking.c2s;

import me.judge.poc.networking.PoCNetworking;
import me.judge.poc.networking.s2c.ClientPacketStartAnimation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ServerPacketStartAnimation {
    private int entityId;
    private String animName;

    public ServerPacketStartAnimation(int entityId, String animName) {
        this.entityId = entityId;
        this.animName = animName;
    }

    public ServerPacketStartAnimation(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.animName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.animName);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(player != null) {
                for(ServerPlayer serverPlayer : (List<ServerPlayer>) player.level.players()) {
                    if(player == serverPlayer) {
                        continue;
                    }
                    PoCNetworking.INSTANCE.sendTo(new ClientPacketStartAnimation(entityId, animName), serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
    }
}
