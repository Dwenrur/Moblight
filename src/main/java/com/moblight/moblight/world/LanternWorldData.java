package com.moblight.moblight.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class LanternWorldData extends WorldSavedData {

    public static final String DATA_NAME = "moblight_lantern_data";

    private final Map<String, String> lightToOwner = new HashMap<String, String>();

    public LanternWorldData() {
        super(DATA_NAME);
    }

    public LanternWorldData(String name) {
        super(name);
    }

    public static LanternWorldData get(World world) {
        LanternWorldData data = (LanternWorldData) world.mapStorage.loadData(LanternWorldData.class, DATA_NAME);

        if (data == null) {
            data = new LanternWorldData();
            world.mapStorage.setData(DATA_NAME, data);
        }

        return data;
    }

    public static String makeKey(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    public boolean isOwned(int x, int y, int z) {
        return lightToOwner.containsKey(makeKey(x, y, z));
    }

    public String getOwner(int x, int y, int z) {
        return lightToOwner.get(makeKey(x, y, z));
    }

    public void setOwner(int x, int y, int z, String ownerKey) {
        lightToOwner.put(makeKey(x, y, z), ownerKey);
        markDirty();
    }

    public void removeOwnership(int x, int y, int z) {
        lightToOwner.remove(makeKey(x, y, z));
        markDirty();
    }

    public List<int[]> getLightsOwnedBy(String ownerKey) {
        List<int[]> result = new ArrayList<int[]>();

        for (Map.Entry<String, String> entry : lightToOwner.entrySet()) {
            if (ownerKey.equals(entry.getValue())) {
                String[] split = entry.getKey()
                    .split(",");
                result.add(
                    new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]) });
            }
        }

        return result;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        lightToOwner.clear();

        NBTTagList list = tag.getTagList("Entries", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            String lightKey = entry.getString("LightKey");
            String ownerKey = entry.getString("OwnerKey");
            lightToOwner.put(lightKey, ownerKey);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();

        for (Map.Entry<String, String> entry : lightToOwner.entrySet()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setString("LightKey", entry.getKey());
            entryTag.setString("OwnerKey", entry.getValue());
            list.appendTag(entryTag);
        }

        tag.setTag("Entries", list);
    }
}
