package com.msa4lms.domain.profile.services;

import com.msa4lms.domain.profile.mapper.ProfessorMapper;
import com.msa4lms.domain.profile.responses.ProfessorProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorMapper professorMapper;

    public ProfessorProfileRes getProfile(Long userId) {
        return professorMapper.findProfileByUserId(userId);
    }
}
