package me.judge.poc.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.tihyo.legends.armors.LegendsArmorItem;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.model.PlayerReplacedModel;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    @Shadow protected M model;

    public LivingEntityRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_);
    }

    @Unique
    PlayerReplacedRenderer renderer;
    @Unique
    PlayerReplaced entity;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void setRenderer(EntityRendererProvider.Context p_174289_, EntityModel p_174290_, float p_174291_, CallbackInfo ci) {
        if(!(p_174290_ instanceof PlayerModel<?>)) {
            return;
        }
        renderer = new PlayerReplacedRenderer(p_174289_);
        entity = new PlayerReplaced();
    }

    @Unique
    private void copyRotAndPosFromVanilla(GeoBone bone, ModelPart part, float offsetX, float offsetY, float offsetZ) {
        bone.setRotX(-part.xRot);
        bone.setRotY(-part.yRot);
        bone.setRotZ(part.zRot);

        bone.setPosX(-part.x + offsetX);
        bone.setPosY(-part.y + offsetY);
        bone.setPosZ(part.z + offsetZ);
    }

    @Unique
    private void copyRotAndPosFromGeoBone(ModelPart part, GeoBone bone, float offsetX, float offsetY, float offsetZ) {
        part.xRot = -bone.getRotX();
        part.yRot = -bone.getRotY();
        part.zRot = bone.getRotZ();

        part.x = bone.getPosX() + offsetX;
        part.y = -bone.getPosY() + offsetY;
        part.z = bone.getPosZ() + offsetZ;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;", shift = At.Shift.AFTER))
    private void renderShadowModel(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        if(!(p_115308_ instanceof AbstractClientPlayer clientPlayer)) {
            return;
        }
        renderer.setCurrentEntity(clientPlayer);
        boolean isAnim = false;
        for(ItemStack itemStack : clientPlayer.getArmorSlots()) {
            if(itemStack.getItem() instanceof LegendsArmorItem item) {
                if(item.getSuit().hasArmorOn(clientPlayer)) {
                    for(AnimationController<GeoAnimatable> controller : entity.getAnimatableInstanceCache().getManagerForId(renderer.getInstanceId(entity)).getAnimationControllers().values()) {
                        if(GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_U) == GLFW.GLFW_PRESS) {
                            entity.triggerAnim(clientPlayer, "controller", "flip");
                        }
                        if(GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_I) == GLFW.GLFW_PRESS) {
                            entity.triggerAnim(clientPlayer, "controller", "jerkoff"); // Don't ask.
                        }

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
        replacedModel.getBone("root").orElse(null).setRotZ(3.14159f);
        replacedModel.getBone("root").orElse(null).setPosY(23.5f);
        replacedModel.getBone("root").orElse(null).setRotY((float) -(((clientPlayer.yBodyRot) * (Math.PI/180)) + Math.PI));
        if(isAnim) {
            copyRotAndPosFromGeoBone(playerModel.head, replacedModel.getHeadBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.body, replacedModel.getBodyBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.leftArm, replacedModel.getLeftArmBone(), replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.leftLeg, replacedModel.getLeftLegBone(), replacedModel.getLeftLegOffsets()[0], replacedModel.getLeftLegOffsets()[1], replacedModel.getLeftLegOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightArm, replacedModel.getRightArmBone(), replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightLeg, replacedModel.getRightLegBone(), replacedModel.getRightLegOffsets()[0], replacedModel.getRightLegOffsets()[1], replacedModel.getRightLegOffsets()[2]);

            renderer.defaultRender(p_115311_, entity, p_115312_, null, null, p_115309_, p_115310_, p_115313_);
        } else {
            copyRotAndPosFromVanilla(replacedModel.getHeadBone(), playerModel.head, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getBodyBone(), playerModel.body, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getLeftArmBone(), playerModel.leftArm, replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getLeftLegBone(), playerModel.leftLeg, replacedModel.getLeftLegOffsets()[0], replacedModel.getLeftLegOffsets()[1], replacedModel.getLeftLegOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightArmBone(),playerModel.rightArm, replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightLegBone(),playerModel.rightLeg, replacedModel.getRightLegOffsets()[0], replacedModel.getRightLegOffsets()[1], replacedModel.getRightLegOffsets()[2]);
        }
    }
}
