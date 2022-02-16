package com.arahansa.learn_file_download.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileDownloadController {
    @GetMapping("/download")
    public String hi(){
        return "hi";
    }

    @GetMapping("/down")
    public ResponseEntity<Resource> download() throws IOException {
        Resource resource = new InputStreamResource(new URL("https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa2.png").openStream());

        String mimeType = "image/jpeg";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mimeType);
        headers.add("Content-Disposition", "attachment; filename='" + "villa2.png" + "'");
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("villa2.png", StandardCharsets.UTF_8).build());
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType(mimeType)).body(resource);
    }

    @RequestMapping(value="/zip", produces="application/zip")
    public void zipFiles(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        List<String> srcFiles = Arrays.asList("https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa2.png", "https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa40.png");
        for (String srcFile : srcFiles) {

            String substring = srcFile.substring(srcFile.lastIndexOf("/")+1);
            zipOutputStream.putNextEntry(new ZipEntry(substring));

            InputStreamResource inputStreamResource = new InputStreamResource(new URL(srcFile).openStream());
            IOUtils.copy(inputStreamResource.getInputStream(), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }
}


