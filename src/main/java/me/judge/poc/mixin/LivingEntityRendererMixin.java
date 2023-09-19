package me.judge.poc.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.tihyo.legends.armors.LegendsArmorItem;
import com.tihyo.legends.utils.ModCompatibilityHelper;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.model.PlayerReplacedModel;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(LivingEntityRenderer.class)
public abstract class PlayerRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    @Shadow protected M model;

    public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_);
    }

    @Unique
    PlayerReplacedRenderer renderer;
    @Unique
    PlayerReplaced entity;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void setRenderer(EntityRendererProvider.Context p_174289_, EntityModel p_174290_, float p_174291_, CallbackInfo ci) {
        renderer = new PlayerReplacedRenderer(p_174289_);
        entity = new PlayerReplaced();
    }

    @Unique
    private void copyRotAndPosFromVanilla(GeoBone bone, ModelPart part, float offsetX, float offsetY, float offsetZ) {
        bone.setRotX(-part.xRot);
        bone.setRotY(-part.yRot);
        bone.setRotZ(part.zRot);

        bone.setPosX(part.x);
        bone.setPosY(part.y);
        bone.setPosZ(part.z);
    }

    @Unique
    private void copyRotAndPosFromGeoBone(ModelPart part, GeoBone bone) {
        part.xRot = -bone.getRotX();
        part.yRot = -bone.getRotY();
        part.zRot = bone.getRotZ();
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE))
    private void renderCustomModel(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        if(!(p_115308_ instanceof AbstractClientPlayer clientPlayer)) {
            return;
        }
        renderer.setCurrentEntity(clientPlayer);
        boolean isAnim = false;
        for(ItemStack itemStack : clientPlayer.getArmorSlots()) {
            if(itemStack.getItem() instanceof LegendsArmorItem item) {
                if(item.getSuit().hasArmorOn(clientPlayer)) {
                    for(AnimationController<GeoAnimatable> controller : entity.getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(entity)).getAnimationControllers().values()) {
                        isAnim = controller.isPlayingTriggeredAnimation();
                        if(isAnim) {
                            break;
                        }
                    }
                }
                break;
            }
        }

        PlayerModel<AbstractClientPlayer> playerModel = (PlayerModel<AbstractClientPlayer>) this.model;
        PlayerReplacedModel replacedModel = renderer.getModel();
        replacedModel.getBakedModel(renderer.getModel().getModelResource(entity));
        if(isAnim) {
            copyRotAndPosFromGeoBone(playerModel.head, replacedModel.getHeadBone());
            copyRotAndPosFromGeoBone(playerModel.body, replacedModel.getBodyBone());
            copyRotAndPosFromGeoBone(playerModel.leftArm, replacedModel.getLeftArmBone());
            copyRotAndPosFromGeoBone(playerModel.leftLeg, replacedModel.getLeftLegBone());
            copyRotAndPosFromGeoBone(playerModel.rightArm, replacedModel.getRightArmBone());
            copyRotAndPosFromGeoBone(playerModel.rightLeg, replacedModel.getRightLegBone());
        } else {
            copyRotAndPosFromVanilla(replacedModel.getHeadBone(), playerModel.head, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getBodyBone(), playerModel.body, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getLeftArmBone(), playerModel.leftArm, replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getLeftLegBone(), playerModel.leftLeg, replacedModel.getLeftLegOffsets()[0], replacedModel.getLeftLegOffsets()[1], replacedModel.getLeftLegOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightArmBone(),playerModel.rightArm, replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightLegBone(),playerModel.rightLeg, replacedModel.getRightLegOffsets()[0], replacedModel.getRightLegOffsets()[1], replacedModel.getRightLegOffsets()[2]);
        }
        p_115311_.pushPose();
        p_115311_.translate(0, -0.1, 0);
        renderer.defaultRender(p_115311_, entity, p_115312_, null, null, p_115309_, p_115310_, p_115313_);
        p_115311_.popPose();

        p_115311_.pushPose();
        GeoBone root = replacedModel.getRootBone();
        p_115311_.translate(root.getPosX(), root.getPosY(), root.getPosZ());
        p_115311_.mulPoseMatrix(root.getModelRotationMatrix());
        super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
        p_115311_.popPose();
    }
}
