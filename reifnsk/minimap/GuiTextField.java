package reifnsk.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiTextField extends GuiButton
{
    private static GuiTextField active;
    private int inputType;
    private GuiTextField prev;
    private GuiTextField next;
    private int norm = 0;

    public GuiTextField(String s)
    {
        super(0, 0, 0, 0, 0, s);
    }

    public GuiTextField()
    {
        super(0, 0, 0, 0, 0, "");
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mx, int my)
    {
        int color = active == this ? -2134851392 : -2141167520;
        drawRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, color);

        if (this.inputType == 0)
        {
            this.drawCenteredString(mc.fontRenderer, this.displayString, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i + 1, -1);
        }
        else
        {
            int w = mc.fontRenderer.getStringWidth(this.displayString);
            this.drawString(mc.fontRenderer, this.displayString, this.field_146128_h + this.field_146120_f - w - 1, this.field_146129_i + 1, -1);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mx, int my)
    {
        if (mx >= this.field_146128_h && mx < this.field_146128_h + this.field_146120_f && my >= this.field_146129_i && my < this.field_146129_i + this.field_146121_g)
        {
            this.active();
        }

        return false;
    }

    public void active()
    {
        if (active != null)
        {
            active.norm();
        }

        active = this;
    }

    static void keyType(Minecraft mc, char c, int i)
    {
        if (active != null)
        {
            active.kt(mc, c, i);
        }
    }

    private void kt(Minecraft mc, char c, int i)
    {
        String newString;
        int e;

        if (this.inputType == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && i == 47)
        {
            newString = GuiScreen.getClipboardString();

            if (newString == null)
            {
                return;
            }

            e = 0;

            for (int k = newString.length(); e < k; ++e)
            {
                char ch = newString.charAt(e);

                if (ch != 13 && ch != 10)
                {
                    if (ch == 58)
                    {
                        ch = 59;
                    }

                    String newString1 = this.displayString + ch;

                    if (mc.fontRenderer.getStringWidth(newString1) >= this.field_146120_f - 2)
                    {
                        break;
                    }

                    this.displayString = newString1;
                }
            }
        }

        if (i != 14 && i != 211)
        {
            if (i == 15)
            {
                if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54))
                {
                    next();
                }
                else
                {
                    prev();
                }
            }

            if (i == 28)
            {
                next();
            }

            if (this.checkInput(c))
            {
                newString = this.displayString + c;

                if (mc.fontRenderer.getStringWidth(newString) < this.field_146120_f - 2)
                {
                    try
                    {
                        if (this.inputType == 1)
                        {
                            e = Integer.parseInt(newString);
                            newString = e < -32000000 ? "-32000000" : (e >= 32000000 ? "31999999" : Integer.toString(e));
                        }

                        if (this.inputType == 2)
                        {
                            e = Integer.parseInt(newString);
                            newString = e < 0 ? "0" : (e > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(e));
                        }
                    }
                    catch (NumberFormatException var9)
                    {
                        ;
                    }

                    this.displayString = newString;
                }
            }
        }
        else
        {
            if (!this.displayString.isEmpty())
            {
                this.displayString = this.displayString.substring(0, this.displayString.length() - 1);
            }
        }
    }

    boolean checkInput(char c)
    {
        switch (this.inputType)
        {
            case 0:
                return " !\"#$%&\'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(c) != -1;

            case 1:
                return (this.displayString.isEmpty() ? "-0123456789" : "0123456789").indexOf(c) != -1;

            case 2:
                return "0123456789".indexOf(c) != -1;

            default:
                return false;
        }
    }

    void norm()
    {
        String newString = this.displayString;

        try
        {
            int e;

            if (this.inputType == 1)
            {
                e = Integer.parseInt(newString);
                newString = e < -32000000 ? "-32000000" : (e >= 32000000 ? "31999999" : Integer.toString(e));
            }

            if (this.inputType == 2)
            {
                e = Integer.parseInt(newString);
                newString = e < 0 ? "0" : (e > ReiMinimap.instance.getWorldHeight() + 2 ? Integer.toString(ReiMinimap.instance.getWorldHeight() + 2) : Integer.toString(e));
            }
        }
        catch (NumberFormatException var3)
        {
            newString = Integer.toString(this.norm);
        }

        this.displayString = newString;
    }

    void setInputType(int i)
    {
        this.inputType = i;
    }

    void setPosition(int x, int y)
    {
        this.field_146128_h = x;
        this.field_146129_i = y;
    }

    void setSize(int w, int h)
    {
        this.field_146120_f = w;
        this.field_146121_g = h;
    }

    void setBounds(int x, int y, int w, int h)
    {
        this.field_146128_h = x;
        this.field_146129_i = y;
        this.field_146120_f = w;
        this.field_146121_g = h;
    }

    void setNext(GuiTextField next)
    {
        this.next = next;
    }

    void setPrev(GuiTextField prev)
    {
        this.prev = prev;
    }

    static void next()
    {
        if (active != null)
        {
            active.norm();
            active = active.next;
        }
    }

    static void prev()
    {
        if (active != null)
        {
            active.norm();
            active = active.prev;
        }
    }

    static GuiTextField getActive()
    {
        return active;
    }

    void setNorm(int norm)
    {
        this.norm = norm;
    }
}
