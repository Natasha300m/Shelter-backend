package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.ShelterDTO;
import com.shelter.mykyda.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelter(@PathVariable Long id){
        return shelterService.findById(id);
    }

    @GetMapping()
    public ResponseEntity<List<ShelterDTO>> getFilteredPosts(@RequestParam Map<String, String> filters){
        return shelterService.findFiltered(filters);
    }
}