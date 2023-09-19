package me.judge.poc;

import me.judge.poc.items.SpiderManArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "poc", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientListener {
    @SubscribeEvent
    public static void onKey(ScreenEvent.KeyboardKeyEvent event) {
        for(ItemStack item :  Minecraft.getInstance().player.getArmorSlots()) {
            if(item.getItem() instanceof SpiderManArmor armor) {
                armor.triggerAnim(Minecraft.getInstance().player, 0, "sprint", "roll");
            }
        }
    }
}
