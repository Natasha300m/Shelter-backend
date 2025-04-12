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
public class PostFullDTO {

    private Long id;

    private String title;

    private String description;

    private String petType;

    private Integer petAge;

    private String need;

    private String authorRole;

    private ShelterDTO shelter;

    private UserDTO user;

    private List<ImageDTO> images = new ArrayList<>();

    public static PostFullDTO mapToDTO(Post post) {
        return PostFullDTO.builder()
                .authorRole(post.getAuthorRole().name())
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .petType(post.getPetType())
                .petAge(post.getPetAge())
                .need(post.getNeed().name())
                .user(post.getUser() != null ?
                        UserDTO.mapWithoutDependency(post.getUser()) : null)
                .shelter(post.getShelter() != null ?
                        ShelterDTO.mapToDTO(post.getShelter()) : null)
                .images(post.getImages() != null ?
                        post.getImages().stream().map(ImageDTO::mapToDTO).collect(Collectors.toList()) : null)
                .build();
    }

}
