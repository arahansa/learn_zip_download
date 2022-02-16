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
        // 파일을 Azure Blob Storage에서 다운 받아온다.
        Resource resource = new InputStreamResource(new URL("https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa2.png").openStream());

        String mimeType = "image/jpeg";
        // 파일을 다운 받을 때 첨부 파일 형식으로 받을 수 있도록 헤더값을 설정한다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mimeType);
        headers.add("Content-Disposition", "attachment; filename='" + "villa2.png" + "'");
        headers.setContentDisposition(ContentDisposition.attachment()
                // 사용자에게 보여질 파일 이름을 설정한다.
                .filename("villa2.png", StandardCharsets.UTF_8).build());

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType(mimeType)).body(resource);
    }

    @RequestMapping(value="/zip", produces="application/zip")
    public void zipFiles(HttpServletResponse response) throws IOException {

        //setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        List<String> srcFiles = Arrays.asList("https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa2.png", "https://d2jneiw56ezkg5.cloudfront.net/card/gangnamgu/villa40.png");
        // package files
        for (String srcFile : srcFiles) {
            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
            String substring = srcFile.substring(srcFile.lastIndexOf("/")+1);
            zipOutputStream.putNextEntry(new ZipEntry(substring));

            InputStreamResource inputStreamResource = new InputStreamResource(new URL(srcFile).openStream());
            IOUtils.copy(inputStreamResource.getInputStream(), zipOutputStream);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }
}


