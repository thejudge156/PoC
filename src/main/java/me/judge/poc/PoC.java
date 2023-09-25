package me.judge.poc;

import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.gson.AnimationJson;
import dev.kosmx.playerAnim.core.data.gson.GeckoLibSerializer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import me.judge.poc.networking.PoCNetworking;
import me.judge.poc.networking.c2s.ServerPacketStartAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("poc")
public class PoC {
    private static PoC instance;
    private static final Logger LOGGER = LogUtils.getLogger();

    public PoC() {
        instance = this;

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new ResourceLocation("poc", "animation"), 62, (player) -> new ModifierLayer<>());
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(((player, animationStack) -> {
            ModifierLayer<IAnimation> layer = new ModifierLayer<>();
            animationStack.addAnimLayer(69, layer);
            PlayerAnimationAccess.getPlayerAssociatedData(player).set(new ResourceLocation("poc", "layer_loc"), layer);
        }));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onEntityJoin);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        PoCNetworking.initPackets();
    }

    private void onEntityJoin(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            return;
        }

        if(GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_U) == GLFW.GLFW_PRESS && Minecraft.getInstance().player == event.player) {
            ModifierLayer<IAnimation> animationModifierLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) event.player).get(new ResourceLocation("poc", "layer_loc"));

            String currentAnim = "animation.player.flip";
            animationModifierLayer.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("poc", currentAnim)))
                    .setFirstPersonMode(FirstPersonMode.VANILLA)
                    .setFirstPersonConfiguration(new FirstPersonConfiguration()));
            PoCNetworking.INSTANCE.sendTo(new ServerPacketStartAnimation(event.player.getId(), currentAnim), Minecraft.getInstance().getConnection().getConnection(), NetworkDirection.PLAY_TO_SERVER);
        }
    }
}
