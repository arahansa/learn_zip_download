package com.arahansa.learn_file_download;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestHttpJava {
    public static void main(String[] args) throws IOException {
        List<String> srcFiles = Arrays.asList("https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa2.png", "https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa40.png");
        FileOutputStream fos = new FileOutputStream("multiCompressed2.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : srcFiles) {

            String substring = srcFile.substring(srcFile.lastIndexOf("/"));
            System.out.println("substring :"+substring);
            ZipEntry zipEntry = new ZipEntry(substring);
            zipOut.putNextEntry(zipEntry);

            InputStream inputStream = new URL(srcFile).openStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                zipOut.write(dataBuffer, 0, bytesRead);
            }
            inputStream.close();
            in.close();
        }
        zipOut.close();
        fos.close();
    }
}
