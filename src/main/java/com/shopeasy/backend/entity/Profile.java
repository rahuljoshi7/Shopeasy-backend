package com.shopeasy.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Maps to public.profiles, which is auto-created by the Supabase trigger
 * handle_new_user() on auth.users INSERT.
 * The id is the same UUID as auth.users.id (the Supabase user id embedded in the JWT).
 */
@Entity
@Table(name = "profiles", schema = "public")
@Getter @Setter @NoArgsConstructor
public class Profile {

    @Id
    private UUID id;          // == auth.users.id == JWT sub claim

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String username;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    private void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}
