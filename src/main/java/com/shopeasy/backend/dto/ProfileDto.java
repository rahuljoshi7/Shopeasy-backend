package com.shopeasy.backend.dto;

import com.shopeasy.backend.entity.Profile;
import lombok.Data;

import java.util.UUID;

@Data
public class ProfileDto {
    private UUID id;
    private String fullName;
    private String username;

    public static ProfileDto from(Profile p) {
        ProfileDto dto = new ProfileDto();
        dto.id = p.getId();
        dto.fullName = p.getFullName();
        dto.username = p.getUsername();
        return dto;
    }
}
