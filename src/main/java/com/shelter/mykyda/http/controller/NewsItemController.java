package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.NewsItemDTO;
import com.shelter.mykyda.dto.NewsItemSavePutDTO;
import com.shelter.mykyda.service.DeleteService;
import com.shelter.mykyda.service.NewsItemService;
import com.shelter.mykyda.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/newsItems")
@RequiredArgsConstructor
public class NewsItemController {

    private final NewsItemService newsItemService;

    private final SaveService saveService;

    private final DeleteService deleteService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsItemDTO> getPost(@PathVariable Long id) {
        return newsItemService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putNewsItem(@PathVariable Long id,
                                              @RequestBody NewsItemSavePutDTO dto) {
        return saveService.putNewsItem(id, dto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNewsItem(@PathVariable Long id) {
        return deleteService.deleteNewsItem(id);

    }

    @PostMapping()
    public ResponseEntity<Long> postNewsItem(@RequestBody NewsItemSavePutDTO dto) {
        return saveService.saveNewsItem(dto);

    }

    @GetMapping()
    public ResponseEntity<List<NewsItemDTO>> getFilteredPosts(@RequestParam Map<String, String> filters) {
        return newsItemService.findFiltered(filters);
    }
}