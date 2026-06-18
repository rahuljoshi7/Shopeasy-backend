package com.shopeasy.backend.controller;

import com.shopeasy.backend.dto.ProfileDto;
import com.shopeasy.backend.service.ProfileService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                profileService.getProfile(getUserId(jwt))
        );
    }

    @PatchMapping
    public ResponseEntity<ProfileDto> updateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateProfileRequest req) {

        return ResponseEntity.ok(
                profileService.updateProfile(
                        getUserId(jwt),
                        req.getFullName(),
                        req.getUsername()
                )
        );
    }

    @Data
    static class UpdateProfileRequest {
        private String fullName;
        private String username;
    }
}