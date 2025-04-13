package com.shelter.mykyda.service;

import com.shelter.mykyda.database.entity.Shelter;
import com.shelter.mykyda.database.repository.ShelterRepository;
import com.shelter.mykyda.database.specification.ShelterSpecification;
import com.shelter.mykyda.dto.ShelterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;

    private final Integer SHELTERS_PAGE_SIZE = 10;

    @Transactional(readOnly = true)
    public ResponseEntity<ShelterDTO> findById(Long id) {
        var shelter = shelterRepository.findById(id);
        return shelter.map(value -> new ResponseEntity<>(ShelterDTO.mapToDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<ShelterDTO>> findFiltered(Map<String, String> filters) {

        Specification<Shelter> spec = Specification.where(null);

        if (filters.get("name") != null && !filters.get("name").isEmpty()) {
            spec = spec.and(ShelterSpecification.hasName(filters.get("name")));
        }

        if (filters.get("location") != null && !filters.get("location").isEmpty()) {
            spec = spec.and(ShelterSpecification.hasLocation(filters.get("location")));
        }


        var page = 0;
        if (filters.get("page") != null && !filters.get("page").isEmpty()) {
            page = Integer.parseInt(filters.get("page"));
        }
        var shelters = shelterRepository.findAll(spec, PageRequest.of(page, SHELTERS_PAGE_SIZE));
        if (!shelters.isEmpty()) {
            return new ResponseEntity<>(shelters.stream().map(ShelterDTO::mapToDTO).collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
    }
}
