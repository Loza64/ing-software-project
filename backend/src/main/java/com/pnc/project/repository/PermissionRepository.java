package com.pnc.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pnc.project.entities.Permission;

@EnableJpaRepositories
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Page<Permission> findAll(Pageable pageable);

    Optional<Permission> findByPathAndMethod(String path, String method);

    List<Permission> findByMethod(String method);
}
