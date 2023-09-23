package me.judge.poc.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.mixin.PlayerRendererMixin;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationUtil {
    public static void triggerAnimation(Player player, String animationName) {
        if(player.level.isClientSide) {
            ((IPlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getAnimLayer().getEntity(player).triggerAnim(player, "controller", animationName);
        }
        // Add logic in order to tell the server and the client what animation is playing, (For timing reasons)
    }

    public static void renderShadow(Player player, PoseStack stack, MultiBufferSource bufferSource, float yaw, float partialTick, int packedLight) {
        ((IPlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getAnimLayer().render(
                stack, bufferSource, packedLight, (AbstractClientPlayer) player, yaw, partialTick, 1, 1, 1, 1);
    }

    public static boolean isAnimated(Player player) {
        // Add logic to grab the current animation, (For timing reasons)
        boolean isAnim = false;
        for(AnimationController<GeoAnimatable> controller : AnimationUtil.getControllers(player)) {
            isAnim = controller.isPlayingTriggeredAnimation();
            if(isAnim) {
                break;
            } else {
                // Tell the server that the animation is done from the client which the anim affects
            }
        }
        return isAnim;
    }

    public static Collection<AnimationController<GeoAnimatable>> getControllers(Player player) {
        PlayerReplaced playerReplaced = ((IPlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getAnimLayer().getEntity(player);
        PlayerReplacedRenderer renderer = ((IPlayerRenderer)Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player)).getAnimLayer().getRenderer(player);

        renderer.setCurrentEntity((AbstractClientPlayer) player);

        return playerReplaced.getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(playerReplaced)).getAnimationControllers().values();
    }
}
