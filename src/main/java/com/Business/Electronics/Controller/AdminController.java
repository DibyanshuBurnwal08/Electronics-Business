package com.Business.Electronics.Controller;

import com.Business.Electronics.DTO.ElectDTO;
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

    @PutMapping("make-admin/{email}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable String email) {
        boolean makeAdmin = userService.makeAdmin(email);
        if(makeAdmin) return ResponseEntity.ok("User Changes to Admin");
        return ResponseEntity.ok("Cannot Find The User");
    }

    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        boolean deleteUser = userService.deleteUser(email);
        if(deleteUser) return ResponseEntity.ok("User Deleted");
        return ResponseEntity.ok("Cannot Find The User");
    }

    @PutMapping("/block-user/{email}")
    public ResponseEntity<?> blockUser(@PathVariable String email) {
        userService.blockUser(email);
        return ResponseEntity.ok("User Blocked");

    }

    @PutMapping("/unblock-user/{email}")
    public ResponseEntity<?> unBlockUser(@PathVariable String email) {
        userService.unBlockUser(email);
        return ResponseEntity.ok("User Un-Blocked");
    }

    // admin manipulating with products
    @PutMapping("admin/products/add-product")
    public ResponseEntity<ElectronicEntity> addProduct(@RequestBody ElectronicEntity entity) {
        ElectronicEntity electronicEntity = electronicService.addProduct(entity);
        return ResponseEntity.ok(electronicEntity);
    }

    @DeleteMapping("admin/products/delete-product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        electronicService.deleteProduct(id);
        return ResponseEntity.ok("");
    }


}
