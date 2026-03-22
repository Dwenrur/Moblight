package com.moblight.moblight.tile;

import net.minecraft.tileentity.TileEntity;

import com.moblight.moblight.ConfigHandler;

public class TileEntityMoblightLantern extends TileEntity {

    private int tickCounter = 0;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            tickCounter++;

            if (tickCounter >= ConfigHandler.tickInterval) {
                tickCounter = 0;
                System.out.println(
                    "Moblight Lantern Tick at " + xCoord
                        + ", "
                        + yCoord
                        + ", "
                        + zCoord
                        + " radius="
                        + ConfigHandler.radius);
            }
        }
    }
}
