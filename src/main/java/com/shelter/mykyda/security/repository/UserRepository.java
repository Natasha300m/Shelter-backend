package com.shelter.mykyda.security.repository;

import com.shelter.mykyda.database.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(@NotBlank String email);



    boolean existsUserByEmail(@NotBlank String email);


    void deleteByEmail(@Email(message = "email is invalid") @NotNull(message = "email is required") String email);
}
