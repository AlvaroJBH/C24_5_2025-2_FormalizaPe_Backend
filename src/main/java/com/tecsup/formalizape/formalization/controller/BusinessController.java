package com.tecsup.formalizape.formalization.controller;

import com.tecsup.formalizape.auth.model.User;
import com.tecsup.formalizape.auth.repository.UserRepository;
import com.tecsup.formalizape.formalization.dto.BusinessDTO;
import com.tecsup.formalizape.formalization.mapper.BusinessMapper;
import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;

    // GET all businesses
    @GetMapping
    public ResponseEntity<List<BusinessDTO.ResponseDTO>> getMyBusinesses(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Business> businesses = businessService.getByUserId(user.getId());
        List<BusinessDTO.ResponseDTO> response = businesses.stream()
                .map(businessMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // GET business by id
    @GetMapping("/{id}")
    public ResponseEntity<BusinessDTO.ResponseDTO> getById(@PathVariable Long id) {
        Business business = businessService.getById(id);
        return ResponseEntity.ok(businessMapper.toResponse(business));
    }

    // GET businesses by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BusinessDTO.ResponseDTO>> getByUserId(@PathVariable Long userId) {
        List<Business> businesses = businessService.getByUserId(userId);
        List<BusinessDTO.ResponseDTO> response = businesses.stream()
                .map(businessMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // POST create business
    @PostMapping
    public ResponseEntity<BusinessDTO.ResponseDTO> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BusinessDTO.RequestDTO request
    ) {
        Business business = businessMapper.toEntity(request);
        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        business.setOwner(owner);
        Business created = businessService.create(business);
        return ResponseEntity.ok(businessMapper.toResponse(created));
    }

    // PUT update business
    @PutMapping("/{id}")
    public ResponseEntity<BusinessDTO.ResponseDTO> update(@PathVariable Long id,
                                                          @RequestBody BusinessDTO.RequestDTO request) {
        Business business = businessMapper.toEntity(request);
        Business updated = businessService.update(id, business);
        return ResponseEntity.ok(businessMapper.toResponse(updated));
    }

    // DELETE business
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        businessService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
