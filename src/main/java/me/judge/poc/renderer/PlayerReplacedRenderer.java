package me.judge.poc.renderer;

import me.judge.poc.entity.PlayerReplaced;
import me.judge.poc.model.PlayerReplacedModel;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoReplacedEntityRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlayerReplacedRenderer extends GeoReplacedEntityRenderer<AbstractClientPlayer, PlayerReplaced> {
    public PlayerReplacedRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PlayerReplacedModel(), new PlayerReplaced());
    }

    public void setCurrentEntity(AbstractClientPlayer entity) {
        currentEntity = entity;
    }

    public PlayerReplacedModel getModel() {
        return (PlayerReplacedModel) this.model;
    }
}
