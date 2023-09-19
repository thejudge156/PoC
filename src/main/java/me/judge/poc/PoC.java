package me.judge.poc;

import com.mojang.logging.LogUtils;
import mod.azure.azurelib.AzureLib;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("poc")
public class PoC {
    private static final Logger LOGGER = LogUtils.getLogger();

    public PoC() {
        AzureLib.initialize();
    }
}
