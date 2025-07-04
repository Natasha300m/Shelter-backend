package com.shelter.mykyda.dto;

import com.shelter.mykyda.database.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    private Long id;

    private String title;

    private String description;

    private String petType;

    private Integer petAge;

    private String need;

    private String authorRole;

    @Builder.Default
    private List<ImageDTO> images = new ArrayList<>();

    public static PostDTO mapToDTO(Post post) {
        return PostDTO.builder()
                .authorRole(post.getAuthorRole().name())
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .petType(post.getPetType())
                .petAge(post.getPetAge())
                .need(post.getNeed().name())
                .images(post.getImages() != null ?
                        post.getImages().stream().map(ImageDTO::mapToDTO).collect(Collectors.toList()) : null)
                .build();
    }

}
