package com.moblight.moblight;

import net.minecraft.block.Block;

import com.moblight.moblight.block.BlockInvisibleLight;
import com.moblight.moblight.block.BlockMoblightLantern;
import com.moblight.moblight.tile.TileEntityMoblightLantern;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Moblight.MODID, name = Moblight.NAME, version = Moblight.VERSION)
public class Moblight {

    public static final String MODID = "moblight";
    public static final String NAME = "Moblight";
    public static final String VERSION = "1.0.0";

    public static Block moblightLantern;
    public static Block invisibleLight;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moblightLantern = new BlockMoblightLantern();
        invisibleLight = new BlockInvisibleLight();

        GameRegistry.registerBlock(moblightLantern, "moblightLantern");
        GameRegistry.registerBlock(invisibleLight, "invisibleLight");
        GameRegistry.registerTileEntity(TileEntityMoblightLantern.class, "MoblightLanternTile");

        System.out.println("[Moblight] Loaded!");
    }
}
