package com.shelter.mykyda.database.specification;

import com.shelter.mykyda.database.entity.NewsItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NewsItemSpecification {

    public static Specification<NewsItem> hasTitle(String title) {
        return (root, query, builder) ->
                builder.equal(root.get("title"), title);
    }

    public static Specification<NewsItem> hasShelterId(Long shelterId) {
        return (root, query, builder) ->
                builder.equal(root.get("shelter").get("id"), shelterId);
    }
}
