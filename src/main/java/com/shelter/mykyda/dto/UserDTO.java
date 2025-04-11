package com.shelter.mykyda.dto;

import com.shelter.mykyda.database.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String contacts;

    private String email;

    private String role;

    private ShelterDTO shelter;

    public static UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .contacts(user.getContacts())
                .email(user.getEmail())
                .role(user.getRole().name())
                .shelter(user.getShelter() != null ? ShelterDTO.mapToDTO(user.getShelter()) : null)
                .build();
    }
    public static UserDTO mapWithoutDependency(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .contacts(user.getContacts())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

}
