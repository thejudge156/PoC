package me.judge.poc.mixin;

import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.cache.texture.AnimatableTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnimatableTexture.class)
public class AnimatableTextureMixin extends SimpleTexture {
    public AnimatableTextureMixin(ResourceLocation p_118133_) {
        super(p_118133_);
    }

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private void dontLogPngs(Logger instance, String s, Object loc, Object ex) {
        if(!this.location.getPath().contains(".png")) {
            AzureLib.LOGGER.warn("Failed reading metadata of: {}", loc, ex);
        }
    }
}
