package com.Business.Electronics.Repository;

import com.Business.Electronics.Entity.ElectronicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectronicsRepo extends JpaRepository<ElectronicEntity, Long> {

    @Query("SELECT COUNT(e) FROM ElectronicEntity e")
    Integer totalProducts();

    @Query("SELECT COUNT(e) FROM ElectronicEntity e where e.available = true")
    Integer avlProducts();

    List<ElectronicEntity> findTop10ByOrderByCreatedAtDesc();
}
