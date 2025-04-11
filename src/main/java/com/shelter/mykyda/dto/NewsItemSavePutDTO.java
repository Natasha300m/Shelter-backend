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
public class NewsItemSavePutDTO {

    private String title;

    private String content;

    private Long shelterId;

    public NewsItem unmap() {
        return NewsItem.builder()
                .content(content)
                .title(title)
                .build();
    }
}
