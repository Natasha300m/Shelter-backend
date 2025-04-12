package com.shelter.mykyda.service;

import com.shelter.mykyda.database.repository.*;
import com.shelter.mykyda.exeption.NotFoundException;
import com.shelter.mykyda.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteService {

    private final ShelterRepository shelterRepository;

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final NewsItemRepository newsItemRepository;

    private final UserRepository userRepository;

    private final MediaService mediaService;

    //:ToDo add Principle and ownership check
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deletePost(Long id) {

        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with such id not found"));
        var images = post.getImages();
        if (images != null) {
            images.forEach(image -> {
                mediaService.deletePostImage(image.getUrl());
                imageRepository.deleteById(image.getId());
            });
        }
        postRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deleteNewsItem(Long id) {
        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        newsItemRepository.findById(id).orElseThrow(() -> new NotFoundException("News item with such id not found"));
        newsItemRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deleteShelter(Long id) {
        //var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        shelterRepository.findById(id).orElseThrow(() -> new NotFoundException("News item with such id not found"));
        shelterRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
