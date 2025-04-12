package com.shelter.mykyda.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final S3Client s3Client;

    @SneakyThrows
    public String uploadPostImage(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String url = "images/" + UUID.randomUUID() + filename;
        PutObjectRequest request = PutObjectRequest.builder()
                .key(url)
                .bucket("shelterpostimagebucket")
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(multipartFile.getBytes()));
        return s3Client.utilities().getUrl(e -> e.bucket("shelterpostimagebucket").key(url)).toString();
    }

    @SneakyThrows
    public DeleteObjectResponse deletePostImage(String url) {
        var uri = new URI(url);
        var deleteKeyUri = uri.getPath().substring(1);
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .key(deleteKeyUri)
                .bucket("shelterpostimagebucket")
                .build();
        return s3Client.deleteObject(deleteObjectRequest);
    }
}
