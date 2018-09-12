package org.ruoxue.backend.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    // 通过黑底mask来获取图片区域（需要保证image和mask等高等宽）
    public static void applyGrayscaleMaskToAlpha(BufferedImage image, BufferedImage mask)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] imagePixels = image.getRGB(0, 0, width, height, null, 0, width);
        int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < imagePixels.length; i++)
        {
            int color = imagePixels[i] & 0x00ffffff;
            int alpha = ~maskPixels[i] << 24;
            imagePixels[i] = color | alpha;
        }

        image.setRGB(0, 0, width, height, imagePixels, 0, width);
    }

    // 将图片变换亮度 (TODO: 变亮的情况溢出，需要处理)
    public static void changeBrightness(BufferedImage image, float offset) {
        for (int x=0; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                int alpha = (color >>> 24) & 0xFF;
                int red   = (color >>> 16) & 0xFF;
                int green = (color >>> 8) & 0xFF;
                int blue  = (color >>> 0) & 0xFF;
                red *= offset; // * (1 - red / 255);
                green *= offset; // * (1 - green / 255);
                blue *= offset; // * (1 - blue / 255);

                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, color);
                System.out.println(alpha);
            }
        }
    }

    // 将图片变换亮度，只变化mask的部分 (TODO: 变亮的情况溢出，需要处理)
    public static void changeBrightnessWithMask(BufferedImage image, BufferedImage mask, float offset) {
        for (int x=0; x<image.getWidth(); x++) {
            for (int y=0; y<image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                int alpha = (color >>> 24) & 0xFF;
                int red   = (color >>> 16) & 0xFF;
                int green = (color >>> 8) & 0xFF;
                int blue  = (color >>> 0) & 0xFF;
                int mcolor = mask.getRGB(x, y);
                if (((mcolor) & 0xFFFFFFFF) == 0)
                    continue;
                red *= offset; // * (1 - red / 255);
                green *= offset; // * (1 - green / 255);
                blue *= offset; // * (1 - blue / 255);

                color = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, color);
                System.out.println(alpha);
            }
        }
    }

    // 图片覆盖
    public static void overlayImage(BufferedImage image, BufferedImage over, int xs, int ys) {
        for (int x=xs; x<xs+over.getWidth(); x++) {
            for (int y = ys; y < ys+over.getHeight(); y++) {
                int color1 = image.getRGB(x, y);
                int color2 = over.getRGB(x-xs, y-ys);

                int alpha1 = (color1 >>> 24) & 0xFF;
                int red1   = (color1 >>> 16) & 0xFF;
                int green1 = (color1 >>> 8) & 0xFF;
                int blue1  = (color1 >>> 0) & 0xFF;
                float transparent1 = (float)alpha1 / 255;

                int alpha2 = (color2 >>> 24) & 0xFF;
                int red2   = (color2 >>> 16) & 0xFF;
                int green2 = (color2 >>> 8) & 0xFF;
                int blue2  = (color2 >>> 0) & 0xFF;
                float transparent2 = (float)alpha2 / 255;

                System.out.println(transparent2);

                float transparent = transparent1 * transparent2;
                float untransparent2 = 1-transparent2;

                int red = (int)(red1 * untransparent2 + red2 * transparent2);
                int green = (int)(green1 * untransparent2 + green2 * transparent2);
                int blue = (int)(blue1 * untransparent2 + blue2 * transparent2);

                int color = ((int)((transparent1) * 255) << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, color);
            }
        }

    }

    // 转换为Base64字符串
    public static String toBase64String(RenderedImage img, String formatName)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}