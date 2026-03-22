package com.moblight.moblight.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockInvisibleLight extends Block {

    public BlockInvisibleLight() {
        super(Material.air);
        this.setBlockName("invisibleLight");
        this.setLightLevel(1.0F);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setBlockBounds(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z);
    }

}
