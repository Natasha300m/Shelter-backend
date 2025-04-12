package com.shelter.mykyda.dto;

import com.shelter.mykyda.database.entity.Shelter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelterSaveDTO {

    private String contacts;

    private String location;

    private String name;

    public Shelter unmap() {
        return Shelter.builder()
                .contacts(contacts)
                .location(location)
                .name(name)
                .build();
    }

}
