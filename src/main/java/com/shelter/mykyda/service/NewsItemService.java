package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.NewsItem;
import com.shelter.mykyda.database.repository.NewsItemRepository;
import com.shelter.mykyda.database.specification.NewsItemSpecification;
import com.shelter.mykyda.dto.NewsItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsItemService {

    private final NewsItemRepository newsRepository;

    private final Integer NEWS_PAGE_SIZE = 2;

    @Transactional(readOnly = true)
    public ResponseEntity<NewsItemDTO> findById(Long id) {
        var newsItem = newsRepository.findById(id);
        return newsItem.map(value -> new ResponseEntity<>(NewsItemDTO.mapToDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<NewsItemDTO>> findFiltered(Map<String, String> filters) {

        Specification<NewsItem> spec = Specification.where(null);

        if (filters.get("shelterId") != null && !filters.get("shelterId").isEmpty()) {
            spec = spec.and(NewsItemSpecification.hasShelterId(Long.parseLong(filters.get("shelterId"))));
        }

        if (filters.get("title") != null && !filters.get("title").isEmpty()) {
            spec = spec.and(NewsItemSpecification.hasTitle(filters.get("title")));
        }


        var page = 0;
        if (filters.get("page") != null && !filters.get("page").isEmpty()) {
            page = Integer.parseInt(filters.get("page"));
        }
        var newsItems = newsRepository.findAll(spec, PageRequest.of(page, NEWS_PAGE_SIZE));
        if (!newsItems.isEmpty()) {
            return new ResponseEntity<>(newsItems.stream().map(NewsItemDTO::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
}
