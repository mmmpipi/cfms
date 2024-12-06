package com.mmmpipi.cfms;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ChildFolderModScanner.MODID)
public class ChildFolderModScanner
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cfms";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ChildFolderModScanner()
    {
    }

    public static Logger getLogger(){
        return LOGGER;
    }
}
