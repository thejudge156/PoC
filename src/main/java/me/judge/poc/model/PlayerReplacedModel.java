package me.judge.poc.model;

import me.judge.poc.entity.PlayerReplaced;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.DefaultedEntityGeoModel;
import net.minecraft.resources.ResourceLocation;

public class PlayerReplacedModel extends DefaultedEntityGeoModel<PlayerReplaced> {
    public PlayerReplacedModel() {
        super(new ResourceLocation("poc", "player"));
    }

    public GeoBone getHeadBone() {
        return this.getBone("head").orElse(null);
    }

    public GeoBone getRootBone() {
        return this.getBone("root").orElse(null);
    }

    public GeoBone getBodyBone() {
        return this.getBone("body").orElse(null);
    }

    public GeoBone getLeftArmBone() {
        return this.getBone("left_arm").orElse(null);
    }

    public GeoBone getRightArmBone() {
        return this.getBone("right_arm").orElse(null);
    }

    public GeoBone getLeftLegBone() {
        return this.getBone("left_leg").orElse(null);
    }

    public GeoBone getRightLegBone() {
        return this.getBone("right_leg").orElse(null);
    }

    public float[] getLeftArmOffsets() {
        return new float[] {5f, 1.4f, 0.4f};
    }

    public float[] getRightArmOffsets() {
        return new float[] {-5f, 1.4f, 0.4f};
    }

    public float[] getLeftLegOffsets() {
        return new float[] {2f, 11.625f, -0.1f};
    }

    public float[] getRightLegOffsets() {
        return new float[] {-2f, 11.625f, -0.1f};
    }

}
