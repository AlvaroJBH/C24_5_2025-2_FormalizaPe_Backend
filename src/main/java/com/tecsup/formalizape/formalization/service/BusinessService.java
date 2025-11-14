package com.tecsup.formalizape.formalization.service;

import com.tecsup.formalizape.formalization.model.Business;
import com.tecsup.formalizape.formalization.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;

    public List<Business> getAll() {
        return businessRepository.findAll();
    }

    public Business getById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }

    public List<Business> getByUserId(Long userId) {
        return businessRepository.findByOwnerId(userId);
    }

    public Business create(Business business) {
        return businessRepository.save(business);
    }

    public Business update(Long id, Business business) {
        Business existing = getById(id);
        existing.setName(business.getName());
        existing.setDescription(business.getDescription());
        existing.setStatus(business.getStatus());
        existing.setSector(business.getSector());
        return businessRepository.save(existing);
    }

    public void delete(Long id) {
        businessRepository.deleteById(id);
    }
}
