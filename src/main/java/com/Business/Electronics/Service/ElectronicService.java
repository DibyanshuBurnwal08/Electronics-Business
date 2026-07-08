package com.Business.Electronics.Service;

import com.Business.Electronics.DTO.AddProDTO;
import com.Business.Electronics.Entity.ElectronicEntity;
import com.Business.Electronics.Repository.ElectronicsRepo;
import com.Business.Electronics.exception.AddProductFailedException;
import com.Business.Electronics.exception.CloudinaryException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ElectronicService {

    private final ElectronicsRepo electronicsRepo;
    private final Cloudinary cloudinary;

    public ElectronicEntity addProduct(AddProDTO dto) {
        try {
            ElectronicEntity entity = toElectronicEntity(dto);
            return electronicsRepo.save(entity);
        } catch (AddProductFailedException e) {
            throw new AddProductFailedException(e.getMessage());
        }
    }

    public void deleteProduct(Long id) throws CloudinaryException {
        ElectronicEntity product = electronicsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item Not Found"));
        try {
            cloudinary.uploader().destroy(
                    product.getPublicId(),
                    ObjectUtils.emptyMap()
            );
        } catch (IOException e) {
            throw new CloudinaryException("Failed to delete image from storage");
        }
        electronicsRepo.delete(product);
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

    public ElectronicEntity toElectronicEntity(AddProDTO addPro) {
        return ElectronicEntity.builder()
                .model(addPro.getProductName())
                .price(addPro.getPrice())
                .url(addPro.getImageUrl())
                .condition(addPro.getDescription())
                .productType(addPro.getCategory())
                .monthStarted(addPro.getMonthStarted())
                .publicId(addPro.getPublicId())
                .brand(addPro.getBrand())
                .build();
    }

    public ElectronicEntity getProduct(Long id) {
        return electronicsRepo.getReferenceById(id);
    }
}
