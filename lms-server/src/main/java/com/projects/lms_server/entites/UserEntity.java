package com.projects.lms_server.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.lms_server.entites.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.projects.lms_server.utils.GenerateID.generateId;

@Entity
@Table(name = "user")
@Data
public class UserEntity implements UserDetails {

    @Id
    private String userId;

    private String name;

    private String email;

    private String address;

    private String contactNo;

    private String image;

    private String gender;

    private Boolean isActive = true;

    private String password;

    private LocalDate enrollmentDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles role;

    /* Assigning the user id to each new User */
    private static final String PREFIX = "USER-";
    @PrePersist
    private void assignUserId() {
        this.userId = PREFIX + generateId();
    }

    /* UserDetails Implementation */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActive;
    }

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<BookCopyEntity> borrowedBooks = new HashSet<>();

    private String approvedBy;


}
