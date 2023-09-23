package me.judge.poc.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tihyo.legends.client.events.RendererChangeEvent;
import com.tihyo.legends.client.events.SetupAnimationEvent;
import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.model.PlayerReplacedModel;
import me.judge.poc.renderer.PlayerReplacedRenderer;
import me.judge.poc.util.AnimationUtil;
import mod.azure.azurelib.cache.object.GeoBone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

public class AnimationLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final Map<Player, PlayerReplacedRenderer> rendererMap;
    private final Map<Player, PlayerReplaced> entityMap;
    private final EntityRendererProvider.Context context;

    public void postSetupAnimation(RendererChangeEvent event) {
        if(event.getPlayer() != Minecraft.getInstance().player) {
            return;
        }
        renderShadow(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPlayer(), event.getNetHeadYaw(), event.getPartialTicks());
    }

    public AnimationLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> p_117346_, EntityRendererProvider.Context context) {
        super(p_117346_);
        rendererMap = new HashMap<>();
        entityMap = new HashMap<>();
        this.context = context;
        MinecraftForge.EVENT_BUS.addListener(this::postSetupAnimation);
    }

    private void copyRotAndPosFromVanilla(GeoBone bone, ModelPart part, float offsetX, float offsetY, float offsetZ) {
        bone.setRotX(-part.xRot);
        bone.setRotY(-part.yRot);
        bone.setRotZ(part.zRot);

        bone.setPosX(-part.x + offsetX);
        bone.setPosY(-part.y + offsetY);
        bone.setPosZ(part.z + offsetZ);
    }

    private void copyRotAndPosFromGeoBone(ModelPart part, GeoBone bone, float offsetX, float offsetY, float offsetZ) {
        part.xRot = -bone.getRotX();
        part.yRot = -bone.getRotY();
        part.zRot = bone.getRotZ();

        part.x = bone.getPosX() + offsetX;
        part.y = -bone.getPosY() + offsetY;
        part.z = bone.getPosZ() + offsetZ;
    }

    private void checkVars(Player player) {
        if(rendererMap.get(player) == null) {
            rendererMap.put(player, new PlayerReplacedRenderer(context));
            entityMap.put(player, new PlayerReplaced());
        }
    }

    private void renderShadow(PoseStack stack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float yaw, float partialTick) {
        checkVars(player);

        PlayerReplacedRenderer renderer = rendererMap.get(player);
        PlayerReplaced entity = entityMap.get(player);
        renderer.setCurrentEntity(player);

        PlayerModel<AbstractClientPlayer> playerModel = this.getParentModel();
        PlayerReplacedModel replacedModel = renderer.getModel();
        replacedModel.getBakedModel(renderer.getModel().getModelResource(entity));
        replacedModel.getBone("root").orElse(null).setRotZ(3.14159f);
        replacedModel.getBone("root").orElse(null).setPosY(23.5f);
        replacedModel.getBone("root").orElse(null).setRotY((float) -(((player.yBodyRot) * (Math.PI/180)) + Math.PI));
        if(AnimationUtil.isAnimated(player)) {
            copyRotAndPosFromGeoBone(playerModel.hat, replacedModel.getHeadBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.head, replacedModel.getHeadBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.body, replacedModel.getBodyBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.jacket, replacedModel.getBodyBone(), 0, 0, 0);
            copyRotAndPosFromGeoBone(playerModel.leftArm, replacedModel.getLeftArmBone(), replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.leftSleeve, replacedModel.getLeftArmBone(), replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.leftLeg, replacedModel.getLeftLegBone(), replacedModel.getLeftLegOffsets()[0], replacedModel.getLeftLegOffsets()[1], replacedModel.getLeftLegOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.leftPants, replacedModel.getLeftArmBone(), replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightSleeve, replacedModel.getRightArmBone(), replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightArm, replacedModel.getRightArmBone(), replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightLeg, replacedModel.getRightLegBone(), replacedModel.getRightLegOffsets()[0], replacedModel.getRightLegOffsets()[1], replacedModel.getRightLegOffsets()[2]);
            copyRotAndPosFromGeoBone(playerModel.rightPants, replacedModel.getRightArmBone(), replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);

            renderer.defaultRender(stack, entity, bufferSource, null, null, yaw, partialTick, packedLight);
        } else {
            copyRotAndPosFromVanilla(replacedModel.getHeadBone(), playerModel.head, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getBodyBone(), playerModel.body, 0, 0, 0);
            copyRotAndPosFromVanilla(replacedModel.getLeftArmBone(), playerModel.leftArm, replacedModel.getLeftArmOffsets()[0], replacedModel.getLeftArmOffsets()[1], replacedModel.getLeftArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getLeftLegBone(), playerModel.leftLeg, replacedModel.getLeftLegOffsets()[0], replacedModel.getLeftLegOffsets()[1], replacedModel.getLeftLegOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightArmBone(),playerModel.rightArm, replacedModel.getRightArmOffsets()[0], replacedModel.getRightArmOffsets()[1], replacedModel.getRightArmOffsets()[2]);
            copyRotAndPosFromVanilla(replacedModel.getRightLegBone(),playerModel.rightLeg, replacedModel.getRightLegOffsets()[0], replacedModel.getRightLegOffsets()[1], replacedModel.getRightLegOffsets()[2]);
        }
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player, float yaw, float partialTick, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if(player != Minecraft.getInstance().player) {
            return;
        }
        renderShadow(stack, bufferSource, packedLight, player, yaw, partialTick);
    }

    public PlayerReplacedRenderer getRenderer(Player player) {
        checkVars(player);
        return rendererMap.get(player);
    }

    public PlayerReplaced getEntity(Player player) {
        checkVars(player);
        return entityMap.get(player);
    }
}
