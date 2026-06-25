package com.msa4lms.domain.profile.services;

import com.msa4lms.domain.profile.mapper.StudentMapper;
import com.msa4lms.domain.profile.responses.StudentProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentMapper studentMapper;

    public StudentProfileRes getProfile(Long userId){
        return studentMapper.findProfileByUserId(userId);
    }
}
