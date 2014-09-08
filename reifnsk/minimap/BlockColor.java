package reifnsk.minimap;

public class BlockColor
{
    private static final float _255F = 0.003921569F;
    public final int argb;
    public final float alpha;
    public final float red;
    public final float green;
    public final float blue;
    public final BlockType type;

    public BlockColor(int argb, BlockType type)
    {
        this.argb = argb;
        this.alpha = (float)(argb >> 24 & 255) * 0.003921569F;
        this.red = (float)(argb >> 16 & 255) * 0.003921569F;
        this.green = (float)(argb >> 8 & 255) * 0.003921569F;
        this.blue = (float)(argb >> 0 & 255) * 0.003921569F;
        this.type = type != null ? type : BlockType.NORMAL;
    }

    public int hashCode()
    {
        return this.type != null ? this.type.hashCode() ^ this.argb : this.argb;
    }

    public boolean equals(Object obj)
    {
        return obj instanceof BlockColor && ((BlockColor)obj).type == this.type && ((BlockColor)obj).argb == this.argb;
    }

    public String toString()
    {
        return String.format("%08X:%s", new Object[] {Integer.valueOf(this.argb), this.type});
    }
}
