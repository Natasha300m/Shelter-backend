package com.shelter.mykyda.database.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shelter")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "location is invalid")
    private String location;

    //Todo:validate
    private String contacts;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelter")
    @JsonBackReference
    private List<User> managers = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelter")
    @JsonBackReference
    private List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelter")
    @JsonBackReference
    private List<NewsItem> news = new ArrayList<>();
}
