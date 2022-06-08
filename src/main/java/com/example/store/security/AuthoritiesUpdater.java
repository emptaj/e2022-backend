package com.example.store.security;

import com.example.store.entity.UserEntity;
import com.example.store.entity.WarehousePermissionEntity;
import com.example.store.service.UserService;
import com.example.store.service.WarehousePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthoritiesUpdater {

    @Transactional
    public void update(UserEntity user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<WarehousePermissionEntity> warehousePermissions = user.getWarehousePermissions();
        Collection<? extends GrantedAuthority> newAuthorities = createAuthorities(warehousePermissions);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), newAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public SimpleGrantedAuthority createAuthority(WarehousePermissionEntity permissionEntity) {
        return new SimpleGrantedAuthority(permissionEntity.getName());
    }

    public Collection<? extends GrantedAuthority> createAuthorities(Collection<WarehousePermissionEntity> permissionEntities) {
        return permissionEntities.stream().
                map(this::createAuthority)
                .collect(Collectors.toList());
    }
}
