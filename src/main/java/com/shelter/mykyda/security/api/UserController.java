package com.shelter.mykyda.security.api;

import com.shelter.mykyda.dto.UserDTO;
import com.shelter.mykyda.security.dto.UserInfo;
import com.shelter.mykyda.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(Principal principal) {
        if(userService.deleteByUsername(principal.getName())){
            return ResponseEntity.ok("Account deleted successfully");
        }else {
            return ResponseEntity.badRequest().body("Account could not be deleted");
        }
    }



    @PatchMapping(value = "/updatePassword")
    public ResponseEntity<UserDTO> updatePassword(Principal principal, @RequestBody String password) {
        return ResponseEntity.ok(userService.updatePassword(principal,password));
    }

    @PatchMapping("/updateUsername")
    public ResponseEntity<String> updateUsername(Principal principal, @RequestBody String username) {
        if (username == null || username.length() < 3 || username.length() > 20) {
            return ResponseEntity.badRequest().body("Username must be between 6 and 30 characters");
        }
        return userService.updateUsername(principal,username);
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<UserDTO> updateInfo(Principal principal, @RequestBody UserInfo info) {
        return userService.updateInfo(principal,info);
    }

    @PatchMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(Principal principal, @RequestBody String email) {
        return userService.updateEmail(principal,email);
    }


}
