package reifnsk.minimap;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.GL11;

public class GLTextureBufferedImage extends BufferedImage
{
    private static final ByteBuffer buffer = GLAllocation.createDirectByteBuffer(262144);
    private static final HashMap<Integer, GLTextureBufferedImage> registerImage = new HashMap();
    private static final Lock lock = new ReentrantLock();
    public byte[] data;
    private int register;
    private boolean magFiltering;
    private boolean minFiltering;
    private boolean clampTexture;

    private GLTextureBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable <? , ? > properties)
    {
        super(cm, raster, isRasterPremultiplied, properties);
        this.data = ((DataBufferByte)raster.getDataBuffer()).getData();
    }

    public static GLTextureBufferedImage create(int w, int h)
    {
        ColorSpace colorspace1 = ColorSpace.getInstance(1000);
        int[] bits = new int[] {8, 8, 8, 8};
        int[] bandOffsets = new int[] {0, 1, 2, 3};
        ComponentColorModel colorModel = new ComponentColorModel(colorspace1, bits, true, false, 3, 0);
        WritableRaster raster = Raster.createInterleavedRaster(0, w, h, w * 4, 4, bandOffsets, (Point)null);
        return new GLTextureBufferedImage(colorModel, raster, false, (Hashtable)null);
    }

    public static GLTextureBufferedImage create(BufferedImage image)
    {
        GLTextureBufferedImage img = create(image.getWidth(), image.getHeight());
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, (ImageObserver)null);
        g.dispose();
        return img;
    }

    public int register()
    {
        lock.lock();
        int var2;

        try
        {
            int clamp;

            if (this.register == 0)
            {
                this.register = GL11.glGenTextures();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
                clamp = this.clampTexture ? 10496 : 10497;
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp);
                buffer.clear();
                buffer.put(this.data);
                buffer.flip();
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.getWidth(), this.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
                registerImage.put(Integer.valueOf(this.register), this);
                var2 = this.register;
                return var2;
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFiltering ? GL11.GL_LINEAR : GL11.GL_NEAREST);
            clamp = this.clampTexture ? 10496 : 10497;
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp);
            buffer.clear();
            buffer.put(this.data);
            buffer.flip();
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, this.getWidth(), this.getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            var2 = this.register;
        }
        finally
        {
            lock.unlock();
        }

        return var2;
    }

    public boolean bind()
    {
        lock.lock();
        boolean var1;

        try
        {
            if (this.register == 0)
            {
                var1 = false;
                return var1;
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.register);
            var1 = true;
        }
        finally
        {
            lock.unlock();
        }

        return var1;
    }

    public void unregister()
    {
        lock.lock();

        try
        {
            if (this.register != 0)
            {
                GL11.glDeleteTextures(this.register);
                this.register = 0;
                registerImage.remove(Integer.valueOf(this.register));
                return;
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public static void unregister(int id)
    {
        lock.lock();

        try
        {
            GLTextureBufferedImage image = (GLTextureBufferedImage)registerImage.get(Integer.valueOf(id));

            if (image != null)
            {
                image.unregister();
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public void setMagFilter(boolean b)
    {
        this.magFiltering = b;
    }

    public void setMinFilter(boolean b)
    {
        this.minFiltering = b;
    }

    public int getId()
    {
        return this.register;
    }

    public boolean getMagFilter()
    {
        return this.magFiltering;
    }

    public boolean getMinFilter()
    {
        return this.minFiltering;
    }

    public void setClampTexture(boolean b)
    {
        this.clampTexture = b;
    }

    public boolean isClampTexture()
    {
        return this.clampTexture;
    }

    public void setRGBA(int x, int y, byte r, byte g, byte b, byte a)
    {
        int i = (y * this.getWidth() + x) * 4;
        this.data[i++] = r;
        this.data[i++] = g;
        this.data[i++] = b;
        this.data[i] = a;
    }

    public void setRGB(int x, int y, byte r, byte g, byte b)
    {
        int i = (y * this.getWidth() + x) * 4;
        this.data[i++] = r;
        this.data[i++] = g;
        this.data[i++] = b;
        this.data[i] = -1;
    }

    public void setRGB(int x, int y, int rgb)
    {
        int i = (y * this.getWidth() + x) * 4;
        this.data[i++] = (byte)(rgb >> 16);
        this.data[i++] = (byte)(rgb >> 8);
        this.data[i++] = (byte)(rgb >> 0);
        this.data[i] = (byte)(rgb >> 24);
    }

    public static void createTexture(int[] data, int w, int h, int name, boolean blur, boolean clamp)
    {
        byte[] bs = new byte[w * h * 4];
        int i = 0;
        int j = data.length;

        for (int k = 0; i < j; ++i)
        {
            int pixel = data[i];
            bs[k++] = (byte)(pixel >> 16);
            bs[k++] = (byte)(pixel >> 8);
            bs[k++] = (byte)(pixel >> 0);
            bs[k++] = (byte)(pixel >> 24);
        }

        createTexture(bs, w, h, name, blur, clamp);
    }

    public static void createTexture(byte[] data, int w, int h, int name, boolean blur, boolean clamp)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, name);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, blur ? GL11.GL_LINEAR : GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, blur ? GL11.GL_LINEAR : GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clamp ? GL11.GL_CLAMP : GL11.GL_REPEAT);
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }
}
