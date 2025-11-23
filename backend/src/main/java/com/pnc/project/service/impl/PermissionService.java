package com.pnc.project.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import com.pnc.project.dto.permission.PermissionDto;
import com.pnc.project.entities.Permission;
import com.pnc.project.repository.PermissionRepository;
import com.pnc.project.repository.RolePermissionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionService(PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Transactional
    public Page<Permission> findAll(int page, int size) {
        return permissionRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Optional<Permission> findById(int id) {
        return permissionRepository.findById(id);
    }

    @Transactional
    public void createIfNotExists(String path, String method) {
        Optional<Permission> existing = permissionRepository.findByPathAndMethod(path, method);
        if (existing.isEmpty()) {
            Permission permission = new Permission();
            permission.setPath(path);
            permission.setMethod(method);
            permissionRepository.save(permission);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Permission> findMatchingPermission(String requestPath, String method) {
        List<Permission> all = permissionRepository.findByMethod(method);
        return all.stream()
                .filter(p -> pathMatcher.match(p.getPath(), requestPath))
                .findFirst();
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(Long roleId, String realPath, String method) {
        List<Permission> permissions = rolePermissionRepository.findPermissionsByRoleAndMethod(roleId, method);
        System.out.println(permissions);
        return permissions.stream()
                .anyMatch(p -> pathMatcher.match(p.getPath(), realPath));
    }

    public Permission update(int id, PermissionDto dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        permission.setTitle(dto.getTitle());
        return permissionRepository.save(permission);
    }

}
