package org.ruoxue.backend.util;

import java.io.*;

public class IO {
    public static byte[] read(String inputFileName) throws Exception{
        File file = new File(inputFileName);
        byte[] result = new byte[(int)file.length()];
        InputStream input = null;

        int totalBytesRead = 0;
        input = new BufferedInputStream(new FileInputStream(file));
        while(totalBytesRead < result.length){
            int bytesRemaining = result.length - totalBytesRead;
            //input.read() returns -1, 0, or more :
            int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
            if (bytesRead > 0){
                totalBytesRead = totalBytesRead + bytesRead;
            }
        }

        return result;
    }
}
