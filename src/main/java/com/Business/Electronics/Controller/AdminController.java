package com.Business.Electronics.Controller;

import com.Business.Electronics.DTO.UserDTO;
import com.Business.Electronics.Entity.ElectronicEntity;
import com.Business.Electronics.Service.ElectronicService;
import com.Business.Electronics.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ElectronicService electronicService;

    @GetMapping("/test")
    public String admin() {
        return "Admin";
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> allUser = userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

    @GetMapping("/total-users")
    public ResponseEntity<Integer> totalUsers() {
        Integer totalUser = userService.totalUsers();
        return ResponseEntity.ok(totalUser);
    }

    @GetMapping("/total-products")
    public ResponseEntity<Integer> totalProducts() {
        Integer totalProducts = electronicService.totalProducts();
        return ResponseEntity.ok(totalProducts);
    }

    @GetMapping("/available-products")
    public ResponseEntity<Integer> avlProducts() {
        Integer avlProducts = electronicService.avlProducts();
        return ResponseEntity.ok(avlProducts);
    }

    @GetMapping("/recent-products")
    public ResponseEntity<List<ElectronicEntity>> recentProducts() {
        List<ElectronicEntity> recentProducts = electronicService.recentProducts();
        return ResponseEntity.ok(recentProducts);
    }

    @PutMapping("/make-admin/{id}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
        boolean makeAdmin = userService.makeAdmin(id);
        if(makeAdmin) return ResponseEntity.ok("User Changes to Admin");
        return ResponseEntity.ok("Cannot Find The User");
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleteUser = userService.deleteUser(id);
        if(deleteUser) return ResponseEntity.ok("User Deleted");
        return ResponseEntity.ok("Cannot Find The User");
    }

    @PutMapping("/block-user/{id}")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok("User Blocked");

    }

    @PutMapping("/unblock-user/{id}")
    public ResponseEntity<?> unBlockUser(@PathVariable Long id) {
        userService.unBlockUser(id);
        return ResponseEntity.ok("User Un-Blocked");
    }

    // admin manipulating with products
    @PutMapping("/products/add-product")
    public ResponseEntity<ElectronicEntity> addProduct(@RequestBody ElectronicEntity entity) {
        ElectronicEntity electronicEntity = electronicService.addProduct(entity);
        return ResponseEntity.ok(electronicEntity);
    }

    @DeleteMapping("/products/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        electronicService.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted");
    }


}
