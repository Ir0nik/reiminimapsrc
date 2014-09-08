package reifnsk.minimap;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiKeyConfigScreen extends GuiScreen implements GuiScreenInterface
{
    private int top;
    private int bottom;
    private int left;
    private int right;
    private GuiSimpleButton okButton;
    private GuiSimpleButton cancelButton;
    private GuiSimpleButton defaultButton;
    private GuiKeyConfigButton edit;
    private int[] currentKeyCode;

    GuiKeyConfigScreen()
    {
        KeyInput[] keys = KeyInput.values();
        this.currentKeyCode = new int[keys.length];

        for (int i = 0; i < this.currentKeyCode.length; ++i)
        {
            this.currentKeyCode[i] = keys[i].getKey();
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        int label = this.calcLabelWidth();
        int button = this.calcButtonWidth();
        this.left = (this.width - label - button - 12) / 2;
        this.right = (this.width + label + button + 12) / 2;
        this.top = (this.height - KeyInput.values().length * 10) / 2;
        this.bottom = (this.height + KeyInput.values().length * 10) / 2;
        int y = this.top;
        KeyInput[] centerX = KeyInput.values();
        int len$ = centerX.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            KeyInput ki = centerX[i$];
            GuiKeyConfigButton gkcb = new GuiKeyConfigButton(this, 0, this.left, y, label, button, ki);
            this.buttonList.add(gkcb);
            y += 10;
        }

        int var9 = this.width / 2;
        this.okButton = new GuiSimpleButton(0, var9 - 74, this.bottom + 7, 46, 14, "OK");
        this.buttonList.add(this.okButton);
        this.cancelButton = new GuiSimpleButton(0, var9 - 23, this.bottom + 7, 46, 14, "Cancel");
        this.buttonList.add(this.cancelButton);
        this.defaultButton = new GuiSimpleButton(0, var9 + 28, this.bottom + 7, 46, 14, "Default");
        this.buttonList.add(this.defaultButton);
    }

    private int calcLabelWidth()
    {
        FontRenderer fr = this.mc.fontRenderer;
        int width = -1;
        KeyInput[] arr$ = KeyInput.values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            KeyInput ki = arr$[i$];
            width = Math.max(width, fr.getStringWidth(ki.name()));
        }

        return width;
    }

    private int calcButtonWidth()
    {
        FontRenderer fr = this.mc.fontRenderer;
        int width = 30;
        KeyInput[] arr$ = KeyInput.values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            KeyInput ki = arr$[i$];
            width = Math.max(width, fr.getStringWidth(">" + ki.getKeyName() + "<"));
        }

        return width + 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        String title = "Key Config";
        int titleWidth = this.fontRendererObj.getStringWidth(title);
        int titleLeft = this.width - titleWidth >> 1;
        int titleRight = this.width + titleWidth >> 1;
        drawRect(titleLeft - 2, this.top - 22, titleRight + 2, this.top - 8, -1610612736);
        this.drawCenteredString(this.fontRendererObj, title, this.width / 2, this.top - 19, -1);
        drawRect(this.left - 2, this.top - 2, this.right + 2, this.bottom + 1, -1610612736);
        super.drawScreen(i, j, f);
    }

    GuiKeyConfigButton getEditKeyConfig()
    {
        return this.edit;
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton instanceof GuiKeyConfigButton)
        {
            this.edit = (GuiKeyConfigButton)guibutton;
        }

        if (guibutton == this.okButton)
        {
            if (KeyInput.saveKeyConfig())
            {
                ReiMinimap.instance.chatInfo("\u00a7E[ObsiFight Minimap] Keyconfig Saved.");
            }
            else
            {
                ReiMinimap.instance.chatInfo("\u00a7E[ObsiFight Minimap] Error Keyconfig Saving.");
            }

            this.mc.displayGuiScreen(new GuiOptionScreen());
        }

        KeyInput[] keys;
        int i;

        if (guibutton == this.defaultButton)
        {
            keys = KeyInput.values();
            i = keys.length;

            for (int i$ = 0; i$ < i; ++i$)
            {
                KeyInput ki = keys[i$];
                ki.setDefault();
            }

            this.buttonList.clear();
            this.initGui();
        }

        if (guibutton == this.cancelButton)
        {
            keys = KeyInput.values();

            for (i = 0; i < this.currentKeyCode.length; ++i)
            {
                keys[i].setKey(this.currentKeyCode[i]);
            }

            this.mc.displayGuiScreen(new GuiOptionScreen());
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
        if (this.edit != null)
        {
            this.edit.getKeyInput().setKey(i);
            this.edit = null;
            this.buttonList.clear();
            this.initGui();
        }
        else if (i == 1)
        {
            KeyInput[] keys = KeyInput.values();

            for (int j = 0; j < this.currentKeyCode.length; ++j)
            {
                keys[j].setKey(this.currentKeyCode[j]);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }
}
