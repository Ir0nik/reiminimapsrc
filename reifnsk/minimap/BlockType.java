package reifnsk.minimap;

import net.minecraft.world.ColorizerFoliage;

 enum BlockType
{
  NORMAL,  AIR,  GRASS,  FOLIAGE,  FOLIAGE_PINE,  FOLIAGE_BIRCH,  WATER(true),  ICE(true),  GLASS,  SIMPLE_GRASS,  SIMPLE_FOLIAGE,  SIMPLE_WATER,  END_PORTAL_FRAME,  END_PORTAL_FRAME_EYE,  EX_WATER(true);
  
  final boolean water;
  
  private BlockType()
  {
    this.water = false;
  }
  
  private BlockType(boolean water)
  {
    this.water = water;
  }
  
  int getColorMultiplayer(Data data, int i)
  {
    return 16777215;
  }
  
  static final class Data
  {
    final int[] foliageColors;
    final int[] grassColors;
    final int[] waterColors;
    final int[] smoothFoliageColors;
    final int[] smoothGrassColors;
    final int[] smoothWaterColors;
    final int foliageColorBirch = ColorizerFoliage.getFoliageColorBirch();
    final int foliageColorPine = ColorizerFoliage.getFoliageColorPine();
    
    public Data(IChunkData chunkData)
    {
      this.foliageColors = chunkData.getFoliageColors();
      this.grassColors = chunkData.getGrassColors();
      this.waterColors = chunkData.getWaterColors();
      
      this.smoothFoliageColors = chunkData.getSmoothFoliageColors();
      this.smoothGrassColors = chunkData.getSmoothGrassColors();
      this.smoothWaterColors = chunkData.getSmoothWaterColors();
    }
  }
}
