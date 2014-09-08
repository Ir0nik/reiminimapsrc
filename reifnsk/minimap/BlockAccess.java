package reifnsk.minimap;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

class BlockAccess implements IBlockAccess
{
    Block block;
    TileEntity blockTileEntity;
    int lightBrightnessForSkyBlocks;
    int blockMetadata;
    WorldChunkManager worldChunkManager;
    int worldHeight = 256;

    public Block getBlock(int i, int j, int k)
    {
        return this.block;
    }

    public TileEntity getTileEntity(int i, int j, int k)
    {
        return this.blockTileEntity;
    }

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l)
    {
        return this.lightBrightnessForSkyBlocks;
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int i, int j, int k)
    {
        return this.blockMetadata;
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int var1, int var2, int var3)
    {
        return this.block.getLightOpacity() == 0;
    }

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int i, int j)
    {
        return null;
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return this.worldHeight;
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    public boolean extendedLevelsInChunkCache()
    {
        return false;
    }

    /**
     * Return the Vec3Pool object for this world.
     */
    public Vec3Pool getWorldVec3Pool()
    {
        return null;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4)
    {
        return 0;
    }
}
