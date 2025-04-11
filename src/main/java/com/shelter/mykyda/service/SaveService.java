package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Image;
import com.shelter.mykyda.database.repository.*;
import com.shelter.mykyda.dto.NewsItemSavePutDTO;
import com.shelter.mykyda.dto.PostPutDTO;
import com.shelter.mykyda.dto.PostSaveDTO;
import com.shelter.mykyda.dto.ShelterSaveDTO;
import com.shelter.mykyda.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final ShelterRepository shelterRepository;

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final NewsItemRepository newsItemRepository;

    private final UserRepository userRepository;

    private final MediaService mediaService;

    //:ToDo add Principle and ownership check
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> savePost(List<MultipartFile> images, PostSaveDTO dto) {

        var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var shelter = shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new NotFoundException("Shelter with such id not found"));
        var post = dto.unmap();
        post.setUser(user);
        post.setShelter(shelter);
        postRepository.saveAndFlush(post);

        if (images != null) {
            List<Image> postImages = new ArrayList<>();
            images.forEach(image -> postImages.add(Image.builder()
                    .post(post)
                    .url(mediaService.uploadPostImage(image))
                    .build()));
            imageRepository.saveAll(postImages);
        }
        return new ResponseEntity<>(post.getId(), HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> putPost(Long id, List<MultipartFile> images,List<Long> imagesToDelete, PostPutDTO dto) {

        var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var shelter = shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new NotFoundException("Shelter with such id not found"));
        var post = dto.unmap();
        post.setId(id);
        post.setUser(user);
        post.setShelter(shelter);
        postRepository.save(post);

        if (images != null) {
            List<Image> postImages = new ArrayList<>();
            images.forEach(image -> postImages.add(Image.builder()
                    .post(post)
                    .url(mediaService.uploadPostImage(image))
                    .build()));
            imageRepository.saveAll(postImages);
        }
        if (imagesToDelete != null) {
        imagesToDelete.forEach(imageId -> {
            var image = imageRepository.findById(Long.parseLong(imageId.toString()));
            if (image.isPresent()){
                mediaService.deletePostImage(image.get().getUrl());
                imageRepository.deleteById(Long.parseLong(imageId.toString()));
            }
        });
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> saveNewsItem(NewsItemSavePutDTO dto) {
        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var shelter = shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new NotFoundException("Shelter with such id not found"));
        var newsItem = dto.unmap();
        newsItem.setShelter(shelter);
        newsItemRepository.save(newsItem);
        return new ResponseEntity<>(newsItem.getId(),HttpStatus.OK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> putNewsItem(Long id, NewsItemSavePutDTO dto) {
        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var shelter = shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new NotFoundException("Shelter with such id not found"));
        var newsItem = dto.unmap();
        newsItem.setId(id);
        newsItem.setShelter(shelter);
        newsItemRepository.save(newsItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> saveShelter(ShelterSaveDTO dto) {
        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var shelter = dto.unmap();
        shelterRepository.save(shelter);
        return new ResponseEntity<>(shelter.getId(),HttpStatus.OK);
    }
}
