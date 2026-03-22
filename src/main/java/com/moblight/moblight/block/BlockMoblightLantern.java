package com.moblight.moblight.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.moblight.moblight.tile.TileEntityMoblightLantern;

public class BlockMoblightLantern extends Block {

    public BlockMoblightLantern() {
        super(Material.iron);
        this.setBlockName("moblightLantern");
        this.setBlockTextureName("moblight:moblight_lantern");
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setLightLevel(1.0F);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityMoblightLantern();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);

            if (tileEntity instanceof TileEntityMoblightLantern) {
                ((TileEntityMoblightLantern) tileEntity).removeOwnedLights();
            }
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }
}
