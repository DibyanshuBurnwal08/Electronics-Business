package com.Business.Electronics.Repository;

import com.Business.Electronics.Entity.ElectronicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicsRepo extends JpaRepository<ElectronicEntity, Long> {

}
