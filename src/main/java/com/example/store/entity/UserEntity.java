package com.example.store.entity;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.example.store.entity.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Users")
public class UserEntity implements UserDetails {
    @Id
    @SequenceGenerator(name = "sequence_user_id", sequenceName = "sequence_user_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
    private Boolean locked;
    private Boolean enabled;

    @CreationTimestamp
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<WarehousePermissionEntity> warehousePermissions;

    @OneToOne
    private AddressEntity address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return warehousePermissions.stream()
                .map(permissionEntity -> new SimpleGrantedAuthority(permissionEntity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
