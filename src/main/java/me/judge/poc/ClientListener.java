package me.judge.poc;

import me.judge.poc.util.AnimationUtil;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = "poc", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientListener {
    @SubscribeEvent
    public static void onClientRender(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;

        AnimationUtil.renderShadow(player, event.getPoseStack(), event.getMultiBufferSource(), player.yHeadRot, event.getPartialTicks(), event.getPackedLight());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.PlayerTickEvent event) {
        if(event.side.isServer()) {
            return;
        }

        Player player = event.player;
        if(Minecraft.getInstance().player != player) {
            return;
        }

        boolean isAnim;
        for(AnimationController<GeoAnimatable> controller : AnimationUtil.getControllers(player)) {
            isAnim = controller.isPlayingTriggeredAnimation();
            if(isAnim) {
                break;
            } else {
                // Tell the server that the animation is done on the client which the anim affects
            }
        }
    }
}
