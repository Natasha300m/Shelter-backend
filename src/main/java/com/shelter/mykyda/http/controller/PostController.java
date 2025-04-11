package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.PostDTO;
import com.shelter.mykyda.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id){
        return postService.findById(id);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getFilteredPosts(@RequestParam Map<String, String> filters){
        return postService.findFiltered(filters);
    }
}