package com.pnc.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pnc.project.entities.Permission;
import com.pnc.project.entities.RolePermission;
import com.pnc.project.entities.impl.RolePermissionId;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    @Query("SELECT rp.permission FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.method = :method")
    List<Permission> findPermissionsByRoleAndMethod(@Param("roleId") Long roleId, @Param("method") String method);
}