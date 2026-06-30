package com.Business.Electronics.Controller;

import com.Business.Electronics.Entity.ElectronicEntity;
import com.Business.Electronics.Service.ElectronicService;
import com.Business.Electronics.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final ElectronicService electronicService;
    private final UserService service;

    @GetMapping("/products/getAllProducts")
    public ResponseEntity<List<ElectronicEntity>> getAll() {
        List<ElectronicEntity> list = electronicService.getProducts();
        System.out.println(list);
        return ResponseEntity.ok(list);
    }


}
