package me.judge.poc;

import com.mojang.logging.LogUtils;
import me.judge.poc.items.POCItems;
import mod.azure.azurelib.AzureLib;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("poc")
public class PoC {
    private static final Logger LOGGER = LogUtils.getLogger();

    public PoC() {
        AzureLib.initialize();
        POCItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
