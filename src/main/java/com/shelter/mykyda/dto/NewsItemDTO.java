package com.shelter.mykyda.dto;

import com.shelter.mykyda.database.entity.NewsItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsItemDTO {

    private Long id;

    private String title;

    private String content;

    private ShelterDTO shelter;

    public static NewsItemDTO mapToDTO(NewsItem item) {
        return NewsItemDTO.builder()
                .id(item.getId())
                .content(item.getContent())
                .title(item.getTitle())
                .shelter(item.getShelter()!= null ?
                        ShelterDTO.mapToDTO(item.getShelter()) : null)
                .build();
    }
}
