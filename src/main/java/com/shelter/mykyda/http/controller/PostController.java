package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.PostSaveDTO;
import com.shelter.mykyda.dto.PostDTO;
import com.shelter.mykyda.dto.PostPutDTO;
import com.shelter.mykyda.service.DeleteService;
import com.shelter.mykyda.service.PostService;
import com.shelter.mykyda.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final SaveService saveService;

    private final DeleteService deleteService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putPost(@PathVariable Long id,
                                          @RequestPart(value = "post") PostPutDTO dto,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                          @RequestPart(value = "imagesToDelete",required = false) List<Long> imagesToDelete) {
        return saveService.putPost(id, images, imagesToDelete, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id){
        return deleteService.deletePost(id);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getFilteredPosts(@RequestParam Map<String, String> filters) {
        return postService.findFiltered(filters);
    }

    @PostMapping
    public ResponseEntity<Long> postPost(@RequestPart(value = "images",required = false) List<MultipartFile> images,
                                         @RequestPart("post") PostSaveDTO post) {
        return saveService.savePost(images, post);
    }
}