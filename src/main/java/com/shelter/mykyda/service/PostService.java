package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Post;
import com.shelter.mykyda.database.entity.Role;
import com.shelter.mykyda.database.repository.PostRepository;
import com.shelter.mykyda.database.specification.PostSpecification;
import com.shelter.mykyda.dto.PostDTO;
import com.shelter.mykyda.dto.PostFullDTO;
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
public class PostService {

    private final PostRepository postRepository;

    private final Integer POSTS_PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    public ResponseEntity<PostFullDTO> findById(Long id) {
        var post = postRepository.findById(id);
        return post.map(value -> new ResponseEntity<>(PostFullDTO.mapToDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostDTO>> findFiltered(Map<String, String> filters) {

        Specification<Post> spec = Specification.where(null);

        if (filters.get("shelterId") != null && !filters.get("shelterId").isEmpty()) {
            spec = spec.and(PostSpecification.hasShelterId(Long.parseLong(filters.get("shelterId"))));
        }

        if (filters.get("userId") != null && !filters.get("userId").isEmpty()) {
            spec = spec.and(PostSpecification.hasUserId(Long.parseLong(filters.get("userId"))));
        }

        if (filters.get("needs") != null && !filters.get("needs").isEmpty()) {
            spec = spec.and(PostSpecification.hasNeed(filters.get("needs").toUpperCase()));
        }

        if (filters.get("title") != null && !filters.get("title").isEmpty()) {
            spec = spec.and(PostSpecification.hasTitle(filters.get("title")));
        }

        if (filters.get("petAge") != null && !filters.get("petAge").isEmpty()) {
            var petAgeFilter = filters.get("petAge");
            var age = petAgeFilter.substring(4, 5);
            var mark = petAgeFilter.substring(0, 4);
            if (mark.equals("more")) {
                spec = spec.and(PostSpecification.hasPetAgeAbove(Integer.parseInt(age)));
            } else {
                spec = spec.and(PostSpecification.hasPetAgeBelow(Integer.parseInt(age)));
            }
        }

        if (filters.get("petType") != null && !filters.get("petType").isEmpty()) {
            var type = filters.get("petType");
            type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
            if (type.equals("Dog") || type.equals("Cat")) {
                spec = spec.and(PostSpecification.hasPetType(type));
            } else {
                List<String> excludedTypes = List.of("Cat", "Dog");
                spec = spec.and(PostSpecification.petTypeNotIn(excludedTypes));
            }
        }

        if (filters.get("volunteers") != null && !filters.get("volunteers").isEmpty()) {
            var volunteer = Boolean.parseBoolean(filters.get("volunteers"));
            if (volunteer) {
                spec = spec.and(PostSpecification.hasAuthorRole(Role.VOLUNTEER.name()));
            }
        }

        var page = 0;
        if (filters.get("page") != null && !filters.get("page").isEmpty()) {
            page = Integer.parseInt(filters.get("page"));
        }
        var posts = postRepository.findAll(spec, PageRequest.of(page, POSTS_PAGE_SIZE));
        if (!posts.isEmpty()) {
            return new ResponseEntity<>(posts.stream().map(PostDTO::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
}
