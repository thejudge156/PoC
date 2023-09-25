package me.judge.poc.networking;

import me.judge.poc.networking.c2s.ServerPacketStartAnimation;
import me.judge.poc.networking.s2c.ClientPacketStartAnimation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PoCNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("poc", "main"),
            () ->  PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void initPackets() {
        int id = -1;
        INSTANCE.registerMessage(id++, ClientPacketStartAnimation.class, ClientPacketStartAnimation::toBytes, ClientPacketStartAnimation::new, ClientPacketStartAnimation::handle);
        INSTANCE.registerMessage(id++, ServerPacketStartAnimation.class, ServerPacketStartAnimation::toBytes, ServerPacketStartAnimation::new, ServerPacketStartAnimation::handle);
    }
}
