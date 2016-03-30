package com.silence.account.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Silence on 2016/2/11 0011.
 */
public class FileUtils {
    private FileUtils() {
    }

    public static void writeFile(String source, String dest) {
        InputStream inputStream = null;
        OutputStream fileOutputStream = null;
        try {
            inputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(dest);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
