package me.judge.poc.entity;

import mod.azure.azurelib.animatable.GeoReplacedEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animatable.instance.InstancedAnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class PlayerReplaced implements GeoReplacedEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    @Override
    public EntityType<?> getReplacingEntityType() {
        return EntityType.PLAYER;
    }

    @Nullable
    @Override
    public AnimatableInstanceCache animatableCacheOverride() {
        return new InstancedAnimatableInstanceCache(this);
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

    @Override
    public double getTick(Object entity) {
        if(Minecraft.getInstance().isPaused()) {
            return ((Entity)entity).tickCount;
        }
        return RenderUtils.getCurrentTick();
    }
}
