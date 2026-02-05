package com.walletiq.entity;

import com.walletiq.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import static jakarta.persistence.GenerationType.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        },
        indexes = {
                @Index(columnList = "email", name = "idx_email"),
                @Index(columnList = "full_name", name = "idx_full_name")
        }
)
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    /* User metadata */

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role = Role.USER;

    /* Profile picture */

    @Column(name = "profile_picture_url", length = 1025)
    private String profilePictureUrl;

    @Column(name = "profile_picture_public_id", length = 255)
    private String profilePicturePublicId;

    /* Email verification */

    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "email_verified_at")
    private Instant emailVerifiedAt;

    /* Audit metadata */

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;

    /* Relationships */


    /* Domain Methods */

    public void verifyEmail(Instant now) {
        this.emailVerified = true;
        this.emailVerifiedAt = now;
    }

    public void updateFullName(String fullName) {
        this.fullName = fullName;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfilePicture(String url, String publicId) {
        this.profilePictureUrl = url;
        this.profilePicturePublicId = publicId;
    }


    /* UserDetails Methods need to Override */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = (role != null) ? "ROLE_" + role.name() : "ROLE_USER";
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
