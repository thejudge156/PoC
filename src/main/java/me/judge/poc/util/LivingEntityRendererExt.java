package me.judge.poc.util;

import com.mojang.blaze3d.vertex.PoseStack;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

public interface LivingEntityRendererExt {
    PlayerReplacedRenderer getRenderer();
    PlayerReplaced getEntity();
    void renderShadow(Player player, PoseStack stack, MultiBufferSource bufferSource, float yaw, float partialTick, int packedLight);
}
