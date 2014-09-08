package reifnsk.minimap;

final class BlockData implements Comparable<BlockData>
{
    static final Object ETC_EMPTY = "EMPTY";
    final int renderType;
    final int renderPass;
    final String textureName;
    final float minX;
    final float minY;
    final float minZ;
    final float maxX;
    final float maxY;
    final float maxZ;
    final int hashCode;
    final Object extend;

    BlockData(int renderType, int renderPass, String textureName, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Object ex)
    {
        if (textureName == null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if (ex == null)
            {
                ex = ETC_EMPTY;
            }

            this.renderType = renderType;
            this.renderPass = renderPass;
            this.textureName = textureName;
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.extend = ex;
            int hash = renderType ^ textureName.hashCode();
            hash ^= Float.floatToRawIntBits(minX + 1.0F);
            hash ^= Float.floatToRawIntBits(minY + 2.0F);
            hash ^= Float.floatToRawIntBits(minZ + 3.0F);
            hash ^= Float.floatToRawIntBits(maxX + 4.0F);
            hash ^= Float.floatToRawIntBits(maxZ + 5.0F);
            hash ^= Float.floatToRawIntBits(maxY + 6.0F);
            hash ^= ex.hashCode();
            this.hashCode = hash;
        }
    }

    public int hashCode()
    {
        return this.hashCode;
    }

    public boolean equals(Object obj)
    {
        return obj != null && this == obj || obj instanceof BlockData && this.equals((BlockData)obj);
    }

    private boolean equals(BlockData obj)
    {
        return this.renderType == obj.renderType && this.minX == obj.minX && this.minY == obj.minY && this.minZ == obj.minZ && this.maxX == obj.maxX && this.maxY == obj.maxY && this.maxZ == obj.maxZ && equals(this.textureName, obj.textureName) && this.extend.equals(obj.extend);
    }

    private static boolean equals(String s1, String s2)
    {
        return s1 == s2 || s1 != null && s1.equals(s2);
    }

    public int compareTo(BlockData o)
    {
        int i = this.textureName.compareTo(o.textureName);
        return i != 0 ? i : (this.renderType != o.renderType ? this.renderType - o.renderType : (this.renderPass != o.renderPass ? this.renderPass - o.renderPass : this.extend.toString().compareTo(o.extend.toString())));
    }

    public String toString()
    {
        return String.format("%s:%d (%1.6f,%1.6f,%1.6f)-(%1.6f,%1.6f,%1.6f) : %s", new Object[] {this.textureName, Integer.valueOf(this.renderType), Float.valueOf(this.minX), Float.valueOf(this.minY), Float.valueOf(this.minZ), Float.valueOf(this.maxX), Float.valueOf(this.maxY), Float.valueOf(this.maxZ), this.extend});
    }

//    public int compareTo(Object x0)
//    {
//        return this.compareTo((BlockData)x0);
//    }
}
