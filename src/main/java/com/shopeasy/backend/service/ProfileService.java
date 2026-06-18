package com.shopeasy.backend.service;

import com.shopeasy.backend.dto.ProfileDto;
import com.shopeasy.backend.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public ProfileDto getProfile(UUID userId) {
        return profileRepository.findById(userId)
                .map(ProfileDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));
    }

    @Transactional
    public ProfileDto updateProfile(UUID userId, String fullName, String username) {
        var profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + userId));

        if (fullName != null) profile.setFullName(fullName);
        if (username != null) profile.setUsername(username);

        return ProfileDto.from(profileRepository.save(profile));
    }
}
