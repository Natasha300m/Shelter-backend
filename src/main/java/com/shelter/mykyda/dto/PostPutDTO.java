package com.shelter.mykyda.dto;

import com.shelter.mykyda.database.entity.Need;
import com.shelter.mykyda.database.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPutDTO {

    private String title;

    private String desc;

    private String petType;

    private Integer petAge;

    private String needs;

    private Long shelterId;

    public Post unmap() {
        return Post.builder()
                .title(title)
                .description(desc)
                .petType(petType.substring(0, 1).toUpperCase() + petType.substring(1).toLowerCase())
                .petAge(petAge)
                .need(Need.valueOf(needs.toUpperCase()))
                .build();
    }
}
