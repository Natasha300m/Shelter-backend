package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Image;
import com.shelter.mykyda.database.entity.Role;
import com.shelter.mykyda.database.repository.ImageRepository;
import com.shelter.mykyda.database.repository.NewsItemRepository;
import com.shelter.mykyda.database.repository.PostRepository;
import com.shelter.mykyda.database.repository.ShelterRepository;
import com.shelter.mykyda.dto.NewsItemSavePutDTO;
import com.shelter.mykyda.dto.PostPutDTO;
import com.shelter.mykyda.dto.PostSaveDTO;
import com.shelter.mykyda.dto.ShelterSaveDTO;
import com.shelter.mykyda.exeption.NotFoundException;
import com.shelter.mykyda.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final ShelterRepository shelterRepository;

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final NewsItemRepository newsItemRepository;

    private final UserRepository userRepository;

    private final MediaService mediaService;

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> savePost(List<MultipartFile> images, PostSaveDTO dto, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var post = dto.unmap();
        post.setUser(user);
        post.setShelter(user.getShelter());
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
    public ResponseEntity<String> putPost(Long id, List<MultipartFile> images, PostPutDTO dto, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var postFound = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with such id not found"));
        if (Objects.equals(user.getId(), postFound.getUser().getId())) {
            var post = dto.unmap();
            post.setId(id);
            post.setUser(user);
            post.setShelter(postFound.getShelter());
            postRepository.save(post);

            var imagesToDelete = imageRepository.findAllByPostId(id);
            if (images != null) {
                if (imagesToDelete != null) {
                    imagesToDelete.forEach(image -> {
                        mediaService.deletePostImage(image.getUrl());
                        imageRepository.deleteById(image.getId());
                    });
                }

                List<Image> newImages = new ArrayList<>();
                images.forEach(image -> newImages.add(Image.builder()
                        .post(post)
                        .url(mediaService.uploadPostImage(image))
                        .build()));
                imageRepository.saveAll(newImages);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> saveNewsItem(NewsItemSavePutDTO dto,Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        if (user.getRole() == Role.MANAGER && Objects.equals(user.getShelter().getId(), dto.getShelterId())) {
            var shelter = shelterRepository.findById(dto.getShelterId()).orElseThrow(() -> new NotFoundException("Shelter with such id not found"));
            var newsItem = dto.unmap();
            newsItem.setShelter(shelter);
            newsItemRepository.save(newsItem);
            return new ResponseEntity<>(newsItem.getId(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> putNewsItem(Long id, NewsItemSavePutDTO dto, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var item = newsItemRepository.findById(id).orElseThrow(() -> new NotFoundException("NewsItem with such id not found"));
        if (user.getRole() == Role.MANAGER && user.getShelter() == item.getShelter()) {
            var newsItem = dto.unmap();
            newsItem.setId(id);
            newsItem.setShelter(item.getShelter());
            newsItemRepository.save(newsItem);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<Long> saveShelter(ShelterSaveDTO dto, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        if (user.getRole() == Role.MANAGER && user.getShelter() == null) {
            var shelter = dto.unmap();
            shelterRepository.saveAndFlush(shelter);
            user.setShelter(shelter);
            userRepository.save(user);
            return new ResponseEntity<>(shelter.getId(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
