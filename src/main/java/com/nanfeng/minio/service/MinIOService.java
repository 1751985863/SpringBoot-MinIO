package com.nanfeng.minio.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * minio 操作类
 */
@Service
public class MinIOService {

    // 我最喜欢用@Resource 强烈建议不要用@autowire
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket.test}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 本地文件上传
     * @param localPath 本地路径
     * @param remotePath 远程路径
     * @return 可访问地址
     */
    public String upload(String localPath,String remotePath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(localPath)
                    .filename(remotePath)
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
            return endpoint + "/" + bucket + "/" + remotePath;
    }

    /**
     * 用流上传
     * @param is 文件流
     * @param remotePath 远程路径
     * @return 可访问地址
     */
    public String upload(InputStream is, String remotePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(remotePath)
                .stream(is, -1, 10485760)
                .build();
        minioClient.putObject(args);
        return endpoint + "/" + bucket + "/" + remotePath;
    }

    /**
     * 删除文件
     * @param remotePath 远程路径
     * @return
     */
    public void delete(String remotePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(remotePath)
                .build();
        minioClient.removeObject(args);
    }

    /**
     * 获取流
     * @param remotePath 远程路径
     */
    public InputStream getInputStream(String remotePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(remotePath)
                .build();
        return minioClient.getObject(args);
    }
}
