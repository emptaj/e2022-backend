package com.example.store.entity;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.example.store.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean locked;
    private Boolean activated;
    
    @CreationTimestamp
    private Date creationDate;

    @Enumerated
    private UserRole userRole;

    @OneToOne
    private AddressEntity address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
