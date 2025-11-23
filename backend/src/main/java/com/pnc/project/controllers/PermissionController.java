package com.pnc.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pnc.project.dto.permission.PermissionDto;
import com.pnc.project.dto.response.Pagination;
import com.pnc.project.entities.Permission;
import com.pnc.project.service.impl.PermissionService;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<Pagination<Permission>> findAll(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        var permissionsPage = permissionService.findAll(page, size);
        var response = new Pagination<Permission>(
                permissionsPage.getContent(),
                permissionsPage.getNumber(),
                permissionsPage.getSize(),
                permissionsPage.getTotalPages(),
                permissionsPage.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> findById(@PathVariable int id) {
        return permissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> update(@PathVariable int id, @RequestBody PermissionDto dto) {
        Permission updated = permissionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

}
