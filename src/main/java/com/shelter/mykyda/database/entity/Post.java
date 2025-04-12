package com.shelter.mykyda.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotBlank(message = "pet type is invalid")
    private String petType;

    @Min(value = 0, message = "pet age should be greater than 0")
    private Integer petAge;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "need is invalid")
    private Need need;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "author role is required")
    private Role authorRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Shelter shelter;

    @OneToMany(mappedBy = "post")
    private List<Image> images;

    public void setUser(User user) {
        this.user = user;
        this.authorRole = user.getRole();
    }
}
