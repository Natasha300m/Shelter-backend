package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Post;
import com.shelter.mykyda.database.repository.PostRepository;
import com.shelter.mykyda.database.specification.PostSpecification;
import com.shelter.mykyda.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<PostDTO> findById(Long id) {
        var post = postRepository.findById(id);
        return post.map(value -> new ResponseEntity<>(PostDTO.mapToDTO(value), HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<PostDTO>> findFiltered(Map<String,String> filters) {
        Specification<Post> spec = Specification.where(null);
        if (filters.get("shelterId") != null && !filters.get("shelterId").isEmpty()){
            spec = spec.and(PostSpecification.hasShelterId(Long.parseLong(filters.get("shelterId"))));
        }
        if (filters.get("userId") != null && !filters.get("userId").isEmpty()){
            spec = spec.and(PostSpecification.hasUserId(Long.parseLong(filters.get("userId"))));
        }
        if (filters.get("need") != null && !filters.get("need").isEmpty()){
            spec = spec.and(PostSpecification.hasNeed(filters.get("need").toUpperCase()));
        }
        if (filters.get("title") != null && !filters.get("title").isEmpty()){
            spec = spec.and(PostSpecification.hasTitle(filters.get("title")));
        }
        if (filters.get("petAge") != null && !filters.get("petAge").isEmpty()){
            spec = spec.and(PostSpecification.hasPetAge(Integer.parseInt(filters.get("petAge"))));
        }
        if (filters.get("petType") != null && !filters.get("petType").isEmpty()){
            spec = spec.and(PostSpecification.hasPetType(filters.get("petType").toLowerCase()));
        }
        if (filters.get("authorRole") != null && !filters.get("authorRole").isEmpty()){
            spec = spec.and(PostSpecification.hasAuthorRole(filters.get("authorRole").toUpperCase()));
        }
        var posts = postRepository.findAll(spec);
        if (!posts.isEmpty()){
           return new ResponseEntity<>(posts.stream().map(PostDTO::mapToDTO).collect(Collectors.toList()),HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
