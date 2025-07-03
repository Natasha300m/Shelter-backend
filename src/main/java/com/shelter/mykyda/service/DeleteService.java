package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Role;
import com.shelter.mykyda.database.repository.*;
import com.shelter.mykyda.exeption.NotFoundException;
import com.shelter.mykyda.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeleteService {

    private final ShelterRepository shelterRepository;

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final NewsItemRepository newsItemRepository;

    private final UserRepository userRepository;

    private final MediaService mediaService;

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deletePost(Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post with such id not found"));
        if (Objects.equals(user.getId(), post.getUser().getId())) {
            var images = post.getImages();
            if (images != null) {
                images.forEach(image -> {
                    mediaService.deletePostImage(image.getUrl());
                    imageRepository.deleteById(image.getId());
                });
            }
            postRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deleteNewsItem(Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        var item = newsItemRepository.findById(id).orElseThrow(() -> new NotFoundException("NewsItem with such id not found"));
        if (user.getRole() == Role.MANAGER && user.getShelter() == item.getShelter()) {
            newsItemRepository.findById(id).orElseThrow(() -> new NotFoundException("News item with such id not found"));
            newsItemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<String> deleteShelter(Long id, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User with such email not found"));
        if (user.getRole() == Role.MANAGER && user.getShelter()!=null) {
            if (Objects.equals(user.getShelter().getId(), id)) {
                shelterRepository.findById(id).orElseThrow(() -> new NotFoundException("News item with such id not found"));
                user.setShelter(null);
                userRepository.save(user);
                shelterRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
