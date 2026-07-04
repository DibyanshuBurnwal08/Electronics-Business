package com.Business.Electronics.Service;

import com.Business.Electronics.Entity.ElectronicEntity;
import com.Business.Electronics.Repository.ElectronicsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ElectronicService {

    private final ElectronicsRepo electronicsRepo;

    public ElectronicEntity addProduct(ElectronicEntity entity) {
        entity = electronicsRepo.save(entity);
        return entity;
    }

    public void deleteProduct(Long id) {
        ElectronicEntity byId = electronicsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item Not Found"));
        electronicsRepo.delete(byId);
    }

    public List<ElectronicEntity> getProducts() {
        return electronicsRepo.findAll();
    }

    public Integer totalProducts() {
        return electronicsRepo.totalProducts();
    }

    public Integer avlProducts() {
        return electronicsRepo.avlProducts();
    }

    public List<ElectronicEntity> recentProducts() {
        return electronicsRepo.findTop10ByOrderByCreatedAtDesc();
    }
}
