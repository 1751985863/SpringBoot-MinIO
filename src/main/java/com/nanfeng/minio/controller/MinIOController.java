package com.nanfeng.minio.controller;


import com.nanfeng.minio.service.MinIOService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
public class MinIOController {

    @Resource
    private MinIOService minIOService;

    @PostMapping("/upload")
    public HashMap<String, String> upload(@RequestParam(name = "file", required = false) MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileName = System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = minIOService.upload(file.getInputStream(), fileName);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url",url);
        map.put("fileName",fileName);
        return map;
    }

    @GetMapping("/remove")
    public String remove(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minIOService.delete(fileName);
        return "success";
    }
}
