package reifnsk.minimap;

public class Waypoint
{
    private static final int MAX_TYPE_VALUE = 1;
    public static final int NORMAL = 0;
    public static final int DEATH_POINT = 1;
    static final GLTexture[] FILE = new GLTexture[] {GLTexture.WAYPOINT1, GLTexture.WAYPOINT2};
    static final GLTexture[] MARKER = new GLTexture[] {GLTexture.MARKER1, GLTexture.MARKER2};
    public String name;
    public int x;
    public int y;
    public int z;
    public boolean enable;
    public float red;
    public float green;
    public float blue;
    public int type;

    public Waypoint(String name, int x, int y, int z, boolean flag, float r, float g, float b)
    {
        this.name = name == null ? "" : name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.enable = flag;
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    Waypoint(String name, int x, int y, int z, boolean flag, float r, float g, float b, int type)
    {
        this.name = name == null ? "" : name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.enable = flag;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.type = Math.max(0, type <= 1 ? type : 0);
    }

    Waypoint(Waypoint pt)
    {
        this.set(pt);
    }

    void set(Waypoint pt)
    {
        this.name = pt.name;
        this.x = pt.x;
        this.y = pt.y;
        this.z = pt.z;
        this.enable = pt.enable;
        this.red = pt.red;
        this.green = pt.green;
        this.blue = pt.blue;
        this.type = Math.max(0, pt.type <= 1 ? pt.type : 0);
    }

    static Waypoint load(String line)
    {
        try
        {
            String[] re = line.split(":");
            String name = re[0];
            int x = Integer.parseInt(re[1]);
            int y = Integer.parseInt(re[2]);
            int z = Integer.parseInt(re[3]);
            boolean flag = Boolean.parseBoolean(re[4]);
            int rgb = Integer.parseInt(re[5], 16);
            float r = (float)(rgb >> 16 & 255) / 255.0F;
            float g = (float)(rgb >> 8 & 255) / 255.0F;
            float b = (float)(rgb >> 0 & 255) / 255.0F;
            int type = re.length >= 7 ? Integer.parseInt(re[6]) : 0;
            return new Waypoint(name, x, y, z, flag, r, g, b, type);
        }
        catch (RuntimeException var12)
        {
            var12.printStackTrace();
            return null;
        }
    }

    public String toString()
    {
        int r = (int)(this.red * 255.0F) & 255;
        int g = (int)(this.green * 255.0F) & 255;
        int b = (int)(this.blue * 255.0F) & 255;
        int rgb = r << 16 | g << 8 | b;
        return String.format(this.type == 0 ? "%s:%d:%d:%d:%s:%06X" : "%s:%d:%d:%d:%s:%06X:%d", new Object[] {this.name, Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z), Boolean.valueOf(this.enable), Integer.valueOf(rgb), Integer.valueOf(this.type)});
    }
}
