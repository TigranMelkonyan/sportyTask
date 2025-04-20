package com.sporty.iam.domain.entity.user;


import com.sporty.iam.domain.entity.common.audit.AuditableBaseEntity;
import com.sporty.iam.domain.model.common.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Tigran Melkonyan
 * Date: 2/18/25
 * Time: 4:27 PM
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends AuditableBaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private int loyaltyPoints;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

}
