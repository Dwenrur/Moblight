package com.moblight.moblight.tile;

import net.minecraft.tileentity.TileEntity;

import com.moblight.moblight.ConfigHandler;
import com.moblight.moblight.Moblight;

public class TileEntityMoblightLantern extends TileEntity {

    private int tickCounter = 0;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            tickCounter++;

            if (tickCounter >= ConfigHandler.tickInterval) {
                tickCounter = 0;
                placeLight();
            }
        }
    }

    private void placeLight() {
        int radius = ConfigHandler.radius;

        for (int i = 0; i < ConfigHandler.maxLightsPerCycle; i++) {
            int x = xCoord + worldObj.rand.nextInt(radius * 2) - radius;
            int y = yCoord + worldObj.rand.nextInt(6) - 3;
            int z = zCoord + worldObj.rand.nextInt(radius * 2) - radius;

            if (worldObj.isAirBlock(x, y, z)) {
                int light = worldObj.getBlockLightValue(x, y, z);

                if (light < ConfigHandler.lightThreshold) {
                    worldObj.setBlock(x, y, z, Moblight.invisibleLight);
                }
            }
        }
    }
}
