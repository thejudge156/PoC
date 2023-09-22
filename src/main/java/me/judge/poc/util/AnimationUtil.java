package me.judge.poc.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationUtil {
    public static void triggerAnimation(Player player, String animationName) {
        if(player.level.isClientSide) {
            ((LivingEntityRendererExt) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getEntity().triggerAnim(player, "controller", animationName);
        }
        // Add logic in order to tell the server and the client what animation is playing, (For timing reasons)
    }

    public static void renderShadow(Player player, PoseStack stack, MultiBufferSource bufferSource, float yaw, float partialTick, int packedLight) {
        ((LivingEntityRendererExt) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).renderShadow(player, stack, bufferSource, yaw, partialTick, packedLight);
    }

    public static boolean isAnimated(Player player) {
        // Add logic to grab the current animation, (For timing reasons)
        return false;
    }

    public static Collection<AnimationController<GeoAnimatable>> getControllers(Player player) {
        PlayerReplaced playerReplaced = ((LivingEntityRendererExt) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getEntity();
        PlayerReplacedRenderer renderer = ((LivingEntityRendererExt) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getRenderer();

        renderer.setCurrentEntity((AbstractClientPlayer) player);

        return playerReplaced.getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(playerReplaced)).getAnimationControllers().values();
    }
}
