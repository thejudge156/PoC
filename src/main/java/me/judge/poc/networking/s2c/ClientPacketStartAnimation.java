package me.judge.poc.networking.s2c;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.judge.poc.PoC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketStartAnimation {
    private int entityId;
    private String animName;

    public ClientPacketStartAnimation(int entityId, String animName) {
        this.entityId = entityId;
        this.animName = animName;
    }

    public ClientPacketStartAnimation(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.animName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.animName);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().player.level.getEntity(this.entityId);
            if(entity instanceof Player player) {
                ModifierLayer<IAnimation> animationModifierLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) player).get(new ResourceLocation("poc", "layer_loc"));
                animationModifierLayer.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("poc", animName)))
                        .setFirstPersonMode(FirstPersonMode.VANILLA)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration()));
            }
        });
    }
}
