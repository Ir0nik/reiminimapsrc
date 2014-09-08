package reifnsk.minimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class WaypointEntityRender extends Render
{
    static final ReiMinimap rm = ReiMinimap.instance;
    final Minecraft mc;
    double far = 1.0D;
    double _d = 1.0D;

    public WaypointEntityRender(Minecraft mc)
    {
        this.mc = mc;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        this.far = (double)this.mc.gameSettings.renderDistanceChunks * 12.8D;
        this._d = 1.6D / this.far;
        double dscale = rm.getVisibleDimensionScale();
        ArrayList list = new ArrayList();

        if (rm.getMarker())
        {
            Iterator func = rm.getWaypoints().iterator();

            while (func.hasNext())
            {
                Waypoint ref = (Waypoint)func.next();

                if (ref.enable)
                {
                    list.add(new WaypointEntityRender.ViewWaypoint(ref, dscale));
                }
            }

            if (!list.isEmpty())
            {
                Collections.sort(list);
                this.mc.entityRenderer.disableLightmap(0.0D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_FOG);
                int func1 = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
                float ref1 = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
                Iterator i$ = list.iterator();

                while (i$.hasNext())
                {
                    WaypointEntityRender.ViewWaypoint w = (WaypointEntityRender.ViewWaypoint)i$.next();
                    this.draw(w, f, f1);
                }

                GL11.glAlphaFunc(func1, ref1);
                GL11.glEnable(GL11.GL_FOG);
                GL11.glEnable(GL11.GL_LIGHTING);
                this.mc.entityRenderer.enableLightmap(0.0D);
                this.shadowSize = 0.0F;
            }
        }
    }

    void draw(WaypointEntityRender.ViewWaypoint w, float f, float f1)
    {
        float alpha = (float)Math.max(0.0D, 1.0D - w.distance * this._d);
        FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
        GL11.glPushMatrix();
        StringBuilder sb = new StringBuilder();

        if (rm.getMarkerLabel() && w.name != null)
        {
            sb.append(w.name);
        }

        if (rm.getMarkerDistance())
        {
            if (sb.length() != 0)
            {
                sb.append(" ");
            }

            sb.append(String.format("[%1.2fm]", new Object[] {Double.valueOf(w.distance)}));
        }

        String str = sb.toString();
        double scale = (w.dl * 0.1D + 1.0D) * 0.02666666666666667D;
        int slideY = rm.getMarkerIcon() ? -16 : 0;
        GL11.glTranslated(w.dx, w.dy, w.dz);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.mc.gameSettings.thirdPersonView == 2 ? -this.renderManager.playerViewX : this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScaled(-scale, -scale, scale);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;

        if (rm.getMarkerIcon())
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            Waypoint.FILE[w.type].bind();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(w.red, w.green, w.blue, 0.4F);
            tessellator.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
            tessellator.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(w.red, w.green, w.blue, alpha);
            tessellator.addVertexWithUV(-8.0D, -8.0D, 0.0D, 0.0D, 0.0D);
            tessellator.addVertexWithUV(-8.0D, 8.0D, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(8.0D, 8.0D, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(8.0D, -8.0D, 0.0D, 1.0D, 0.0D);
            tessellator.draw();
        }

        int j = fontrenderer.getStringWidth(str) >> 1;

        if (j != 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.6275F);
            tessellator.addVertex((double)(-j - 1), (double)(slideY - 1), 0.0D);
            tessellator.addVertex((double)(-j - 1), (double)(slideY + 8), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(slideY + 8), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(slideY - 1), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            fontrenderer.drawString(str, -j, slideY, w.type == 0 ? 1627389951 : 1627324416);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            int a = (int)(255.0F * alpha);

            if (a > 8)
            {
                fontrenderer.drawString(str, -j, slideY, (w.type == 0 ? 16777215 : 16711680) | a << 24);
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity var1)
    {
        return null;
    }

    private class ViewWaypoint extends Waypoint implements Comparable<WaypointEntityRender.ViewWaypoint>
    {
        double dx;
        double dy;
        double dz;
        double dl;
        double distance;

        ViewWaypoint(Waypoint w, double dscale)
        {
            super(w);
            double var10001 = (double)w.x * dscale;
            this.dx = var10001 - RenderManager.renderPosX + 0.5D;
            var10001 = (double)w.y;
            this.dy = var10001 - RenderManager.renderPosY + 0.5D;
            var10001 = (double)w.z * dscale;
            this.dz = var10001 - RenderManager.renderPosZ + 0.5D;
            this.dl = this.distance = Math.sqrt(this.dx * this.dx + this.dy * this.dy + this.dz * this.dz);

            if (this.dl > WaypointEntityRender.this.far)
            {
                double d = WaypointEntityRender.this.far / this.dl;
                this.dx *= d;
                this.dy *= d;
                this.dz *= d;
                this.dl = WaypointEntityRender.this.far;
            }
        }

        public int compareTo(WaypointEntityRender.ViewWaypoint o)
        {
            return o.distance < this.distance ? -1 : (o.distance > this.distance ? 1 : 0);
        }
    }
}
