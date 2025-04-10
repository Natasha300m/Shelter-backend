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
public class ShelterDTO {

    private Long id;

    private String contacts;

    private String location;

    private String name;

    public static ShelterDTO mapToDTO(Shelter shelter) {
        return ShelterDTO.builder()
                .id(shelter.getId())
                .contacts(shelter.getContacts())
                .location(shelter.getLocation())
                .name(shelter.getName())
                .build();
    }

}
