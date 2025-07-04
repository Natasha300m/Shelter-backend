package com.shelter.mykyda.security.service;

import com.shelter.mykyda.dto.UserDTO;
import com.shelter.mykyda.database.entity.User;
import com.shelter.mykyda.security.config.JwtUtil;
import com.shelter.mykyda.security.dto.UserInfo;
import com.shelter.mykyda.security.exception.NotFoundException;
import com.shelter.mykyda.security.mapper.UserMapper;
import com.shelter.mykyda.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final ServerProperties serverProperties;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;


    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(@NotNull Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }

    public User findByUsername(@NotNull String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("user not found"));
    }


    public Optional<User> findByIdOptional(@NotNull Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsernameOptional(@NotNull String username) {
        return userRepository.findByEmail(username);
    }
    public Optional<User> findByEmailOptional(@NotNull String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(@NotNull String email) {
        return userRepository.existsUserByEmail(email);
    }

    public boolean existsByUsername(@NotNull String username) {
        return userRepository.existsUserByEmail(username);
    }


    //manipulations
//    @Transactional
//    public User save(@NotNull User newUser) {
//        if (userRepository.existsUserByUsername(newUser.getUsername())) {
//            throw new UsernameAlreadyExistsException("Username already exists");
//        }
//        if (newUser.getPassword() != null) {
//
//            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        }
//        return userRepository.save(newUser);
//    }

    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User save(@NotNull User newUser) {
        if (userRepository.existsUserByEmail(newUser.getEmail())) {
            throw new RuntimeException("User already exists");
        }
//        if(image != null) {
//            String avatar = imageService.uploadImage("avatars", image);
//            newUser.setImage(avatar);
//        }else {
//            Random random = new Random();
//            int randomNumber = random.nextInt(10) + 1;
//            newUser.setImage(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()+"/avatars/"+randomNumber+".jpg");
//        }

        if (newUser.getPassword() != null) {

            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userRepository.save(newUser);
    }
    @Transactional
    public boolean delete(@NotNull Long id) {

        try{
            userRepository.deleteById(id);
//            deleteCookie();
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean deleteByUsername(@NotNull String username) {
        try{
            userRepository.deleteByEmail(username);
//            deleteCookie();
            return true;
        }catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }


    @Transactional
    public UserDTO updatePassword(Principal principal, String password) {
        User user = findByUsername(principal.getName());
        if (password!=null && !password.isBlank()){
            user.setPassword(passwordEncoder.encode(password));
        }
        return userMapper.userToUserDto(userRepository.save(user));
    }
//    public User update(@NotNull UserUpdateDto userUpdateDto) {
//
//        return userRepository.save(userUpdateDto);
//    }

    //system

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }

    public ResponseEntity<String> updateUsername(Principal principal, String username) {

        if (principal.getName().equals(username)){
            return ResponseEntity.badRequest().body("Username already set");
        }
        if (existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = findByUsername(principal.getName());
        User save = userRepository.save(user);
        setCookie(save);
        return ResponseEntity.ok("Username updated: " + username);
    }
    @Transactional
    public ResponseEntity<UserDTO> updateInfo(Principal principal, UserInfo info) {
        User user = findByUsername(principal.getName());

        User update = update(user);
        return ResponseEntity.ok(userMapper.userToUserDto(update));
    }

    public ResponseEntity<String> updateEmail(Principal principal, String email) {
        User user = findByUsername(principal.getName());
        if (user.getEmail().equals(email)){
            return ResponseEntity.badRequest().body("Email already set");
        }
        if (existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already register");
        }
        user.setEmail(email);
        update(user);
        return ResponseEntity.ok("User email updated: " + email);
    }


    private void setCookie(User user) {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes()))
                .getResponse();
        ResponseCookie responseCookie = ResponseCookie.from("accessToken", jwtUtil.generateToken(user)).
                httpOnly(true).
                maxAge(24_192_000).
                sameSite("None").
                path("/").
                secure(true).
                build();
        Objects.requireNonNull(response).addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
