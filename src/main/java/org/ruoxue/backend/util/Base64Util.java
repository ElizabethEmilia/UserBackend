package org.ruoxue.backend.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *  Base64加密图片工具类
 */
public class Base64Util {

    public static String baseImageSavePath = null;

    /**
     *  图片转化成base64字符串
     * @param imgFile
     * @return
     */
    public static String GetImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    /**
     * data URI 转换为图片并保存
     * @param imgStr
     * @return 以保存的文件名
     */
    public static String GenerateImageFromDataURI(String imgStr)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == "") //图像数据为空
        {
            System.out.println("Empty string");
            return "";
        }
        try  {
            Pattern P = Pattern.compile("^data:(\\w+)\\/(\\w+);(\\w+),");
            Matcher M = P.matcher(imgStr);

            // Not a base64 URI
            if (!M.find())
                return "";

            // Test file type
            String fileType =  M.group(1);
            String extension = M.group(2);

            if (!fileType.equals("image")) {
                return "";
            }

            imgStr = imgStr.replaceAll("^data:(\\w+)\\/(\\w+);(\\w+),", "").replaceAll(" ","+");
            System.out.println(imgStr);
            //byte[] data = Base64.decodeBase64(imgStr);
            String imgFilePath = "" + (new Date().getTime()) + (int)(Math.random() * 1000) + "." + extension;//新生成的图片

            GenerateImage(imgStr, baseImageSavePath + imgFilePath);
            return imgFilePath;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *  base64字符串转化成图片
     * @param base64str
     * @param savepath
     * @return
     */
    public static boolean GenerateImage(String base64str,String savepath) {   //对字节数组字符串进行Base64解码并生成图片
        if (base64str == null) //图像数据为空
            return false;
        // System.out.println("开始解码");
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(base64str);
            //  System.out.println("解码完成");
            for(int i=0;i<b.length;++i) {
                if(b[i]<0) {//调整异常数据
                    b[i]+=256;
                }
            }
            // System.out.println("开始生成图片");
            //生成jpeg图片
            OutputStream out = new FileOutputStream(savepath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
