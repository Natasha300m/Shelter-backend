package com.shelter.mykyda.database.specification;

import com.shelter.mykyda.database.entity.Shelter;
import org.springframework.data.jpa.domain.Specification;

public class ShelterSpecification {

    public static Specification<Shelter> hasLocation(String location) {
        return (root, query, builder) ->
                builder.equal(root.get("location"), location);
    }

    public static Specification<Shelter> hasName(String name) {
        return (root, query, builder) ->
                builder.equal(root.get("name"), name);
    }
}
