package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.PostFullDTO;
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

import java.security.Principal;
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
    public ResponseEntity<PostFullDTO> getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putPost(@PathVariable Long id,
                                          @RequestPart(value = "post") PostPutDTO dto,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                          Principal principal) {
        return saveService.putPost(id, images, dto, principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id,
                                             Principal principal) {
        return deleteService.deletePost(id, principal);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getFilteredPosts(@RequestParam Map<String, String> filters) {
        return postService.findFiltered(filters);
    }

    @PostMapping
    public ResponseEntity<Long> postPost(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                         @RequestPart("post") PostSaveDTO post,
                                         Principal principal) {
        return saveService.savePost(images, post, principal);
    }
}