package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiOptionButton extends GuiButton
{
    private static int NAME_WIDTH;
    private static int VALUE_WIDTH;
    private static int WIDTH;
    private EnumOption option;
    private EnumOptionValue value;

    public GuiOptionButton(FontRenderer renderer, EnumOption eo)
    {
        super(0, 0, 0, 0, 10, "");
        this.option = eo;
        this.value = this.option.getValue(0);

        for (int i = 0; i < eo.getValueNum(); ++i)
        {
            String valueName = eo.getValue(i).text();
            int stringWidth = renderer.getStringWidth(valueName) + 4;
            VALUE_WIDTH = Math.max(VALUE_WIDTH, stringWidth);
        }

        NAME_WIDTH = Math.max(NAME_WIDTH, renderer.getStringWidth(eo.getText() + ": "));
        WIDTH = VALUE_WIDTH + 8 + NAME_WIDTH;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if (this.field_146125_m)
        {
            this.value = ReiMinimap.instance.getOption(this.option);
            FontRenderer fontrenderer = minecraft.fontRenderer;
            boolean flag = i >= this.field_146128_h && j >= this.field_146129_i && i < this.field_146128_h + getWidth() && j < this.field_146129_i + getHeight();
            int textcolor = flag ? -1 : -4144960;
            int bgcolor = flag ? 1728053247 : this.value.color;
            this.drawString(fontrenderer, this.option.getText(), this.field_146128_h, this.field_146129_i + 1, textcolor);
            int x1 = this.field_146128_h + NAME_WIDTH + 8;
            int x2 = x1 + VALUE_WIDTH;
            drawRect(x1, this.field_146129_i, x2, this.field_146129_i + getHeight() - 1, bgcolor);
            this.drawCenteredString(fontrenderer, this.value.text(), x1 + VALUE_WIDTH / 2, this.field_146129_i + 1, -1);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
        if (this.enabled && i >= this.field_146128_h && j >= this.field_146129_i && i < this.field_146128_h + getWidth() && j < this.field_146129_i + getHeight())
        {
            this.nextValue();
            return true;
        }
        else
        {
            return false;
        }
    }

    public EnumOption getOption()
    {
        return this.option;
    }

    public EnumOptionValue getValue()
    {
        return this.value;
    }

    public void setValue(EnumOptionValue value)
    {
        if (this.option.getValue(value) != -1)
        {
            this.value = value;
        }
    }

    public void nextValue()
    {
        this.value = this.option.getValue((this.option.getValue(this.value) + 1) % this.option.getValueNum());

//        if (!ReiMinimap.instance.getAllowCavemap() && this.option == EnumOption.RENDER_TYPE && this.value == EnumOptionValue.CAVE)
//        {
//            this.nextValue();
//        }
    }

    public static int getWidth()
    {
        return WIDTH;
    }

    public static int getHeight()
    {
        return 10;
    }
}
