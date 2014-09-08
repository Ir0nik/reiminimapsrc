package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiKeyConfigButton extends GuiButton
{
    private GuiKeyConfigScreen parrent;
    private KeyInput keyInput;
    private String labelText;
    private String buttonText;
    private int labelWidth;
    private int buttonWidth;

    public GuiKeyConfigButton(GuiKeyConfigScreen parrent, int id, int x, int y, int label, int button, KeyInput key)
    {
        super(id, x, y, label + 12 + button, 9, "");
        this.parrent = parrent;
        this.keyInput = key;
        this.labelWidth = label;
        this.buttonWidth = button;
        this.labelText = this.keyInput.label();
        this.buttonText = this.keyInput.getKeyName();
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if (this.keyInput != null)
        {
            boolean b = i >= this.field_146128_h && i < this.field_146128_h + this.field_146120_f && j >= this.field_146129_i && j < this.field_146129_i + this.field_146121_g;
            this.drawString(minecraft.fontRenderer, this.labelText, this.field_146128_h, this.field_146129_i + 1, b ? -1 : -4144960);
            String text = this.buttonText;

            if (this == this.parrent.getEditKeyConfig())
            {
                text = ">" + text + "<";
            }

            b = i >= this.field_146128_h + this.field_146120_f - this.buttonWidth && i < this.field_146128_h + this.field_146120_f && j >= this.field_146129_i && j < this.field_146129_i + this.field_146121_g;
            int color = b ? 1728053247 : (this.keyInput.getKey() == 0 ? (this.keyInput.isDefault() ? -1610612481 : -1593868288) : (this.keyInput.isDefault() ? -1610547456 : -1593901056));
            drawRect(this.field_146128_h + this.field_146120_f - this.buttonWidth, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color);
            this.drawCenteredString(minecraft.fontRenderer, text, this.field_146128_h + this.field_146120_f - this.buttonWidth / 2, this.field_146129_i + 1, -1);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
        return i >= this.field_146128_h + this.field_146120_f - this.buttonWidth && i < this.field_146128_h + this.field_146120_f && j >= this.field_146129_i && j < this.field_146129_i + this.field_146121_g;
    }

    void setBounds(int x, int y, int label, int button)
    {
        this.field_146128_h = x;
        this.field_146129_i = y;
        this.labelWidth = label;
        this.buttonWidth = button;
        this.field_146120_f = label + button + 2;
    }

    KeyInput getKeyInput()
    {
        return this.keyInput;
    }
}
