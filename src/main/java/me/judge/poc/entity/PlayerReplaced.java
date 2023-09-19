package me.judge.poc.entity;

import mod.azure.azurelib.animatable.GeoReplacedEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.world.entity.EntityType;

public class PlayerReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    @Override
    public EntityType<?> getReplacingEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, event -> PlayState.CONTINUE)
                .triggerableAnim("flip", RawAnimation.begin().thenPlay("animation.player.flip"))
                .triggerableAnim("jerkoff", RawAnimation.begin().thenPlay("animation.player.jerkoff")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
