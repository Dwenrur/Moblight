package com.moblight.moblight.tile;

import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.moblight.moblight.ConfigHandler;
import com.moblight.moblight.Moblight;
import com.moblight.moblight.world.LanternWorldData;

public class TileEntityMoblightLantern extends TileEntity {

    private int tickCounter = 0;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            tickCounter++;

            if (tickCounter >= ConfigHandler.tickInterval) {
                tickCounter = 0;
                placeLight();
                pruneOwnedLights();
            }
        }
    }

    private void placeLight() {
        LanternWorldData data = LanternWorldData.get(worldObj);
        String ownerKey = getOwnerKey();

        if (data.getLightsOwnedBy(ownerKey)
            .size() >= ConfigHandler.maxTrackedLights) {
            return;
        }

        int radius = ConfigHandler.radius;

        for (int i = 0; i < ConfigHandler.maxLightsPerCycle; i++) {
            int x = xCoord + worldObj.rand.nextInt(radius * 2 + 1) - radius;
            int y = yCoord + worldObj.rand.nextInt(7) - 3;
            int z = zCoord + worldObj.rand.nextInt(radius * 2 + 1) - radius;

            if (!isWithinRadius(x, y, z, radius)) {
                continue;
            }

            if (!worldObj.isAirBlock(x, y, z)) {
                continue;
            }

            if (data.isOwned(x, y, z)) {
                continue;
            }

            if (hasNearbyInvisibleLight(x, y, z, ConfigHandler.minLightSpacing)) {
                continue;
            }

            int light = worldObj.getBlockLightValue(x, y, z);

            if (light < ConfigHandler.lightThreshold) {
                worldObj.setBlock(x, y, z, Moblight.invisibleLight, 0, 3);
                data.setOwner(x, y, z, ownerKey);
                markDirty();
            }
        }
    }

    private boolean hasNearbyInvisibleLight(int x, int y, int z, int spacing) {
        int spacingSq = spacing * spacing;

        for (int dx = -spacing; dx <= spacing; dx++) {
            for (int dy = -spacing; dy <= spacing; dy++) {
                for (int dz = -spacing; dz <= spacing; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }

                    int distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > spacingSq) {
                        continue;
                    }

                    if (worldObj.getBlock(x + dx, y + dy, z + dz) == Moblight.invisibleLight) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isWithinRadius(int x, int y, int z, int radius) {
        int dx = x - xCoord;
        int dy = y - yCoord;
        int dz = z - zCoord;
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }

    private void pruneOwnedLights() {
        LanternWorldData data = LanternWorldData.get(worldObj);
        String ownerKey = getOwnerKey();
        List<int[]> owned = data.getLightsOwnedBy(ownerKey);

        Iterator<int[]> iterator = owned.iterator();

        while (iterator.hasNext()) {
            int[] pos = iterator.next();

            if (worldObj.getBlock(pos[0], pos[1], pos[2]) != Moblight.invisibleLight) {
                data.removeOwnership(pos[0], pos[1], pos[2]);
            }
        }
    }

    public void removeOwnedLights() {
        LanternWorldData data = LanternWorldData.get(worldObj);
        String ownerKey = getOwnerKey();
        List<int[]> owned = data.getLightsOwnedBy(ownerKey);

        for (int[] pos : owned) {
            String currentOwner = data.getOwner(pos[0], pos[1], pos[2]);

            if (ownerKey.equals(currentOwner) && worldObj.getBlock(pos[0], pos[1], pos[2]) == Moblight.invisibleLight) {
                worldObj.setBlockToAir(pos[0], pos[1], pos[2]);
            }

            data.removeOwnership(pos[0], pos[1], pos[2]);
        }

        markDirty();
    }

    private String getOwnerKey() {
        return LanternWorldData.makeKey(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("TickCounter", tickCounter);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tickCounter = tag.getInteger("TickCounter");
    }
}
