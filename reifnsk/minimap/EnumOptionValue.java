package reifnsk.minimap;

public enum EnumOptionValue
{
    ENABLE(-1610547456, "Enabled"),
    DISABLE(-1593901056, "Disabled"),
    SURFACE(-1610547456),
//    CAVE(-1593901056),
    BIOME(-1610579713),
//    SQUARE(-1610612481),
    ROUND(-1610547456),
    DYNAMIC(-1610547456),
    DAY_TIME(-1610547201),
    NIGHT_TIME(-1593868288),
    NEW_LIGHTING(-1610547456),
    OLD_LIGHTING(-1593901056),
    VERY_LOW(-1610612481),
    LOW(-1610547201),
    MIDDLE(-1610547456),
    HIGH(-1593868288),
    VERY_HIGH(-1593901056),
    SUB_OPTION(-1606401984, "->"),
    UPPER_LEFT(-1610547456),
    LOWER_LEFT(-1610547456),
    UPPER_RIGHT(-1610547456),
    LOWER_RIGHT(-1610547456),
    TYPE1(-1610547456),
    TYPE2(-1610547456),
    TYPE3(-1610547456),
    TYPE4(-1610547456),
    TYPE5(-1610547456),
    AUTO(-1610612481),
    SMALL(-1610547456, "Small"),
    NORMAL(-1610547456, "Normal"),
    LARGE(-1610547456, "Large"),
    LARGER(-1610547456, "Larger"),
    GUI_SCALE(-1610579713),
    X0_5(-1610547456, "x0.5"),
    X1_0(-1610547456, "x1.0"),
    X1_5(-1610547456, "x1.5"),
    X2_0(-1610547456, "x2.0"),
    X4_0(-1610547456, "x4.0"),
    X8_0(-1610547456, "x8.0"),
    PERCENT25(-1610547456, "25%"),
    PERCENT50(-1610547456, "50%"),
    PERCENT75(-1610547456, "75%"),
    PERCENT100(-1610547456, "100%"),
    DEPTH(-1610579840),
    STENCIL(-1610547456),
    EAST(-1610547456),
    NORTH(-1610612481),
    REI_MINIMAP(-1610547456),
    ZAN_MINIMAP(-1610612481),
    UPDATE_CHECK(-1610547456, "Check"),
    UPDATE_CHECKING(-1593868288, "Checking..."),
    UPDATE_FOUND1(-1610547201, "Found!!"),
    UPDATE_FOUND2(-1610612481, "Found!"),
    UPDATE_NOT_FOUND(-1593901056, "Not Found"),
    VERSION(-1610547456, "v3.4_03(beta)"),
    AUTHOR(-1610547456, "");
    public final int color;
    private final String text;

    private EnumOptionValue(int color)
    {
        this.color = color;
        this.text = ReiMinimap.capitalize(this.name());
    }

    private EnumOptionValue(int color, String text)
    {
        this.color = color;
        this.text = text;
    }

    public String text()
    {
        return this.text;
    }

    public static EnumOptionValue bool(boolean b)
    {
        return b ? ENABLE : DISABLE;
    }

    public static boolean bool(EnumOptionValue v)
    {
        return v == ENABLE;
    }
}
