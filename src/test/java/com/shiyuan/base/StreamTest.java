package com.shiyuan.base;

import org.junit.jupiter.api.Test;

import java.io.*;

public class StreamTest {

    @Test
    public void testInputStream() throws IOException {
        // 读取当前文件同目录下的data.txt文件
        try(BufferedReader reader  = new BufferedReader(new InputStreamReader(new FileInputStream("uploads/data.txt")))){
            char[] bytes = new char[1024];
            int len = reader.read(bytes, 0, 24);
            while(len != -1){
                String str = new String(bytes,0, len);
                System.out.println(str);
                len = reader.read(bytes, 0, 24);
            }
        }
        try (InputStream inputStream = new FileInputStream("uploads/data.txt")) {
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            while (len > 0) {
                for (int i = 0; i < len; i++) {
                    System.out.print(bytes[i] + " ");
                }
//                System.out.println(new String(bytes, 0, len));
                len = inputStream.read(bytes);
            }
        }
    }
}
