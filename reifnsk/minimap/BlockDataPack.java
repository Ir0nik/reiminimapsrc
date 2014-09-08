package reifnsk.minimap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class BlockDataPack
{
    private static final int renderStandardBlock = 0;
    private static final int renderCrossedSquares = 1;
    private static final int renderBlockTorch = 2;
    private static final int renderBlockFire = 3;
    private static final int renderBlockFluids = 4;
    private static final int renderBlockRedstoneWire = 5;
    private static final int renderBlockCrops = 6;
    private static final int renderBlockDoor = 7;
    private static final int renderBlockLadder = 8;
    private static final int renderBlockMinecartTrack = 9;
    private static final int renderBlockStairs = 10;
    private static final int renderBlockFence = 11;
    private static final int renderBlockLever = 12;
    private static final int renderBlockCactus = 13;
    private static final int renderBlockBed = 14;
    private static final int renderBlockRepeater = 15;
    private static final int renderPistonBase = 16;
    private static final int renderPistonExtension = 17;
    private static final int renderBlockPane = 18;
    private static final int renderBlockStem = 19;
    private static final int renderBlockVine = 20;
    private static final int renderBlockFenceGate = 21;
    private static final int renderBlockChest = 22;
    private static final int renderBlockLilyPad = 23;
    private static final int renderBlockCauldron = 24;
    private static final int renderBlockBrewingStand = 25;
    private static final int renderBlockEndPortalFrame = 26;
    private static final int renderBlockDragonEgg = 27;
    private static final int renderBlockCocoa = 28;
    private static final int renderBlockTripWireSource = 29;
    private static final int renderBlockTripWire = 30;
    private static final int renderBlockLog = 31;
    private static final int renderBlockWall = 32;
    private static final int renderBlockFlowerpot = 33;
    private static final int renderBlockBeacon = 34;
    private static final int renderBlockAnvil = 35;
    private static final int renderBlockRepeater2 = 36;
    private static final int renderBlockComparator = 37;
    private static final int renderBlockHopper = 38;
    private static final int renderBlockModLoader = -1;
    protected static final int BLOCK_NUM = 4096;
    protected static final int BLOCK_META_BITS = 4;
    protected static final int BLOCK_META = 16;
    protected static final int BLOCK_META_MASK = 15;
    protected static final int BLOCK_COLOR_NUM = 65536;
    protected static BlockData[] blockData;
    protected static float[] height = new float[65536];
    protected static BlockData[] blockColorData = new BlockData[65536];
    public static BlockColor[] defaultBlockColor = null;
    private static AtomicReference<Thread> referenceThread = new AtomicReference();

    static synchronized void calcTexture()
    {
        Thread thread = new Thread()
        {
            public void run()
            {
                BlockDataPack.defaultBlockColor = BlockDataPack.calcTextureColor();
            }
        };
        thread.setDaemon(true);
        thread.setPriority(1);
        referenceThread.set(thread);
        thread.start();
    }

    private static BlockColor[] calcTextureColor()
    {
        BlockDynamicLiquid waterMoving = (BlockDynamicLiquid)Block.getBlockFromName("flowing_water");
        BlockStaticLiquid waterStill = (BlockStaticLiquid)Block.getBlockFromName("water");
        Thread currentThread = Thread.currentThread();
        IResourceManager rm = Minecraft.getMinecraft().getResourceManager();
        BlockColor[] result = defaultBlockColor != null ? (BlockColor[])Arrays.copyOf(defaultBlockColor, 65536) : new BlockColor[65536];
        boolean skipTexture = false;
        String textureName = null;
        BufferedImage image = null;
        int[] splitImage = null;
        boolean w = false;
        boolean h = false;
        int sw = 0;
        int sh = 0;
        HashMap map = new HashMap();
        BlockData[] i = blockData;
        int len$ = i.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            BlockData bd = i[i$];

            if (referenceThread.get() != currentThread)
            {
                break;
            }

            if (!bd.textureName.equals(textureName))
            {
                textureName = bd.textureName;
                String bc = fixDomain("textures/blocks/", textureName) + ".png";

                try
                {
                    ResourceLocation tempImage = new ResourceLocation(bc);
                    IResource base1 = rm.getResource(tempImage);
                    image = ImageIO.read(base1.getInputStream());
                    skipTexture = false;
                    int var30 = image.getWidth();
                    int var31 = image.getHeight();
                    sw = var30;
                    sh = var30;
                    splitImage = calcColorArrays(image, bd.renderPass, (List)null);
                }
                catch (IOException var29)
                {
                    skipTexture = true;
                    continue;
                }
            }
            else if (skipTexture)
            {
                continue;
            }

            BlockColor var34 = null;
            int base2;
            int r;
            int base3;
            int b;
            int g;
            int meta;
            int var35;
            int var33;
            BlockType var37;

            switch (bd.renderType)
            {
                case 0:
                default:
                    var37 = bd.extend instanceof BlockType ? (BlockType)bd.extend : BlockType.NORMAL;
                    var35 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);
                    var34 = new BlockColor(var35, var37);
                    break;

                case 1:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.max(var33 >>> 24, 48) << 24;
                        var33 = var33 & 16777215 | var35;
                        var34 = new BlockColor(var33, bd.extend instanceof BlockType ? (BlockType)bd.extend : BlockType.NORMAL);
                    }

                    break;

                case 2:
                    b = calcColorInt(splitImage, sw, sh, 0.4375F, 0.4375F, 0.5625F, 0.5625F);
                    meta = calcColorInt(splitImage, sw, sh, 0.375F, 0.375F, 0.625F, 0.625F);
                    var33 = b >> 24 & 255;
                    var35 = meta >> 24 & 255;
                    base2 = var33 + var35;

                    if (base2 != 0)
                    {
                        base3 = ((b >> 16 & 255) * var33 + (meta >> 16 & 255) * var35) / base2;
                        r = ((b >> 8 & 255) * var33 + (meta >> 8 & 255) * var35) / base2;
                        g = ((b >> 0 & 255) * var33 + (meta >> 0 & 255) * var35) / base2;
                        var34 = new BlockColor(Integer.MIN_VALUE | base3 << 16 | r << 8 | g, BlockType.NORMAL);
                    }
                    else
                    {
                        b = calcColorInt(splitImage, sw, sh, 0.25F, 0.25F, 0.75F, 0.75F);
                        meta = calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 1.0F, 1.0F);
                        var33 = b >> 24 & 255;
                        var35 = meta >> 24 & 255;
                        base2 = var33 + var35;

                        if (base2 != 0)
                        {
                            base3 = ((b >> 16 & 255) * var33 + (meta >> 16 & 255) * var35) / base2;
                            r = ((b >> 8 & 255) * var33 + (meta >> 8 & 255) * var35) / base2;
                            g = ((b >> 0 & 255) * var33 + (meta >> 0 & 255) * var35) / base2;
                            var34 = new BlockColor(Integer.MIN_VALUE | base3 << 16 | r << 8 | g, BlockType.NORMAL);
                        }
                    }

                    break;

                case 3:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 4:
                    var37 = bd.extend != waterStill && bd.extend != waterMoving ? BlockType.NORMAL : BlockType.WATER;
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), var37);
                    break;

                case 5:
                    var33 = bd.extend instanceof Integer ? ((Integer)bd.extend).intValue() : 0;
                    float var36 = (float)var33 / 15.0F;
                    base2 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((base2 & -16777216) != 0)
                    {
                        base3 = Math.max(base2 >> 24 & 255, 108);
                        r = (int)((float)(base2 >> 16 & 255) * Math.max(0.3F, var36 * 0.6F + 0.4F));
                        g = (int)((float)(base2 >> 8 & 255) * Math.max(0.0F, var36 * var36 * 0.7F - 0.5F));
                        var34 = new BlockColor(base3 << 24 | r << 16 | g << 8, BlockType.NORMAL);
                    }

                    break;

                case 6:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.max(var33 >>> 24, 32) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.NORMAL);
                    }

                    break;

                case 7:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 8:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.min(var33 >>> 24, 40) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.NORMAL);
                    }

                    break;

                case 9:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 10:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 11:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.min(var33 >>> 24, 96) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.NORMAL);
                    }

                    break;

                case 12:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 13:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 14:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 15:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 16:
                    if (bd.extend instanceof Integer)
                    {
                        var33 = ((Integer)bd.extend).intValue();

                        if (var33 >= 10 && var33 <= 13)
                        {
                            var34 = new BlockColor(calcColorInt(splitImage, sw, sh, 0.0F, 0.25F, 1.0F, 1.0F), BlockType.NORMAL);
                            break;
                        }
                    }

                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 17:
                    if (bd.extend instanceof Integer)
                    {
                        var33 = ((Integer)bd.extend).intValue();

                        if ((var33 & 7) == 0 || (var33 & 7) == 1)
                        {
                            var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                            break;
                        }
                    }

                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 1.0F, 0.25F) & 16777215 | Integer.MIN_VALUE, BlockType.NORMAL);
                    break;

                case 18:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.min(var33 >>> 24, 40) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.NORMAL);
                    }

                    break;

                case 19:
                    var33 = bd.extend instanceof Integer ? ((Integer)bd.extend).intValue() : 0;
                    var35 = calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 1.0F, bd.maxY);

                    if ((var35 & -16777216) != 0)
                    {
                        base2 = Math.max(48, var35 >> 24 & 255);
                        base3 = (var35 >> 16 & 255) * var33 * 32 / 255;
                        r = (var35 >> 8 & 255) * (255 - var33 * 8) / 255;
                        g = (var35 >> 0 & 255) * var33 * 4 / 255;
                        var34 = new BlockColor(base2 << 24 | base3 << 16 | r << 8 | g << 0, BlockType.NORMAL);
                    }

                    break;

                case 20:
                    var33 = calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 1.0F, 1.0F);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.min(var33 >>> 24, 32) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.SIMPLE_FOLIAGE);
                    }

                    break;

                case 21:
                    var33 = calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 1.0F, 1.0F);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = Math.min(var33 >>> 24, 128) << 24;
                        var34 = new BlockColor(var33 & 16777215 | var35, BlockType.NORMAL);
                    }

                    break;

                case 22:
                    var34 = new BlockColor(calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ), BlockType.NORMAL);
                    break;

                case 23:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if ((var33 & -16777216) != 0)
                    {
                        var35 = var33 & -16777216;
                        base2 = (var33 >> 16 & 255) * 32 / 255;
                        base3 = (var33 >> 8 & 255) * 128 / 255;
                        r = (var33 >> 0 & 255) * 48 / 255;
                        var34 = new BlockColor(var35 | base2 << 16 | base3 << 8 | r << 0, BlockType.NORMAL);
                    }

                    break;

                case 24:
                    var33 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);
                    base2 = calcColorInt(splitImage, sw, sh, bd.minX, bd.minZ, bd.maxX, bd.maxZ);

                    if (bd.extend instanceof Integer)
                    {
                        base3 = ((Integer)bd.extend).intValue();

                        if (base3 > 0)
                        {
                            r = ((base2 >> 16 & 255) * 102 + 5508) / 255;
                            g = ((base2 >> 8 & 255) * 102 + 9027) / 255;
                            b = ((base2 >> 0 & 255) * 102 + 39015) / 255;
                            base2 = -16777216 | r << 16 | g << 8 | b << 0;
                        }
                    }

                    base3 = var33 >> 24;
                    r = 255 - base3;
                    g = ((base2 >> 16 & 255) * r + (var33 >> 16 & 255) * base3) / 255;
                    b = ((base2 >> 8 & 255) * r + (var33 >> 8 & 255) * base3) / 255;
                    meta = ((base2 >> 0 & 255) * r + (var33 >> 0 & 255) * base3) / 255;
                    var34 = new BlockColor(-16777216 | g << 16 | b << 8 | meta << 0, BlockType.NORMAL);
                    break;

                case 25:
                    var35 = calcColorInt(splitImage, sw, sh, 0.5625F, 0.3125F, 0.9375F, 0.6875F);
                    base2 = calcColorInt(splitImage, sw, sh, 0.125F, 0.0625F, 0.5F, 0.4375F);
                    base3 = calcColorInt(splitImage, sw, sh, 0.125F, 0.5625F, 0.5F, 0.9375F);
                    r = (var35 >> 16 & 255) + (base2 >> 16 & 255) + (base3 >> 16 & 255);
                    g = (var35 >> 8 & 255) + (base2 >> 8 & 255) + (base3 >> 8 & 255);
                    b = (var35 >> 0 & 255) + (base2 >> 0 & 255) + (base3 >> 0 & 255);
                    meta = bd.extend instanceof Integer ? ((Integer)bd.extend).intValue() : 0;
                    int stand1 = calcColorInt(splitImage, sw, sh, 0.5F, 0.0F, 1.0F, 1.0F);
                    int stand2 = calcColorInt(splitImage, sw, sh, 0.0F, 0.0F, 0.5F, 1.0F);

                    switch (meta)
                    {
                        case 0:
                            r += (stand1 >> 16 & 255) * 3;
                            g += (stand1 >> 8 & 255) * 3;
                            b += (stand1 >> 0 & 255) * 3;
                            break;

                        case 1:
                        case 2:
                        case 4:
                            r += (stand1 >> 16 & 255) * 2 + (stand2 >> 16 & 255);
                            g += (stand1 >> 8 & 255) * 2 + (stand2 >> 8 & 255);
                            b += (stand1 >> 0 & 255) * 2 + (stand2 >> 0 & 255);
                            break;

                        case 3:
                        case 5:
                        case 6:
                            r += (stand1 >> 16 & 255) + (stand2 >> 16 & 255) * 2;
                            g += (stand1 >> 8 & 255) + (stand2 >> 8 & 255) * 2;
                            b += (stand1 >> 0 & 255) + (stand2 >> 0 & 255) * 2;
                            break;

                        case 7:
                            r += (stand2 >> 16 & 255) * 3;
                            g += (stand2 >> 8 & 255) * 3;
                            b += (stand2 >> 0 & 255) * 3;
                    }

                    r /= 6;
                    g /= 6;
                    b /= 6;
                    var34 = new BlockColor(Integer.MIN_VALUE | r << 16 | g << 8 | b << 0, BlockType.NORMAL);
            }

            map.put(bd, var34);
        }

        for (int var32 = 0; var32 < 65536; ++var32)
        {
            result[var32] = (BlockColor)map.get(blockColorData[var32]);
        }

        referenceThread.compareAndSet(currentThread, null);
        ReiMinimap.instance.updateTexture = true;
        return result;
    }

    private static int[] calcColorArrays(BufferedImage image, int renderPass, List<Integer> list)
    {
        boolean alpha = renderPass == 1;
        int w = image.getWidth();
        int h = image.getHeight();
        int sz = w * w;
        int[] result = new int[sz];

        if (w == h)
        {
            image.getRGB(0, 0, w, h, result, 0, w);
            return result;
        }
        else
        {
            int[] rgbArray = image.getRGB(0, 0, w, h, new int[w * h], 0, w);
            int[] factor = new int[h / w];
            int num = 0;
            int rSum;
            int gSum;
            int bSum;

            if (list == null)
            {
                Arrays.fill(factor, 1);
                num = factor.length;
            }
            else
            {
                Iterator i = list.iterator();

                while (i.hasNext())
                {
                    Integer aSum = (Integer)i.next();

                    if (aSum != null)
                    {
                        rSum = aSum.intValue();
                        gSum = rSum >>> 16;
                        bSum = rSum & 65535;

                        if (gSum < factor.length)
                        {
                            factor[gSum] += bSum;
                            num += bSum;
                        }
                    }
                }
            }

            for (int var20 = 0; var20 < sz; ++var20)
            {
                int var21 = 0;
                rSum = 0;
                gSum = 0;
                bSum = 0;
                int r;
                int a;

                for (a = 0; a < factor.length; ++a)
                {
                    r = rgbArray[a * sz + var20];
                    var21 += (r >> 24 & 255) * factor[a];
                    rSum += (r >> 16 & 255) * factor[a];
                    gSum += (r >> 8 & 255) * factor[a];
                    bSum += (r >> 0 & 255) * factor[a];
                }

                a = clamp(var21 / num, 0, 255);
                r = clamp(rSum / num, 0, 255);
                int g = clamp(gSum / num, 0, 255);
                int b = clamp(bSum / num, 0, 255);

                if (!alpha)
                {
                    a = a <= 25 ? 0 : 255;
                }

                result[var20] = a << 24 | r << 16 | g << 8 | b << 0;
            }

            return result;
        }
    }

    private static int clamp(int i, int min, int max)
    {
        return i < min ? min : (i > max ? max : i);
    }

    private static int calcColorInt(int[] image, int w, int h, float minX, float minZ, float maxX, float maxZ)
    {
        if (minX != maxX && minZ != maxZ)
        {
            int startX = (int)Math.floor((double)((float)w * Math.max(0.0F, minX < maxX ? minX : maxX)));
            int startY = (int)Math.floor((double)((float)h * Math.max(0.0F, minZ < maxZ ? minZ : maxZ)));
            int endX = (int)Math.floor((double)((float)w * Math.min(1.0F, minX < maxX ? maxX : minX)));
            int endY = (int)Math.floor((double)((float)h * Math.min(1.0F, minZ < maxZ ? maxZ : minZ)));
            long a = 0L;
            long r = 0L;
            long g = 0L;
            long b = 0L;

            for (int d = startY; d < endY; ++d)
            {
                for (int x = startX; x < endX; ++x)
                {
                    int argb = image[d * w + x];
                    int _a = argb >> 24 & 255;
                    a += (long)_a;
                    r += (long)((argb >> 16 & 255) * _a);
                    g += (long)((argb >> 8 & 255) * _a);
                    b += (long)((argb >> 0 & 255) * _a);
                }
            }

            if (a == 0L)
            {
                return 16711935;
            }
            else
            {
                double var23 = 1.0D / (double)a;
                a /= (long)image.length;
                r = (long)Math.min(255, Math.max(0, (int)((double)r * var23)));
                g = (long)Math.min(255, Math.max(0, (int)((double)g * var23)));
                b = (long)Math.min(255, Math.max(0, (int)((double)b * var23)));
                return (int)(a << 24 | r << 16 | g << 8 | b);
            }
        }
        else
        {
            return 16711935;
        }
    }

    private static String getBlockTexture(Block block)
    {
        for (Class clazz = block.getClass(); clazz != null; clazz = clazz.getSuperclass())
        {
            Method[] arr$ = clazz.getMethods();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                Method m = arr$[i$];

                if (m.getReturnType() == String.class && m.getParameterTypes().length == 0 && m.getName().equals("getTextureFile"))
                {
                    try
                    {
                        return (String)m.invoke(block, new Object[0]);
                    }
                    catch (Exception var7)
                    {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    protected static final int calcPointer(int id, int meta)
    {
        assert id >= 0 && id < 4096;
        assert meta >= 0 && meta < 16;
        return id << 4 | meta;
    }

    private static boolean isPlasmaCraftFluidBlock(Block block)
    {
        assert block != null;
        String className = block.getClass().getName();
        return className.equals("Plasmacraft.BlockCausticStationary") || className.equals("Plasmacraft.BlockCausticFlowing");
    }

    private static String fixDomain(String base, String complex)
    {
        int idx = complex.indexOf(58);

        if (idx == -1)
        {
            return base + complex;
        }
        else
        {
            String name = complex.substring(idx + 1, complex.length());

            if (idx > 1)
            {
                String domain = complex.substring(0, idx);
                return domain + ':' + base + name;
            }
            else
            {
                return base + name;
            }
        }
    }

    static
    {
        HashMap blockDataMap = new HashMap();
        BlockAccess blockAccess = new BlockAccess();
        BlockAir air = (BlockAir)Block.getBlockFromName("air");
        BlockLeaves leaves = (BlockLeaves)Block.getBlockFromName("leaves");
        BlockPistonExtension pistonExtension = (BlockPistonExtension)Block.getBlockFromName("piston_head");
        BlockCrops crops = (BlockCrops)Block.getBlockFromName("wheat");
        BlockGrass grass = (BlockGrass)Block.getBlockFromName("grass");
        BlockTallGrass tallGrass = (BlockTallGrass)Block.getBlockFromName("tallgrass");
        BlockIce ice = (BlockIce)Block.getBlockFromName("ice");
        leaves.func_150122_b(true);
        boolean var38 = false;

        try
        {
            var38 = true;

            for (int e = 0; e < 4096; ++e)
            {
                Block block = Block.getBlockById(e);

                if (block != null && block != air)
                {
                    blockAccess.block = block;
                    int renderType = block.getRenderType();
                    int renderPass = block.getRenderBlockPass();

                    try
                    {
                        for (int e1 = 0; e1 < 16; ++e1)
                        {
                            Object extend = null;
                            int exmeta = e1;

                            if (block == pistonExtension)
                            {
                                exmeta = (e1 & 7) >= 6 ? 108 : e1;
                            }

                            int ptr;

                            if (block == crops && e1 >= 8)
                            {
                                ptr = calcPointer(e, e1);
                                blockColorData[ptr] = blockColorData[ptr & -1];
                            }
                            else
                            {
                                blockAccess.blockMetadata = e1;
                                ptr = calcPointer(e, e1);

                                try
                                {
                                    block.setBlockBoundsBasedOnState(blockAccess, 0, 0, 0);
                                }
                                catch (Exception var49)
                                {
                                    ;
                                }

                                height[ptr] = (float)block.getBlockBoundsMaxY();
                                boolean redstoneTorch = block instanceof BlockRedstoneTorch;
                                IIcon icon = null;

                                try
                                {
                                    icon = block.func_149735_b(redstoneTorch ? 0 : 1, exmeta);
                                }
                                catch (Exception var48)
                                {
                                    ;
                                }

                                if (block instanceof BlockRedstoneWire)
                                {
                                    icon = BlockRedstoneWire.func_150173_e("redstoneDust_cross");
                                }
                                else if (block instanceof BlockDoor)
                                {
                                    icon = block.getIcon(blockAccess, 0, 0, 0, 0);
                                }

                                if (icon != null)
                                {
                                    String textureName = icon.getIconName();

                                    if (textureName != null)
                                    {
                                        if (block == grass)
                                        {
                                            extend = BlockType.GRASS;
                                        }
                                        else if (block == leaves)
                                        {
                                            switch (e1 & 3)
                                            {
                                                case 0:
                                                case 3:
                                                default:
                                                    extend = BlockType.FOLIAGE;
                                                    break;

                                                case 1:
                                                    extend = BlockType.FOLIAGE_PINE;
                                                    break;

                                                case 2:
                                                    extend = BlockType.FOLIAGE_BIRCH;
                                            }
                                        }
                                        else if (block == tallGrass && e1 != 0)
                                        {
                                            extend = BlockType.SIMPLE_GRASS;
                                        }
                                        else if (block == ice)
                                        {
                                            extend = BlockType.ICE;
                                        }

                                        float minX = (float)block.getBlockBoundsMinX();
                                        float minY = (float)block.getBlockBoundsMinY();
                                        float minZ = (float)block.getBlockBoundsMinZ();
                                        float maxX = (float)block.getBlockBoundsMaxX();
                                        float maxY = (float)block.getBlockBoundsMaxY();
                                        float maxZ = (float)block.getBlockBoundsMaxZ();

                                        switch (renderType)
                                        {
                                            case -1:
                                                extend = block;

                                            case 0:
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 6:
                                            case 7:
                                            case 8:
                                            case 9:
                                            case 11:
                                            case 12:
                                            case 13:
                                            case 14:
                                            case 15:
                                            case 18:
                                            case 20:
                                            case 21:
                                            case 22:
                                            case 23:
                                            default:
                                                break;

                                            case 4:
                                                height[ptr] = Math.max(0.0F, 1.0F - (float)(e1 + 1) / 9.0F);
                                                extend = block;
                                                break;

                                            case 5:
                                                extend = Integer.valueOf(e1);
                                                break;

                                            case 10:
                                                height[ptr] = (e1 & 4) == 0 ? 0.75F : 1.0F;
                                                break;

                                            case 16:
                                                extend = Integer.valueOf(e1);
                                                break;

                                            case 17:
                                                extend = Integer.valueOf(e1);
                                                break;

                                            case 19:
                                                extend = Integer.valueOf(Math.min(7, e1));
                                                break;

                                            case 24:
                                                height[ptr] = (float)(2656 + 432 * Math.min(3, e1)) / 256.0F;
                                                extend = Integer.valueOf(Math.min(3, e1));
                                                break;

                                            case 25:
                                                height[ptr] = 0.2F;
                                                extend = Integer.valueOf(e1 & 7);
                                                break;

                                            case 26:
                                                boolean temp = BlockEndPortalFrame.func_150020_b(e1);

                                                if (temp)
                                                {
                                                    height[ptr] = 0.859375F;
                                                }

                                                extend = Boolean.valueOf(temp);
                                        }

                                        BlockData var54 = new BlockData(renderType, renderPass, textureName, minX, minY, minZ, maxX, maxY, maxZ, extend);
                                        BlockData bd = (BlockData)blockDataMap.get(var54);

                                        if (bd == null)
                                        {
                                            bd = var54;
                                            blockDataMap.put(var54, var54);
                                        }

                                        blockColorData[ptr] = bd;
                                    }
                                }
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException var50)
                    {
                        ;
                    }
                    finally
                    {
                        ;
                    }
                }
            }

            var38 = false;
        }
        finally
        {
            if (var38)
            {
                try
                {
                    BlockLeaves e2 = (BlockLeaves)Block.getBlockFromName("leaves");
                    e2.func_150122_b(Minecraft.getMinecraft().gameSettings.fancyGraphics);
                }
                catch (Exception var46)
                {
                    ;
                }
            }
        }

        try
        {
            BlockLeaves var53 = (BlockLeaves)Block.getBlockFromName("leaves");
            var53.func_150122_b(Minecraft.getMinecraft().gameSettings.fancyGraphics);
        }
        catch (Exception var47)
        {
            ;
        }

        blockData = (BlockData[])blockDataMap.keySet().toArray(new BlockData[blockDataMap.size()]);
        Arrays.sort(blockData);
    }
}
