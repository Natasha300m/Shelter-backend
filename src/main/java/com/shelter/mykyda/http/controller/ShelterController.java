package com.shelter.mykyda.http.controller;

import com.shelter.mykyda.dto.ShelterDTO;
import com.shelter.mykyda.dto.ShelterSaveDTO;
import com.shelter.mykyda.service.DeleteService;
import com.shelter.mykyda.service.SaveService;
import com.shelter.mykyda.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


//TODO: Shelter id ignore
@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    private final SaveService saveService;

    private final DeleteService deleteService;

    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelter(@PathVariable Long id){
        return shelterService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShelter(@PathVariable Long id){
        return deleteService.deleteShelter(id);
    }

    @GetMapping()
    public ResponseEntity<List<ShelterDTO>> getFilteredShelters(@RequestParam Map<String, String> filters){
        return shelterService.findFiltered(filters);
    }

    @PostMapping()
    public ResponseEntity<Long> postShelter(@RequestBody ShelterSaveDTO dto){
        return saveService.saveShelter(dto);
    }

}