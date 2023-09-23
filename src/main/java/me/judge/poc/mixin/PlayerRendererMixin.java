package me.judge.poc.mixin;

import me.judge.poc.layers.AnimationLayer;
import me.judge.poc.util.IPlayerRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements IPlayerRenderer {
    public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void addLayer(EntityRendererProvider.Context p_174557_, boolean p_174558_, CallbackInfo ci) {
        this.addLayer(new AnimationLayer(this, p_174557_));
    }

    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer p_114482_) {
        return null;
    }

    @Override
    public AnimationLayer getAnimLayer() {
        for(RenderLayer layer : this.layers) {
            if(layer instanceof AnimationLayer animLayer) {
                return animLayer;
            }
        }
        return null;
    }
}
