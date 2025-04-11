package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.NewsItemDTO;
import com.shelter.mykyda.service.NewsItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsItemController {

    private final NewsItemService newsItemService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsItemDTO> getPost(@PathVariable Long id){
        return newsItemService.findById(id);
    }

    @GetMapping()
    public ResponseEntity<List<NewsItemDTO>> getFilteredPosts(@RequestParam Map<String, String> filters){
        return newsItemService.findFiltered(filters);
    }
}