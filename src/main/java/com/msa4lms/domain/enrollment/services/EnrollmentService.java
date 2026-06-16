package com.msa4lms.domain.enrollment.services;

import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.mapper.EnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentMapper enrollmentMapper;

    /**
     * 학생의 수강 내역 목록과 총 신청 학점 조회
     */
    public EnrollmentRes getMyEnrollments(Long studentId, int year, int semester) {
        List<EnrollmentRes.EnrollmentDetail> enrollments = 
            enrollmentMapper.findMyEnrollments(studentId, year, semester);
        
        int totalCredits = enrollmentMapper.calculateTotalCredits(studentId, year, semester);
        
        return new EnrollmentRes(enrollments, totalCredits);
    }
}
