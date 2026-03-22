package com.moblight.moblight;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static Configuration config;

    public static int tickInterval = 10;
    public static int radius = 10;
    public static int maxLightsPerCycle = 1;
    public static int lightThreshold = 12;
    public static int minLightSpacing = 4;
    public static int maxTrackedLights = 256;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        sync();
    }

    public static void sync() {
        try {
            config.load();

            tickInterval = config.getInt(
                "tickInterval",
                "general",
                10,
                1,
                200,
                "How many ticks between Moblight lantern update cycles.");

            radius = config
                .getInt("radius", "general", 10, 1, 64, "How many blocks outward the lantern checks for dark spots.");

            maxLightsPerCycle = config.getInt(
                "maxLightsPerCycle",
                "general",
                1,
                1,
                64,
                "Maximum number of invisible light blocks placed per update cycle.");

            lightThreshold = config.getInt(
                "lightThreshold",
                "general",
                12,
                0,
                15,
                "Only place light blocks where block light is below this value.");

            minLightSpacing = config.getInt(
                "minLightSpacing",
                "general",
                4,
                1,
                16,
                "Minimum spacing between placed invisible light sources.");

            maxTrackedLights = config.getInt(
                "maxTrackedLights",
                "general",
                256,
                1,
                4096,
                "Maximum number of invisible light blocks a single lantern may own.");

        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
