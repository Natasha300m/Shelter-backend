package com.shelter.mykyda.database.specification;

import com.shelter.mykyda.database.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PostSpecification {

    public static Specification<Post> hasNeed(String need) {
        return (root, query, builder) ->
                builder.equal(root.get("need"), need);
    }

    public static Specification<Post> hasPetType(String petType) {
        return (root, query, builder) ->
                builder.equal(root.get("petType"), petType);
    }

    public static Specification<Post> hasTitle(String title) {
        return (root, query, builder) ->
                builder.equal(root.get("title"), title);
    }

    public static Specification<Post> hasPetAge(Integer petAge) {
        return (root, query, builder) ->
                builder.equal(root.get("petAge"), petAge);
    }

    public static Specification<Post> hasShelterId(Long shelterId) {
        return (root, query, builder) ->
                builder.equal(root.get("shelter").get("id"), shelterId);
    }

    public static Specification<Post> hasUserId(Long userId) {
        return (root, query, builder) ->
                builder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Post> hasAuthorRole(String authorRole) {
        return (root, query, builder) ->
                builder.equal(root.get("authorRole"), authorRole);
    }
}
