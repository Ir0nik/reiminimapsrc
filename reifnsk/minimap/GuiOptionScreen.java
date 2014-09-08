package reifnsk.minimap;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiOptionScreen extends GuiScreen implements GuiScreenInterface
{
    private static final int LIGHTING_VERSION = 16844800;
    private static final int SUNRISE_DIRECTION = 16844931;
    public static final int minimapMenu = 0;
    public static final int optionMinimap = 1;
    public static final int optionSurfaceMap = 2;
    public static final int optionEntitiesRadar = 3;
    public static final int optionMarker = 4;
    public static final int aboutMinimap = 5;
    private static final String[] TITLE_STRING = new String[] {"ObsiFight MiniMap", "Minimap Options", "SurfaceMap Options", "Entities Radar Options", "Marker Options", "About Rei\'s Minimap"};
    private int page;
    private ArrayList<GuiOptionButton> optionButtonList = new ArrayList();
    private GuiSimpleButton exitMenu;
    private GuiSimpleButton waypoint;
    private GuiSimpleButton keyconfig;
    private int top;
    private int left;
    private int right;
    private int bottom;
    private int centerX;
    private int centerY;

    public GuiOptionScreen() {}

    GuiOptionScreen(int page)
    {
        this.page = page;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        this.buttonList.clear();
        this.optionButtonList.clear();
        EnumOption[] i = EnumOption.values();
        int button = i.length;

        for (int i$ = 0; i$ < button; ++i$)
        {
            EnumOption eo = i[i$];

            if (eo.getPage() == this.page && (!this.mc.theWorld.isClient || eo != EnumOption.ENTITIES_RADAR_OPTION || ReiMinimap.instance.getAllowEntitiesRadar()) && eo != EnumOption.DIRECTION_TYPE)
            {
                GuiOptionButton button1 = new GuiOptionButton(this.mc.fontRenderer, eo);
                button1.setValue(ReiMinimap.instance.getOption(eo));
                this.buttonList.add(button1);
                this.optionButtonList.add(button1);
            }
        }

        this.left = this.width - GuiOptionButton.getWidth() >> 1;
        this.top = this.height - this.optionButtonList.size() * 10 >> 1;
        this.right = this.width + GuiOptionButton.getWidth() >> 1;
        this.bottom = this.height + this.optionButtonList.size() * 10 >> 1;

        for (int var6 = 0; var6 < this.optionButtonList.size(); ++var6)
        {
            GuiOptionButton var7 = (GuiOptionButton)this.optionButtonList.get(var6);
            var7.field_146128_h = this.left;
            var7.field_146129_i = this.top + var6 * 10;
        }

        if (this.page == 0)
        {
            this.exitMenu = new GuiSimpleButton(0, this.centerX - 95, this.bottom + 7, 60, 14, "Exit Menu");
            this.buttonList.add(this.exitMenu);
            this.waypoint = new GuiSimpleButton(1, this.centerX - 30, this.bottom + 7, 60, 14, "Waypoints");
            this.buttonList.add(this.waypoint);
            this.keyconfig = new GuiSimpleButton(2, this.centerX + 35, this.bottom + 7, 60, 14, "Keyconfig");
            this.buttonList.add(this.keyconfig);
        }
        else
        {
            this.exitMenu = new GuiSimpleButton(0, this.centerX - 30, this.bottom + 7, 60, 14, "Back");
            this.buttonList.add(this.exitMenu);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        String title = TITLE_STRING[this.page];
        int titleWidth = this.fontRendererObj.getStringWidth(title);
        int optionLeft = this.width - titleWidth >> 1;
        int optionRight = this.width + titleWidth >> 1;
        drawRect(optionLeft - 2, this.top - 22, optionRight + 2, this.top - 8, -1610612736);
        this.drawCenteredString(this.fontRendererObj, title, this.centerX, this.top - 19, -1);
        drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
        super.drawScreen(i, j, f);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton instanceof GuiOptionButton)
        {
            GuiOptionButton gob = (GuiOptionButton)guibutton;
            ReiMinimap.instance.setOption(gob.getOption(), gob.getValue());
            ReiMinimap.instance.saveOptions();
        }

        if (guibutton instanceof GuiSimpleButton)
        {
            if (guibutton == this.exitMenu)
            {
                this.mc.displayGuiScreen(this.page == 0 ? null : new GuiOptionScreen(0));
            }

            if (guibutton == this.waypoint)
            {
                this.mc.displayGuiScreen(new GuiWaypointScreen(this));
            }

            if (guibutton == this.keyconfig)
            {
                this.mc.displayGuiScreen(new GuiKeyConfigScreen());
            }
        }
    }
}
