package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiSimpleButton extends GuiButton
{
    public GuiSimpleButton(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if (this.field_146125_m)
        {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            boolean flag = i >= this.field_146128_h && j >= this.field_146129_i && i < this.field_146128_h + this.field_146120_f && j < this.field_146129_i + this.field_146121_g;
            int color = flag && this.enabled ? -932813210 : -1610612736;
            drawRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color);
            this.drawCenteredString(fontrenderer, this.displayString, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, this.enabled ? -1 : -8355712);
        }
    }
}
